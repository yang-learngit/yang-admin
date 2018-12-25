import util from '@/libs/util'

const post = util.ajax.post
const notice = {}

notice.list = function (params) {
  return post('/member/notice/list', params)
}

notice.add = function (params) {
  return post('/member/notice/add', params)
}

notice.find = function (params) {
  return post('/member/notice/find', params)
}

notice.delete = function (params) {
  return post('/member/notice/delete', params)
}

notice.loadNew = function (params) {
  return post('/member/notice/load-new', params)
}

const noticeUrls = {}

export { noticeUrls, notice }
