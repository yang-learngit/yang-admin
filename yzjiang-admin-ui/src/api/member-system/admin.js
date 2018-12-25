import util from '@/libs/util'

const post = util.ajax.post
const admin = {}

admin.list = function (params) {
  return post('/member/admin/list', params)
}

const adminUrls = {}

export { adminUrls, admin }
