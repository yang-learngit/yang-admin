const informationMenuRouter = [{
  path: 'information-list',
  name: 'information-list',
  meta: {
    title: '会员资料',
    buttons: ['back']
  },
  component: (resolve) => require(['@/view/yzjiang/member-system/information/list.vue'], resolve)
}]

export { informationMenuRouter }
