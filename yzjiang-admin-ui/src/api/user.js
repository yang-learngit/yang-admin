import util from '@/libs/util'

const post = util.ajax.post
const get = util.ajax.get
const user = {}
user.login = ({ userName, password, kaptcha }) => {
  let params = new FormData()
  params.append('userName', userName)
  params.append('password', password)
  params.append('kaptcha', kaptcha)
  return post('/login', params)
}

user.loadSessions = () => {
  return get('/common/sessions.json')
}

user.loadPermissions = () => {
  return get('/common/user-permissions.json')
}

user.index = () => {
  return post('/index.json')
}

// user.logout = () => {
//   return post('/logout.json')
// }
user.logout = (token) => {
  return post('/logout')
}
user.modifyPassword = params => {
  return post('/modify-password', params)
}

const userUrls = {}

export { userUrls, user }

// import axios from '@/libs/api.request'

// export const login = ({ userName, password }) => {
//   const data = {
//     userName,
//     password
//   }
//   console.log('55555555')
//   return axios.request({
//     url: 'login',
//     data,
//     method: 'post'
//   })
// }
//
// export const getUserInfo = (token) => {
//   return axios.request({
//     url: 'get_info',
//     params: {
//       token
//     },
//     method: 'get'
//   })
// }
//
// export const logout = (token) => {
//   return axios.request({
//     url: 'logout',
//     method: 'post'
//   })
// }
