import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
//const options = {}
//export default new Vuex.Store(options)

export default new Vuex.Store({
  state:{
    token:''
  },
  mutations:{
      setToken(newToken){
        this.state.token=newToken
        localStorage.setItem('token',newToken)
      },
      clearToken(){
        localStorage.removeItem('token')
        this.state.token=''
      }
  }
})

