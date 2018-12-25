<template>
  <div id='ops-fit' class="ops-fit">
    <slot></slot>
  </div>
</template>

<script>
  export default {
    name: 'opsFit',
    data () {
      return {
        timer: false
      }
    },
    mounted: function () {
      let that = this
      that.resize(that)
      window.addEventListener('resize', () => {
        return (() => {
          that.resize(that)
        })()
      })
    },
    methods: {
      resize (that) {
        if (!that.timer) {
          that.timer = true
          setTimeout(() => {
            that.timer = false
            let node = document.getElementById('ops-fit')
            if (node) {
              let parentNode = node.parentNode
              let offsetParentNode = node.offsetParent
              if (parentNode && offsetParentNode) {
                let childNodes = parentNode.childNodes
                // 主要计算当前控件高度 parentNode.offsetTop * 2是现在的父节点有margin:10
                let parentOffsetHeight = offsetParentNode.offsetHeight - parentNode.offsetTop * 2
                if (parentOffsetHeight > 0) {
                  let nodeHeight = parentOffsetHeight
                  for (let j = 0; j < childNodes.length; j++) {
                    if (childNodes[j].className.indexOf('ops-fit') === -1) {
                      nodeHeight -= childNodes[j].offsetHeight
                    }
                  }
                  if (nodeHeight > 0) {
                    node.style.transition = 'all 1s'
                    node.style.cssText = 'height:' + nodeHeight + 'px'
                  }
                }
              }
            }
          }, 250)
        }
      }
    }
  }
</script>

<style scoped>
  .ops-fit {
    width: 100%;
  }
</style>
