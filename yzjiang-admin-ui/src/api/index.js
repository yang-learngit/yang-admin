import util from '@/libs/util'
import merge from 'deepmerge'

import { user } from './user'
import { information } from './member-system/information'
import { admin } from './member-system/admin'
import { finance } from './member-system/finance'
import { notice } from './member-system/notice'

// 简化api查询方法URL
const urls = []

function lowerToHyphenUpper (match) {
  return match.replace('-', '').toUpperCase()
}

let urlApis = {}
urls.forEach(url => {
  let result = url.split('/').reduceRight((accumulator, currentValue) => {
    if (currentValue === '') {
      return accumulator
    }
    let obj = {}
    obj[currentValue.replace(/-\w/g, lowerToHyphenUpper)] = accumulator
    return obj
  }, function (params) {
    return util.ajax.post({ url, params })
  })
  urlApis = merge(urlApis, result)
})

// 自定义查询方法
let apis = {
  user,
  admin,
  information,
  finance,
  notice
}

const api = merge(apis, urlApis)
export default api
