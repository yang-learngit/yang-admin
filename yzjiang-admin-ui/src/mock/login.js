import { getParams } from '@/libs/util'
import axios from '@/libs/api.request'

const USER_MAP = {
  super_admin: {
    name: 'super_admin',
    user_id: '1',
    access: ['super_admin', 'admin'],
    token: 'super_admin',
    avator: 'https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png'
  },
  admin: {
    name: 'admin',
    user_id: '2',
    access: ['admin'],
    token: 'admin',
    avator: 'https://avatars0.githubusercontent.com/u/20942571?s=460&v=4'
  }
}

export const login = req => {
  req = JSON.parse(req.body)
  console.log(req)
  console.log('666666666')
  const params = new FormData()
  params.append('username', req.username)
  params.append('password', req.password)
  // params.append('kaptcha', this.form.kaptcha)
  axios.post('/login', params).then(response => {
    const result = response.data
    if (result.code === '200') {
      console.log('login true')
    } else {
      console.log('login faile=' + result)
    }
  })
  return {token: USER_MAP[req.userName].token}
}

export const getUserInfo = req => {
  const params = getParams(req.url)
  return USER_MAP[params.token]
}

export const logout = req => {
  return null
}
