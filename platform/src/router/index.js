import Vue from 'vue'
import VueRouter from 'vue-router'

//异步加载
const login = () => import('../views/login.vue')
const register = () => import('../views/register.vue')
const recovery = () => import('../views/recovery.vue')
const Home = () => import('@/views/Home.vue')

const About = () => import('@/views/About.vue')

const infoRefine = () => import('@/views/infoRefine.vue')

const index = () => import('../components/base/base.vue')
// const nav = () => import('../components/base/Nav.vue')
const Cardies = () => import('../components/Cardies.vue')

const subcategory = () => import('../components/bigclass/subcategory.vue')
const searchcourse = () => import('@/components/course/searchcourse.vue')

const Courses = () => import('@/components/course/Courses.vue')
const study = () => import('@/components/learn/study.vue')
const videopage = () => import('@/components/learn/videopage.vue')
const profile = () => import('@/components/user/profile.vue')
const edit = () => import('@/components/user/edit.vue')
const myclass = () => import('@/components/user/myclass.vue')
Vue.use(VueRouter)

const routes = [

  {
    path: '/register',
    name: 'register',
    component: register
  },
  {
    path: '/recovery',
    name: 'recovery',
    component: recovery
  },

  {
    path: '/Cardies',
    name: 'Cardies',
    component: Cardies,
  },

  {
    path: '/index',
    name: 'index',
    component: index,
    // base页面并不需要被访问
    redirect: '/Home',
    children: [

      {
        path: '/Home',

        component: Home,
        meta: {
          requireAuth: true
        }
      },
    
      {
        path: '/searchcourse?coursename',
        name: 'searchcourse',
        component: searchcourse,
      },
      {
        path: '/searchcourse',
        name: 'searchcourse',
        component: searchcourse,
      },
     
      {
        path: '/profile',
        name: 'profile',
        component: profile,
      },
      {
        path: '/myclass',
        name: 'myclass',
        component: myclass,
      },
      {
        path: '/edit',
        name: 'edit',
        component: edit,
      },
      {
        path: ':subcategory',
        name: 'subcategory',
        component: subcategory,
      },

      {
        path: ':subcategory/:course/study',
        component: study,
      },
      {
        path: ':subcategory/:course?chiildcategoryID',
        component: subcategory,
      },
      {
        path: ':subcategory/:course',
        component: Courses,
      },


    ]
  },
  {
    path: '/About',
    name: 'About',
    component: About
  },

  {
    path: '/',
    redirect: "/login"
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },

  {
    path: '/register',
    name: 'register',
    component: register
  },
  {
    path: '/infoRefine',
    name: 'infoRefine',
    component: infoRefine
  },
  {
    path: '/videopage',
    name: ' videopage',
    component:  videopage
  },


]

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  //path的展示形式
  mode: 'history',
  //前端的项目名称
  base: '/',
  routes
})

export default router
