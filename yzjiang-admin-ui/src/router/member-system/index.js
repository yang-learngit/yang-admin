import Main from '@/view/main'

import { adminMenuRouter } from './admin'
import { informationMenuRouter } from './information'
import { financeMenuRouter } from './finance'
import { noticeMenuRouter } from './notice'

const memberSystemMenuRouter = {
  path: '/',
  name: 'memberSystem',
  meta: {
    icon: 'ios-people',
    title: '会员管理系统'
  },
  component: Main,
  children: [
    ...informationMenuRouter,
    ...adminMenuRouter,
    ...financeMenuRouter,
    ...noticeMenuRouter
  ]
}

const memberSystemOtherRouter = {
  path: '/',
  name: 'memberSystemOther',
  meta: {
    title: '会员管理系统'
  },
  component: Main,
  children: [
  ]
}

export { memberSystemMenuRouter, memberSystemOtherRouter }
