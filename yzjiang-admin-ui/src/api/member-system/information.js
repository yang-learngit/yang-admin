import util from '@/libs/util'

const post = util.ajax.post
const information = {}

information.list = function (params) {
  return post('/member/information/list', params)
}

const informationUrls = {}

export { informationUrls, information }
