import util from '@/libs/util'

const post = util.ajax.post
const finance = {}

finance.list = function (params) {
  return post('/member/finance/list', params)
}

const financeUrls = {}

export { financeUrls, finance }
