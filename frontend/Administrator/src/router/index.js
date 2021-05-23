import Vue from 'vue'
import Router from 'vue-router'
import BanUser from '../views/BanUser'
import AuditProduct from '../views/AuditProduct'
import Index from '../views/Index'
import AmdinManager from '../views/AdminManager'
import AdminLogin from '../views/AdminLogin'
import App from '../App'
import store from 'element-ui/packages/cascader-panel/src/store'
import AppealManager from '../views/AppealManager'
import ReportManager from '../views/ReportManager'
import NoticeManager from '../views/NoticeManager'
import Refresh from '../views/Refresh'
Vue.use(Router)

const router= new Router({
  routes: [
    {
      path: '/',
      name: '登录',
      component: AdminLogin,
      meta: { title: "Administrator" }
    },
    {
      path: '../App',
      name: '管理界面',
      component: App,
      redirect:'/BanUser',
      meta: { title: "Administrator" },
      children:[
        {
          path: '/Index',
          name: '用户管理',
          component: Index,
          children: [
            {
              path: '/BanUser',
              name: '封禁用户',
              component: BanUser
            },
          ]
        },
        {
          path: '/Index',
          name: '商品管理',
          component: Index,
          children: [


            {
              path: '/AuditProduct',
              name: '审核商品',
              component: AuditProduct
            }
          ]
        },
        {

          path: '/Index',
          name: '公告管理',
          component: Index,
          children:[
            {
              path:'/NoticeManager',
              name:'公告管理',
              component: NoticeManager,
            },

          ]


        },
        {
          path: '/Index',
          name: '违规管理',
          component: Index,
          children: [
            {
              path:'/AppealManager',
              name:'申诉处理',
              component: AppealManager
            },
            {
              path:'/ReportManager',
              name:'举报处理',
              component: ReportManager
            }
          ]
        },
        {
          path: '/Index',
          name: '管理员管理',
          component: Index,
          children: [
            {
              path: '/AdminManager',
              name: '编辑管理员',
              component: AmdinManager
            }

          ]
        }
      ]
    },
    {
      path: '/refresh',
      component: Refresh,
    },

  ]
});

//访问router对应路径前验证是否登录
router.beforeEach((to, from, next) => {

  //如果前往登录页面则设置状态为未登录
  if (to.path === '/' || to.path === '/AdminLogin')
  {

    sessionStorage.setItem('isLogin','null');
    next();
  }
  else
    {
        const loginToken =sessionStorage.getItem('isLogin');
        const authToken=localStorage.getItem('token')
        //未登录，token无效都跳转到登录界面
        if(loginToken === 'null' || loginToken === null || loginToken === undefined || authToken === null || authToken === '')
        {
          next({path:'/'});
        }
        else
        {
          next();
        }
    }
});

export default router;
