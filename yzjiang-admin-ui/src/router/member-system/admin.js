const adminMenuRouter = [{
  path: 'admin-list',
  name: 'admin-list',
  meta: {
    title: '会员管理',
    buttons: ['back']
  },
  component: (resolve) => require(['@/view/yzjiang/member-system/admin/list.vue'], resolve)
}]

export { adminMenuRouter }
