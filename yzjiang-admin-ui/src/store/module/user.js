import api from '@/api'
import util from '@/libs/util'

export default {
  state: {
    userName: '',
    userId: '',
    avatorImgPath: '',
    token: util.getToken(),
    access: '',
    hasGetInfo: false
  },
  mutations: {
    setAvator (state, avatorPath) {
      state.avatorImgPath = avatorPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUserName (state, name) {
      state.userName = name
    },
    setAccess (state, access) {
      state.access = access
    },
    setToken (state, token) {
      state.token = token
      util.setToken(token)
    },
    setHasGetInfo (state, status) {
      state.hasGetInfo = status
    }
  },
  actions: {
    // 登录
    handleLogin ({ commit }, {userName, password, kaptcha}) {
      userName = userName.trim()
      return new Promise((resolve, reject) => {
        api.user.login({
          userName,
          password,
          kaptcha
        }).then(res => {
          const data = res.data
          if (data.code === '200') {
            commit('setToken', userName)
            resolve()
          } else {
            reject(data.message)
          }
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 退出登录
    handleLogOut ({ state, commit }) {
      return new Promise((resolve, reject) => {
        // api.user.logout(state.token).then(() => {
        //   commit('setToken', '')
        //   commit('setAccess', [])
        //   resolve()
        // }).catch(err => {
        //   reject(err)
        // })
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        commit('setToken', '')
        commit('setAccess', [])
        resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          api.user.getUserInfo(state.token).then(res => {
            const data = res.data
            commit('setAvator', data.avator)
            commit('setUserName', data.user_name)
            commit('setUserId', data.user_id)
            commit('setAccess', data.access)
            commit('setHasGetInfo', true)
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    // 获取用户相关信息
    loadSessions (store) {
      let commit = store.commit
      let state = store.state
      return new Promise((resolve, reject) => {
        if (state.sessionData) {
          resolve(state.sessionData)
        } else {
          api.user.loadSessions().then(res => {
            // 修改标题
            // 修改shortcut icon
            const data = res.data
            util.title(window.document.title, sign[data.sign].title)
            commit('setAvator', data.imgUrl)
            commit('setUserName', data.name)
            commit('setUserId', data.account)
            commit('setSessionData', data)
            resolve(data)
          }).catch(err => {
            setTimeout(() => {
              console.warn('Session数据请求失败，重新请求...')
              store.dispatch('loadSessions')
            }, 1000)
            reject(err)
          })
        }
      })
    },
    loadPermissions (store) {
      let commit = store.commit
      let state = store.state
      return new Promise((resolve, reject) => {
        if (state.access && state.access.length > 0) {
          resolve(state.access)
        } else {
          api.user.loadPermissions().then(res => {
            const data = res.data
            commit('setAccess', data)
            resolve(data)
          }).catch(err => {
            setTimeout(() => {
              console.warn('权限数据请求失败，重新请求...')
              store.dispatch('loadPermissions')
            }, 1000)
            reject(err)
          })
        }
      })
    },
    index ({ state, commit }) {
      return new Promise((resolve, reject) => {
        api.user.index().then(res => {
          const data = res.data
          resolve(data)
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}
