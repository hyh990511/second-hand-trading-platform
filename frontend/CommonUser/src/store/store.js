import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //未读信息数
    UnReadMessageNum:sessionStorage.getItem('UnReadMessageNum'),
    //用户头像
    UserImages:sessionStorage.getItem('Images'),
  },
  mutations: {
    //未读数减1
    decrease(){
      this.state.UnReadMessageNum--
      sessionStorage.setItem('UnReadMessageNum',this.state.UnReadMessageNum)
      // console.log('num now:'+this.state.UnReadMessageNum)
    }
  },
  actions: {
  },
  modules: {
  }
})
