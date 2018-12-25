const financeMenuRouter = [{
  path: 'finance-list',
  name: 'finance-list',
  meta: {
    title: '财务管理',
    buttons: ['back']
  },
  component: (resolve) => require(['@/view/yzjiang/member-system/finance/list.vue'], resolve)
}]

export { financeMenuRouter }
