import axios from 'axios'

const baseURL = '/api'

const upload = axios.create({
  baseURL: baseURL,
  timeout: 900000,
  withCredentials: true
})

const ajax = axios.create({
  baseURL: baseURL,
  // 斑马数据测试环境查询需要15秒左右时间，所以暂时先配置成20秒
  timeout: 20000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'X-Requested-With': 'XMLHttpRequest'
  }
})

const staticDownload = function (url) {
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = url
  document.body.appendChild(link)
  link.click()
}

const download = function (url, param, method, callback) {
  let flag = true
  return axios({
    method: method || 'post',
    baseURL: baseURL,
    url: url,
    data: param,
    // 导出时间避免过长，修改为60s
    timeout: 60000,
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    responseType: 'blob'
  }).then(response => {
    flag = parseResp(response)
    if (callback) {
      let msg = '下载开始'
      if (!flag) {
        msg = '下载异常，请重新登录后再试'
      }
      callback(flag, msg)
    }
  }).catch(e => {
    if (callback) {
      flag = false
      let error = e.message
      if (error.search('timeout') !== -1) {
        callback(flag, '下载超时，请联系管理员')
      } else if (error.search('Network') !== -1) {
        callback(flag, '网络异常，请重新登录后再试')
      } else if (error.search('500') !== -1) {
        callback(flag, '服务器接口异常，请联系管理员')
      } else {
        callback(flag, '下载异常:' + e.message + '，请联系管理员')
      }
    }
  })
}

// 解析返回下载的response
function parseResp (response) {
  if (!response.data || response.data.type === 'text/html') {
    return false
  }
  const contentDisposition = response.headers['content-disposition']
  const filename = decodeURI(contentDisposition.match(/filename="(.+)"/i)[1])
  const url = window.URL.createObjectURL(new Blob([response.data]))
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = url
  link.setAttribute('download', filename)
  document.body.appendChild(link)
  link.click()
  return true
}

export {baseURL, ajax, upload, download, staticDownload}
