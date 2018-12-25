const noticeMenuRouter = [{
  path: 'notice-list',
  name: 'notice-list',
  meta: {
    title: '公告管理',
    buttons: ['back']
  },
  component: (resolve) => require(['@/view/yzjiang/member-system/notice/list.vue'], resolve)
}]

export { noticeMenuRouter }
