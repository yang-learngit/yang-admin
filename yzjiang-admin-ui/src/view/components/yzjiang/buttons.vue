<template>
  <Dropdown
    @on-click="operate"
    trigger="click"
    placement="right"
    :transfer="true"
    >
    <Button
      type="text"
      size="small"
      style="color: #2d8cf0">
      <Icon type="compose" size="18"></Icon>
    </Button>
    <DropdownMenu slot="list">
      <DropdownItem
        v-for="menu in menus"
        :name="menu.key"
        :key="menu.key"
        :disabled="menu.disabled">
        {{menu.title}}
      </DropdownItem>
    </DropdownMenu>
  </Dropdown>
</template>

<script>

  export default {
    name: 'Buttons',
    props: {
      row: Object,
      menus: {
        type: Array,
        default: function () {
          return []
        }
      }
    },
    computed: {
    },
    methods: {
      isDisabled (name) {
        return this.menus.some(menu => {
          return menu.key === name && menu.disabled
        })
      },
      operate (name) {
        if (!this.isDisabled(name)) {
          let row = Object.assign({}, this.row)
          this.$emit(name, row)
        }
      }
    }
  }
</script>
