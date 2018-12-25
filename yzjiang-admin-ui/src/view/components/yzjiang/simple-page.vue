<template>
  <ul :class="simpleWrapClasses" :style="styles">
    <li
      :title="t('i.page.prev')"
      :class="prevClasses"
      @click="prev">
      <a><i class="ivu-icon ivu-icon-ios-arrow-left"></i></a>
    </li>
    <div :class="simplePagerClasses" :title="currentPage">
      <input
        type="text"
        :value="currentPage"
        autocomplete="off"
        spellcheck="false"
        @keydown="keyDown"
        @keyup="keyUp"
        @change="keyUp"
        :style="{width:'90px'}">
    </div>
    <li
      :title="t('i.page.next')"
      :class="nextClasses"
      @click="next">
      <a><i class="ivu-icon ivu-icon-ios-arrow-right"></i></a>
    </li>
    <div v-if="showSizer" :class="sizerClasses">
      <i-select v-model="currentPageSize" :size="size" :placement="placement" :transfer="transfer"
                @on-change="changeSize">
        <i-option v-for="item in pageSizeOpts" :key="item" :value="item" style="text-align:center;">{{ item }} {{
          t('i.page.page') }}
        </i-option>
      </i-select>
    </div>
  </ul>

</template>

<script>
  import Locale from 'iview/src/mixins/locale'

  const prefixCls = 'ivu-page'
  export default {
    // 组件名称：命名使用PascalCase (大骆驼拼写法) ，文件名使用kebab-case（短横线命名）
    name: 'SimplePage',
    mixins: [Locale],
    // 子组件
    components: {},
    // 定义变量source
    data () {
      return {
        currentPage: this.current,
        currentPageSize: this.pageSize

      }
    },
    // 接收父组件传递过来的参数
    props: {
      current: {
        type: Number,
        default: 1
      },
      pageSize: {
        type: Number,
        default: 10
      },
      placement: {
        type: String
      },
      transfer: Boolean,
      pageSizeOpts: Array,
      showSizer: {
        type: Boolean,
        default: false
      },
      styles: {
        type: Object
      },
      isSmall: Boolean,
      pageDataLength: {
        type: Number,
        default: 0
      }
    },
    // html加载完成之前，执行。执行顺序：父组件-子组件
    created () {
    },
    // html加载完成后执行。执行顺序：子组件-父组件
    mounted () {
    },
    // 监听属性变化触发函数
    watch: {
      current (val) {
        this.currentPage = val
      }

    },
    // 计算属性，也就是依赖其它的属性计算所得出最后的值
    computed: {
      size () {
        return this.isSmall ? 'small' : 'default'
      },
      sizerClasses () {
        return [
          `${prefixCls}-options-sizer`
        ]
      },
      simpleWrapClasses () {
        return [
          `${prefixCls}`,
          `${prefixCls}-simple`,
          {
            [`${this.className}`]: !!this.className
          }
        ]
      },
      simplePagerClasses () {
        return `${prefixCls}-simple-pager`
      },
      prevClasses () {
        return [
          `${prefixCls}-prev`,
          {
            [`${prefixCls}-disabled`]: this.currentPage === 1
          }
        ]
      },
      nextClasses () {
        return [
          `${prefixCls}-next`,
          {
            // [`${prefixCls}-disabled`]: this.currentPage === this.allPages
            [`${prefixCls}-disabled`]: this.pageDataLength < this.pageSize
          }
        ]
      }

    },
    // 事件方法执行
    methods: {
      changeSize (pageSize) {
        this.$emit('on-page-size-change', pageSize)
        this.changePage(1)
      },
      changePage (page) {
        if (this.currentPage !== page) {
          this.currentPage = page
          this.$emit('update:current', page)
          this.$emit('on-change', page)
        }
      },
      prev () {
        const current = this.currentPage
        if (current <= 1) {
          return false
        }
        this.changePage(current - 1)
      },
      next () {
        const current = this.currentPage
        if (this.pageDataLength >= this.pageSize) {
          this.changePage(current + 1)
        }
      },
      keyDown (e) {
        const key = e.keyCode
        const condition = (key >= 48 && key <= 57) || (key >= 96 && key <= 105) || key === 8 || key === 37 || key === 39

        if (!condition) {
          e.preventDefault()
        }
      },
      keyUp (e) {
        const key = e.keyCode
        const val = parseInt(e.target.value)

        if (key === 38) {
          this.prev()
        } else if (key === 40) {
          this.next()
        } else if (key === 13) {
          let page = 1

          if (val > this.allPages) {
            page = this.allPages
          } else if (val <= 0 || !val) {
            page = 1
          } else {
            page = val
          }

          e.target.value = page
          this.changePage(page)
        }
      }

    }
  }
</script>

<style scoped>

</style>
