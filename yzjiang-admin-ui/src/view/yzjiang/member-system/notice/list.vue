<template>
  <ops-fit>
    <ops-table
      :columns="columns"
      :data="tableData"
      :current="page"
      :total="tableDataTotal"
      :page-size.sync="pageSize"
      @on-change="changePage"
      @on-page-size-change="onPageSizeChange"
      :pageDataLength="pageDataLength"
      simple
      showSizer
    >
      <Row slot="header" style="padding:0px 10px">
        <Button type="primary" @click="addNotice" class="margin-right-10 margin-top-5">新增公告</Button>
        <Button type="primary" @click="kanshiping" class="margin-right-10 margin-top-5">看视屏</Button>
      </Row>
    </ops-table>

    <Modal ref="addModal" v-model="addNoticeDialog" :mask-closable="false" title="新增公告" width="800" loading
           okText="保存" @on-ok="addSave">
      <p slot="header" style="text-align: center">
        <span>新增公告</span>
      </p>
      <notice-info ref="add" v-if="addNoticeDialog" :cacheTime="addNoticeCacheTime"></notice-info>
    </Modal>

    <!-- 编辑 -->
    <Modal ref="editModal" v-model="editNoticeDialog" :mask-closable="false" title="编辑" width="800" loading
           okText="保存" @on-visible-change="closeEdit"
           @on-ok="editSave">
      <p slot="header" style="text-align: center">
        <span>编辑</span>
      </p>
      <notice-info v-if="editNoticeDialog" ref="edit" :noticeId="noticeId" :cacheTime="editNoticeCacheTime" ></notice-info>
    </Modal>

    <Modal ref="addModal" v-model="kanshipingDialog" :mask-closable="false" width="700"
            >
      <p slot="header" style="text-align: center">
        <span>看视频</span>
      </p>
      <video-player  class="video-player-box"
                     ref="videoPlayer"
                     :options="playerOptions"
                     >
      </video-player>

    </Modal>

  </ops-fit>
</template>

