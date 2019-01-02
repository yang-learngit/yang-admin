<!--suppress ALL -->
<template>
  <Form ref="loginForm" :model="form" :rules="rules" @keydown.enter.native="handleSubmit">
    <FormItem prop="userName">
      <i-input v-model="form.userName" placeholder="请输入用户名3">
        <span slot="prepend">
          <Icon :size="16" type="ios-person"></Icon>
        </span>
      </i-input>
    </FormItem>
    <FormItem prop="password">
      <i-input type="password" v-model="form.password" placeholder="请输入密码434">
        <span slot="prepend">
          <Icon :size="14" type="md-lock"></Icon>
        </span>
      </i-input>
    </FormItem>
    <FormItem prop="kaptcha" class="kaptcha">
      <i-input v-model="form.kaptcha" placeholder="请输入验证码334">
        <span slot="prepend">
          <Icon :size="4" type="ios-pricetag"></Icon>
        </span>
        <span slot="append">
          <img :src="kaptchaUrl" @click="refreshKaptcha"/>
        </span>
      </i-input>
    </FormItem>
    <FormItem>
      <my-slider v-model="sliderSucess"></my-slider>
    </FormItem>
    <FormItem>
      <Button @click="handleSubmit" type="primary" long :disabled="!sliderSucess">登录4</Button>
    </FormItem>
  </Form>
</template>
<script>
import UUID from '@/libs/UUID'
import MySlider from '@/view/components/yzjiang/my-slider'

export default {
  name: 'LoginForm',
  components: {MySlider},
  props: {
    userNameRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ]
      }
    },
    passwordRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    },
    kaptchaRules: {
      type: Array,
      default: () => {
        return [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  data () {
    return {
      sliderSucess: false,
      form: {
        userName: 'super_admin',
        password: '',
        kaptcha: ''
      }
    }
  },
  computed: {
    rules () {
      return {
        userName: this.userNameRules,
        password: this.passwordRules,
        kaptcha: this.kaptchaRules
      }
    },
    kaptchaUrl () {
      return `/api/kaptcha?uuid=${this.form.uuid}`
    }
  },
  created () {
    this.refreshKaptcha()
  },
  methods: {
    refreshKaptcha () {
      this.form.uuid = new UUID().id
    },
    handleSubmit () {
      console.log('3333333333')
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          console.log('44444444444')
          // const params = new FormData()
          // params.append('username', this.form.account)
          // params.append('password', this.form.password)
          // params.append('kaptcha', this.form.kaptcha)
          // ajax.post('/login', params).then(response => {
          //   const result = response.data
          //   if (result.code === '200') {
          //     const user = result.data
          //     localStorage.setItem('user', JSON.stringify(user))
          //     this.setUser(user)
          //     this.$router.push({
          //       name: 'HomeIndex'
          //     })
          //   } else {
          //     if (result.code === '40001') {
          //       // 刷新验证码
          //       this.refreshKaptcha()
          //     }
          //   }
          // })
          this.$emit('on-success-valid', {
            userName: this.form.userName,
            password: this.form.password,
            kaptcha: this.form.kaptcha
          })
        }
      })
    }
  }
}
</script>
<style>
  .kaptcha /deep/ .ivu-input-group-append {
    padding: 0;
    border: 0;
    height: 30px;
  }

  .kaptcha img {
    height: 100%;
    cursor: pointer;
  }
</style>
