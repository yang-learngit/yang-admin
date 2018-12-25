<template>
  <div
    ref="opsTable"
    class="ops-table">
    <div class="margin-bottom-10"
         ref="queryForm"
         v-if="showSlotQueryForm"
         style="padding:0px 10px ">
      <slot
        name="queryForm">
      </slot>
    </div>
    <div ref="head" v-if="showSlotHeader" class="table-head">
      <slot
        name="header">
      </slot>
    </div>
    <Table
      ref="datagrid"
      :columns="columns"
      :data="data"
      :stripe="stripe"
      :border="border"
      :no-data-text="noDataText"
      :size="size"
      :disabled-hover="disabledHover"
      :highlight-row="highlightRow"
      :row-class-name="rowClassName"
      :height="tableHeight"
      @on-selection-change="onSelectionChange"
      @on-current-change="onCurrentChange"
      @on-select="onSelect"
      :loading="loading"
    >
    </Table>
    <div ref="page" class="table-page">
      <simple-page
        :current="current"
        :page-size-opts="pageSizeOpts"
        :page-size="pageSize"
        :showSizer="showSizer"
        :pageDataLength="pageDataLength"
        @on-change="changePage"
        @on-page-size-change="onPageSizeChange"
        v-if="simple"></simple-page>
      <Page v-else
        placement="top"
        :current="current"
        :total="total"
        :page-size-opts="pageSizeOpts"
        :page-size="pageSize"
        :showTotal="showTotal"
        :showElevator="showElevator"
        :showSizer="showSizer"
        @on-change="changePage"
        @on-page-size-change="onPageSizeChange"
      ></Page>
    </div>
  </div>
</template>
<script>
  import {oneOf} from 'iview/src/utils/assist'
  import SimplePage from './simple-page'

  export default {
    name: 'opsTable',
    components: {SimplePage},
    props: {
      columns: {
        type: Array,
        default: function () {
          return []
        }
      },
      data: {
        type: Array,
        default: function () {
          return []
        }
      },
      current: {
        type: Number,
        default: 1
      },
      pageSize: {
        type: Number,
        default: 10
      },
      loading: {
        type: Boolean,
        default: false
      },
      stripe: {
        type: Boolean,
        default: false
      },
      border: {
        type: Boolean,
        default: false
      },
      disabledHover: {
        type: Boolean,
        default: false
      },
      highlightRow: {
        type: Boolean,
        default: false
      },
      rowClassName: {
        type: Function
      },
      size: {
        validator (value) {
          return oneOf(value, ['large', 'small', 'default'])
        }
      },
      noDataText: {
        type: String,
        default: '暂无数据'
      },
      showTotal: {
        type: Boolean,
        default: true
      },
      showElevator: {
        type: Boolean,
        default: false
      },
      showSizer: {
        type: Boolean,
        default: false
      },
      simple: {
        type: Boolean,
        default: false
      },
      pageSizeOpts: {
        type: Array,
        default () {
          return [10, 20, 40]
        }
      },
      total: {
        type: Number,
        default: 0
      },
      height: {
        type: Number
      },
      pageDataLength: {
        type: Number,
        default: 0
      }
    },
    data () {
      return {
        timer: false,
        opsTableHeight: 0,
        queryFormHeight: 0,
        headHeight: 0,
        pageHeight: 0,
        showSlotHeader: false,
        showSlotQueryForm: false,
        tableHeight: null
      }
    },
    mounted: function () {
      this.showSlotHeader = this.$slots.header !== undefined
      this.showSlotQueryForm = this.$slots.queryForm !== undefined
      let that = this
      that.resize(that)
      window.addEventListener('resize', () => {
        return (() => {
          that.resize(that)
        })()
      })
    },
    created () {
      if (this.height) {
        this.tableHeight = this.height
      }
    },
    computed: {},
    watch: {
      opsTableHeight () {
        if (this.height) {
          this.tableHeight = this.height
        } else if (this.opsTableHeight > 0) {
          let height = this.opsTableHeight - this.queryFormHeight - this.headHeight - this.pageHeight - 10
          if (height > 0) {
            this.tableHeight = height
          }
        }
      },
      queryFormHeight () {
        if (this.height) {
          this.tableHeight = this.height
        } else if (this.opsTableHeight > 0) {
          let height = this.opsTableHeight - this.queryFormHeight - this.headHeight - this.pageHeight - 10
          if (height > 0) {
            this.tableHeight = height
          }
        }
      },
      headHeight () {
        if (this.height) {
          this.tableHeight = this.height
        } else if (this.opsTableHeight > 0) {
          let height = this.opsTableHeight - this.queryFormHeight - this.headHeight - this.pageHeight - 10
          if (height > 0) {
            this.tableHeight = height
          }
        }
      }
    },
    methods: {
      changePage (page) {
        this.$emit('update:current', page)
        this.$emit('on-change', page)
      },
      onPageSizeChange (pageSize) {
        this.$emit('update:pageSize', pageSize)
        this.$emit('on-page-size-change', pageSize)
      },
      onSelectionChange (selection) {
        this.$emit('on-selection-change', selection)
      },
      onSelect (selection) {
        this.$emit('on-current-change', selection)
      },
      onCurrentChange (currentRow, oldCurrentRow) {
        this.$emit('on-current-change', currentRow, oldCurrentRow)
      },
      resize (that) {
        if (!that.timer) {
          that.timer = true
          setTimeout(() => {
            that.timer = false
            if (that.$refs.opsTable) {
              that.opsTableHeight = that.$refs.opsTable.offsetHeight
            }
            if (that.$refs.queryForm) {
              that.queryFormHeight = that.$refs.queryForm.offsetHeight
            }
            if (that.$refs.page) {
              that.pageHeight = that.$refs.page.offsetHeight
            }
            if (that.$refs.head) {
              that.headHeight = that.$refs.head.offsetHeight
            }
          }, 500)
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  .ops-table {
    width: 100%;
    height: 100%;
    padding: 5px;
  }

  .table-head {
    border-top: 1px solid #e9eaec;
    border-left: 1px solid #e9eaec;
    border-right: 1px solid #e9eaec;
    width: 100%;
    padding: 5px 10px;
    background-color: white;
  }

  .table-page {
    border-bottom: 1px solid #e9eaec;
    border-left: 1px solid #e9eaec;
    border-right: 1px solid #e9eaec;
    width: 100%;
    padding: 5px;
    background-color: white;
    text-align: right
  }
</style>
