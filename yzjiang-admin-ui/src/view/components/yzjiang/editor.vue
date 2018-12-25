<template>
  <div ref="editor"></div>
</template>

<script>
  import WangEditor from 'wangeditor'

  export default {
    name: 'editor',
    props: {
      id: String, // 当前编辑记录的主键
      isTransferContent: Boolean // 是否需要将编辑的内容传递给父组件
    },
    data () {
      return {
        editor: null,
        editorContent: '请编辑你的内容'
      }
    },
    methods: {
      getContent () {
        return this.editor.txt.html()
      },
      setContent (content) {
        this.editor.txt.html(content)
      }
    },
    created () {
    },
    mounted () {
      // 初始化editor
      this.editor = new WangEditor(this.$refs.editor)
      this.editor.customConfig.onchange = (html) => {
        this.editorContent = html
        // 讲内容传递给父组件
        if (this.isTransferContent) {
          this.$emit('transferContent', html)
        }
      }
      this.editor.customConfig.uploadImgShowBase64 = true
      // this.editor.customConfig.showLinkImg = true // 显示网络图片tab
      // this.editor.customConfig.uploadFileName = 'uploadFile' // 自定义文件名
      // this.editor.customConfig.uploadImgMaxSize = 4 * 1024 * 1024 // 设置上传图片大小 4M
      // this.editor.customConfig.uploadImgServer = '/api/page/upload'
      // this.editor.customConfig.uploadImgServer = 'http://localhost:6510/api/page/upload'
      // // 上传图片时可自定义传递一些参数 参数会被添加到formdata中
      // this.editor.customConfig.uploadImgParams = {
      //   moduleName: this.moduleName, // 所属的模块名 后台上传图片保存路径为：操作系统临时目录+模块名称+id的目录
      //   id: this.id
      // }
      this.editor.create()
    }
  }
  // import E from 'wangeditor'
  // export default {
  //   name: 'editor',
  //   data () {
  //     return {
  //       editor: null,
  //     }
  //   },
  //   props: {
  //     value: {
  //       default: ""
  //     }
  //   },
  //   mounted() {
  //     this.editor = new E(this.$refs.editor)
  //     this.editor.customConfig.uploadImgShowBase64 = true
  //     this.editor.customConfig.onchange = (html) => { // 获取内容
  //       this.$emit("input", html);
  //     }
  //     this.editor.create();
  //     this.editor.txt.html(this.value); // 设置内容
  //   }
  // }
</script>

<style scoped>

</style>
