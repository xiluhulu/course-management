import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:"/index",
    children:[
      {
        path:'index',
        component: () => import('../views/Index.vue'),
        meta:{title:'首页'}
      },
      // {
      //   path:'personnel',
      //   component: () => import('../views/Personnel.vue'),
      //   meta:{title:'查看所有人员'}
      // },
      {
        path:'personnel_stu',
        component: () => import('../views/PersonnelStu.vue'),
        meta:{title:'学生管理'}
      },
      {
        path:'personnel_tea',
        component: () => import('../views/PersonnelTea.vue'),
        meta:{title:'教师管理'}
      },
      {
        path:'class',
        component: () => import('../views/Class.vue'),
        meta:{title:'班级管理'}
      },
      {
        path:'course_stu',
        component: () => import('../views/CourseStu.vue'),
        meta:{title:'学生管理'}
      },
      {
        path:'course',
        component: () => import('../views/Course.vue'),
        meta:{title:'课程管理'},
      },
      {
        path:'class_tea',
        component: () => import('../views/ClassTea.vue'),
        meta:{title:'班级管理'},
      },
      {
        path:"course_tea",
        component: () => import('../views/CourseTea.vue'),
        meta:{title:'课程管理'}
      },
      {
        path:"class_people",
        name:"class_people",
        component:()=> import('../views/tea/ClassPeople.vue'),
        meta:{title:'班级人员'}
      },
      {
        path:"tea_look_stu_score",
        name:"tea_look_stu_score",
        component:()=> import('../views/tea/StuScore.vue'),
        meta:{title:'分数详情'}
      },
      {
        path:"personnel_in_the_course",
        name:"personnel_in_the_course",
        component:()=> import('../views/tea/PersonnelInTheCourse.vue'),
        meta:{title:'课内情况'}
      },
      {
        path:"course_detail_pages",
        name:"course_detail_pages",
        component:()=> import('../views/stu/CourseDetailPages.vue'),
        meta:{title:'课程详情'}
      },
      {
        path:"tea_approva_page",
        name:"tea_approva_page",
        component:()=> import('../views/tea/TeaApprovaPage.vue'),
        meta:{title:'加分审批'}
      },
      {
        path:"stu_add_course",
        name:"stu_add_course",
        component:()=> import('../views/stu/AddCourse.vue'),
        meta:{title:'加分审批'}
      }
    ]
  },{
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue')
  }

  // {
  //   path: '/about',
  //   name: 'about',
  //   // component: () => import('../views/AboutView.vue')
  // }
]



const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// 挂载路由导航
router.beforeEach((to,from,next) =>{
	console.log("router---beforeEach")
	// to 将要访问的路径
	// from 代而表从哪个路径跳转来
	// next 是一个函数，表示放行
	//     next()  放行    next('/login')  强制跳转
	if(to.path==="/login") return next();
	const token=window.sessionStorage.getItem("token");
  
	// 判断token不为空的话就放行
	if(token) return next();
	next("/login")
});
export default router


// 挂载路由导航
router.beforeEach((to,from,next) =>{
	if(to.path==="/login") return next();
	const token=window.sessionStorage.getItem("token");
  // 判断token不为空的话就放行
	if(token) return next();
	next("/login")
});