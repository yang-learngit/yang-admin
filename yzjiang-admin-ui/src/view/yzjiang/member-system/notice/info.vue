<template>
  <div>
    <Form
      ref="noticeForm"
      :model="noticeForm"
      label-position="top">
      <FormItem label="标题" prop="title">
        <Input
          v-model="noticeForm.title"
          placeholder="标题"
          style="width: 100%;"
        />
      </FormItem>
      <FormItem label="摘要" prop="summary">
        <Input
          v-model="noticeForm.summary"
          placeholder="摘要"
          style="width: 100%;"
        />
      </FormItem>
      <!--<FormItem label="公告内容" prop="content">-->
      <!--<editor1 v-model="noticeForm.content"></editor1>-->
      <!--</FormItem>-->

      <FormItem label="内容" prop="title">
        <editor ref="editor"
                v-bind:id="id"
                v-bind:isTransferContent="isTransferContent"
                @transferContent="setContent"/>
      </FormItem>
    </Form>
  </div>
</template>

<script>
  import api from '@/api'
  import editor from '@/view/components/yzjiang/editor'

  export default {
    name: 'noticeInfo',
    components: {editor},
    props: {
      _cache: {type: String, default: ''},
      noticeId: {type: Number, default: 0}
    },
    data () {
      // 添加表单校验规则
      return {
        moduleName: 'ops_static_page',
        id: '0',
        isTransferContent: true,
        noticeForm: {
          title: '',
          summary: '',
          content: '',
          moduleName: 'notice_content',
          id: 0,
          isTransferContent: true
        }
      }
    },
    created () {
      this.$on('addSave', (callback) => {
        // 验证表单
        // this.$refs['noticeForm'].validate((valid) => {
        //   if (valid) {
        //     this.addSave(callback)
        //   } else {
        //     callback(valid)
        //   }
        // })
        this.addSave(callback)
      })
      this.loadData()
    },
    methods: {
      loadData () {
        let params = {}
        params.id = this.noticeId
        api.notice.find(params).then(response => {
          let result = response.data
          if (result.code === '200') {
            this.noticeForm.title = result.data.title
            this.noticeForm.summary = result.data.summary
            this.noticeForm.content = result.data.content
            this.$refs['editor'].setContent(this.noticeForm.content)
          }
        })
      },
      addSave (callback) { // 新增页面
        const params = {}
        params.title = this.noticeForm.title.trim()
        params.summary = this.noticeForm.summary
        params.content = this.noticeForm.content
        console.log(this.noticeForm)
        api.notice.add(params).then(response => {
          const result = response.data
          console.log(result)
          if (result.code === '200') {
            this.$Message.success('新增公告成功')
          } else {
            this.$Message.error(`新增公告失败:${result.message}`)
          }
          let flag = result.code === '200'
          callback(flag)
        })
      },
      setContent (content) {
        this.noticeForm.content = content
      }
    }
  }
</script>

<style scoped>

</style>