<script>
  import api from '@/api'
  import OpsFit from '@/view/components/yzjiang/ops-fit'
  import OpsTable from '@/view/components/yzjiang/ops-table'
  import buttons from '@/view/components/yzjiang/buttons'
  import noticeInfo from './info'
  import util from '@/libs/util'
  import 'video.js/dist/video-js.css'
  import { videoPlayer } from 'vue-video-player'

  export default {
    name: 'list',
    components: {
      OpsFit,
      OpsTable,
      buttons,
      noticeInfo,
      videoPlayer
    },
    props: {
      _cache: {type: String, default: ''}
    },
    data () {
      return {
        playerOptions: {
          muted: false,
          language: 'en',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [{
            type: 'application/x-mpegURL',
            src: 'http://yamei-adr-oss.iauto360.cn/G_123_20181105192100_0_10_H_lmqKUNr6W92fAHem7_XRQT7ukHn_.mp4'
          }],
          notSupportedMessage: '此视频暂无法播放，请稍后再试',
          poster: '/static/images/author.jpg'
        },
        innerHeight: window.innerHeight,
        kanshipingDialog: false,
        addNoticeDialog: false,
        addNoticeCacheTime: 0,
        editNoticeDialog: false,
        editNoticeCacheTime: 0,
        page: 1,
        pageSize: 20,
        loading: true,
        pageDataLength: 0,
        columns: [
          {
            title: '',
            width: 40,
            align: 'center',
            className: 'operator',
            render: (h, data) => {
              let operText = '发布'
              if (data.row.status === 1) {
                operText = '取消发布'
              }
              return h(buttons, {
                props: {
                  row: data.row,
                  menus: [{
                    key: 'edit',
                    title: '编辑'
                  }, {
                    key: 'status',
                    title: operText
                  }, {
                    key: 'delete',
                    title: '删除'
                  }]
                },
                on: {
                  // 编辑
                  edit: row => {
                    this.noticeId = row.id
                    this.editNoticeDialog = true
                  },
                  updateStatus: (row) => {
                    if (row) {
                      let texts = '发布'
                      let changStatus = 0
                      if (row.status === 1) {
                        texts = '取消发布'
                      } else if (row.status === 0) {
                        texts = '发布'
                        changStatus = 1
                      }
                      let content = '<p>将此区域设置为' + texts
                      let okText = '确认'
                      // 删除二次确认框
                      this.$Modal.confirm({
                        title: '消息',
                        content: content,
                        loading: true,
                        okText: okText,
                        onOk: () => {
                          let params = new FormData()
                          params.append('noticeId', row.id)
                          params.append('showStatus', changStatus)
                          // ajax提交数据
                          api.notice.updateStatus(params).then(response => {
                            this.$Modal.remove()
                            const result = response.data
                            if (result.code === '200') {
                              this.loadTableData()
                              this.$Message.success(texts + '成功')
                            } else {
                              this.$Message.error(texts + `失败:${result.message}`)
                            }
                          })
                        }
                      })
                    }
                  }
                }
              })
            }
          },
          {
            title: '标题',
            minWidth: 100,
            align: 'center',
            key: 'title'
          },
          {
            title: '摘要',
            align: 'center',
            minWidth: 80,
            key: 'summary'
          },
          {
            title: '发布状态',
            align: 'center',
            minWidth: 80,
            key: 'status',
            render: function (h, param) {
              let texts = ''
              if (param.row.status === '1') {
                texts = '发布'
              } else if (param.row.status === '0') {
                texts = '未发布'
              } else {
                texts = ''
              }
              return h('div', texts)
            }
          },
          {
            title: '发布时间',
            align: 'center',
            minWidth: 150,
            render (h, param) {
              if (param.row.status === '1' && param.row.updateTime) {
                return h('span', util.moment(param.row.updateTime).format('YYYY-MM-DD HH:mm'))
              }
              return h('span', '')
            }
          }
        ],
        data: []
      }
    },
    watch: {
      _cache () {
        this.loadTableData(1)
      }
    },
    created () {

    },
    computed: {
      tableData: function () {
        return this.data || []
      },
      tableDataTotal: function () {
        return this.data.total
      },
      tableHeight () {
        return this.innerHeight - 320
      }
    },
    mounted: function () {
      this.loadTableData()
      this.$on('refresh', function () {
        this.loadTableData(1)
      })
    },
    methods: {
      kanshiping () {
        this.kanshipingDialog = true
      },
      onPageSizeChange () {
        if (this.page === 1) {
          this.loadTableData(1)
        }
      },
      changePage (page) {
        this.loadTableData(page)
      },
      loadTableData: function (page) {
        this.page = Number.isInteger(page) ? page : 1
        this.loading = true
        let params = new FormData()
        params.append('pageIndex', this.page + '')
        params.append('pageSize', this.pageSize + '')
        api.notice.list(params).then(response => {
          let result = response.data
          console.log(result)
          if (result.code === '200') {
            this.data = result.data
            this.pageDataLength = 10
          } else {
            this.$Message.error(result.message)
          }
          this.loading = false
        })
      },
      addNotice: function () {
        this.addNoticeCacheTime = new Date().getTime()
        this.addNoticeDialog = true
      },
      addSave: function () {
        this.$refs['add'].$emit('addSave', (result) => {
          this.$refs['addModal'].buttonLoading = false
          if (result) {
            // 重新加载表格数据
            this.loadTableData()
            this.addNoticeDialog = false
          }
        })
      },
      editSave: function () {
        this.$refs['edit'].$emit('editSave', (result) => {
          this.$refs['editModal'].buttonLoading = false
          if (result) {
            // 重新加载表格数据
            this.noticeId = 0
            this.loadTableData()
            this.editNoticeDialog = false
          }
        })
      },
      closeEdit (v) {
        if (!v) {
          this.noticeId = 0
        }
      }
    }
  }
</script>
