<template>
  <Row>
    <Table :columns="columns" :height="tableHeight" :data="tableData"></Table>
  </Row>
</template>

<script>
  import api from '@/api'

  export default {
    name: 'list',
    data () {
      return {
        innerHeight: window.innerHeight,
        loading: true,
        data: [],
        columns: [
          {
            title: '姓名',
            key: 'name'
          },
          {
            title: '年龄',
            key: 'age'
          },
          {
            title: '地址',
            key: 'address'
          }
        ]
      }
    },
    created: function () {
      this.loadTableData()
    },
    mounted: function () {
      this.$on('refresh', function () {
        this.loadTableData()
      })
    },
    computed: {
      tableHeight () {
        return this.innerHeight - 237
      },
      tableData: function () {
        console.log(this.data)
        return this.data || []
      }
    },
    methods: {
      loadTableData () {
        let params = {}
        api.admin.list(params).then(response => {
          this.data = response.data.data
        })
      }
    }
  }
</script>
<style scoped>

</style>
