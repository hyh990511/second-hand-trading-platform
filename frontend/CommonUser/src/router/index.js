import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from  '../views/Login'
import UserInfo from "../views/UserInfo";
import GoodsDetails from "../views/GoodsDetails";
import Order from "../views/Order";
import ModifyInfo from "../views/ModifyInfo";
import ModifyPassword from "../views/ModifyPassword";
import IssueGoods from "../views/IssueGoods";
import CommonGoods from "../views/CommonGoods";
import Refresh from "../views/Refresh";
import Register from "../views/Register";
import ResetPassword from "../views/ResetPassword";
import ShowUserInfo from "../views/ShowUserInfo";
import ListIssues from "../views/ListIssues";
import GraduationGoods from "../views/GraduationGoods";
import BooksGoods from "../views/BooksGoods";
import ListMessages from "../views/ListMessages";
import HomeIndex from "../views/HomeIndex";
import LoginIndex from "../views/LoginIndex";

Vue.use(VueRouter)

  const routes = [
    {
      path: '/',
      redirect: 'home',
    },
    {
      path: '/l_index',
      component: LoginIndex,
      children: [
        {
          path: '/login',
          name: 'Login',
          component: Login,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/register',
          name: 'Register',
          component: Register,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/resetPass',
          name: 'ResetPassword',
          component: ResetPassword,
          meta: {
            keepAlive: false
          }
        },
      ]
    },
    {
      path: '/h_index',
      component: HomeIndex,
      children: [
        {
          path: '/home',
          name: 'Home',
          component: Home,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/u_info',
          name: 'UserInfo',
          component: UserInfo,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/m_info',
          name: 'ModifyInfo',
          component: ModifyInfo,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/m_pass',
          name: 'ModifyPassword',
          component: ModifyPassword,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/c_goods',
          name: 'CommonGoods',
          component: CommonGoods,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/g_goods',
          name: 'GraduationGoods',
          component: GraduationGoods,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/b_goods',
          name: 'BooksGoods',
          component: BooksGoods,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/goodsdetails',
          name: 'GoodsDetails',
          component: GoodsDetails,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/show_info',
          name: 'ShowUserInfo',
          component: ShowUserInfo,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/order',
          name: 'Order',
          component: Order,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/issueGoods',
          name: 'IssueGoods',
          component: IssueGoods,
          meta: {
            keepAlive: true
          }
        },
        {
          path:'/listIssues',
          name: 'ListIssues',
          component: ListIssues,
          meta: {
            keepAlive: true,
          }
        },
        {
          path:'/listMessages',
          name: 'ListMessages',
          component: ListMessages,
          meta: {
            keepAlive: true,
          }
        }
      ],
    },
    {
      path: '/refresh',
      component: Refresh,
      meta: {
        keepAlive: false,
      }
    },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

// 解决两次访问相同路由地址报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
