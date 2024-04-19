<template>
  <div id="layout">
    <el-container class="layout-container">
      <!-- <el-aside > -->
      <el-menu
        :default-active="$route.fullPath"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        :collapse="$store.state.collapse"
        router
        background-color="#06283D"
        text-color="#fff"
        active-text-color="#FFD124"
      >
        <el-menu-item index="/index">
              <i class="iconfont icon-shouye"></i>
              <span slot="title">首页</span>
            </el-menu-item>
            
        <el-menu-item v-if="role == 2" index="/course_stu">
          <i class="iconfont icon-zhaokecheng"></i>
          <span slot="title">查看课程</span>
        </el-menu-item>
        <el-menu-item v-if="role == 2" index="/stu_add_course">
          <i class="iconfont icon-jiaru"></i>
          <span slot="title">加入课程</span>
        </el-menu-item>
        <el-menu-item v-if="role == 1" index="/course_tea">
          <i class="iconfont icon-kecheng"></i>
          <span slot="title">课程管理</span>
        </el-menu-item>
        <el-menu-item v-if="role == 1" index="/class_tea">
          <i class="iconfont icon-banjiguanli"></i>
          <span slot="title">班级管理</span>
        </el-menu-item>
        <el-menu-item v-if="role == 1" index="/tea_approva_page">
          <i class="iconfont icon-shenpi"></i>
          <span slot="title">加分审批</span>
        </el-menu-item>
        <el-menu-item v-if="role == 0" index="/course">
          <i class="iconfont icon-zhaokecheng"></i>
          <span slot="title">课程管理</span>
        </el-menu-item>
        <el-menu-item v-if="role == 0" index="/class">
          <i class="iconfont icon-banjiguanli1"></i>
          <span slot="title">班级管理</span>
        </el-menu-item>
        <el-menu-item v-if="role == 0" index="/personnel_stu">
          <i class="iconfont icon-zaijixueshengguanli"></i>
          <span slot="title">学生管理</span>
        </el-menu-item>
        <el-menu-item v-if="role == 0" index="/personnel_tea">
          <i class="iconfont icon-jiaoshiguanli"></i>
          <span slot="title">教师管理</span>
        </el-menu-item>
      </el-menu>

     
      <el-container>
        <el-header><v-head></v-head></el-header>

        <v-tags></v-tags>
        <el-main min-width="2400px">
          <transition name="move" mode="out-in">
            <keep-alive :include="tagsList">
              <router-view></router-view>
            </keep-alive>
          </transition>
          <!-- <router-view></router-view> -->
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import vTags from "../components/common/Tags.vue";
// import bus from "../components/common/bus";
import vHead from "../components/common/Header.vue";

export default {
  components: {
    vTags,

    vHead,
  },
  data() {
    return {
      role: 9,
      ac: "/index",
      menulist: [],
      // menulist9: [
      //   // {
      //     // id: 2,
      //     // name: "课程管理",
      //     // children: [
      //       // {
      //       //   id: 20,
      //       //   name: "查看所有课程（stu-卡片）",
      //       //   path: "/course_stu",
      //       // },
      //       // {
      //       //   id: 21,
      //       //   name: "查看管理的课程（tea-表格）",
      //       //   path: "course_tea",
      //       // },
      //       // {
      //       //   id: 22,
      //       //   name: "查看所有课程（admin-表格）",
      //       //   path: "/course",
      //       // },
      //       // {
      //       //   id: 23,
      //       //   name: "加分审批（tea）",
      //       //   path: "/tea_approva_page",
      //       // },
      //     // ],
      //   // },
      //   // {
      //   //   id: 3,
      //   //   name: "班级管理",
      //   //   children: [
      //       // {
      //       //   id: 30,
      //       //   name: "查看班级(admin-表格)",
      //       //   path: "/class",
      //       // },
      //       // {
      //       //   id: 31,
      //       //   name: "查看管理的班级（tea-表格）",
      //       //   path: "/class_tea",
      //       // },
      //   //   ],
      //   // },
      //   {
      //     id: 5,
      //     name: "人员管理",
      //     children: [
      //       // {
      //       //   id: 50,
      //       //   name: "查看所有人员",
      //       //   path: "/personnel",
      //       // },
      //       {
      //         id: 51,
      //         name: "学生管理（admin-表格）",
      //         path: "/personnel_stu",
      //       },
      //       {
      //         id: 52,
      //         name: "教师管理(admin-表格)",
      //         path: "/personnel_tea",
      //       },
      //     ],
      //   },
      // ],
      // menulist_admin: [
      //   {
      //     id: 2,
      //     name: "课程管理",
      //     children: [
      //       {
      //         id: 22,
      //         name: "查看所有课程（admin-表格）",
      //         path: "/course",
      //       },
      //     ],
      //   },
      //   {
      //     id: 3,
      //     name: "班级管理",
      //     children: [
      //       {
      //         id: 30,
      //         name: "查看班级(admin-表格)",
      //         path: "/class",
      //       }
      //     ],
      //   },
      //   {
      //     id: 5,
      //     name: "人员管理",
      //     children: [
      //       {
      //         id: 50,
      //         name: "查看所有人员",
      //         path: "/personnel",
      //       },
      //       {
      //         id: 51,
      //         name: "查看所有学生（admin-表格）",
      //         path: "/personnel_stu",
      //       },
      //       {
      //         id: 52,
      //         name: "查看所有教师(admin-表格)",
      //         path: "/personnel_tea",
      //       },
      //     ],
      //   },
      // ],
      // menulist_tea: [
      //   {
      //     id: 2,
      //     name: "课程管理",
      //     children: [
      //       {
      //         id: 21,
      //         name: "查看管理的课程（tea-表格）",
      //         path: "course_tea",
      //       },
      //     ],
      //   },
      //   {
      //     id: 3,
      //     name: "班级管理",
      //     children: [
      //       {
      //         id: 31,
      //         name: "查看管理的班级（tea-表格）",
      //         path: "/class_tea",
      //       },
      //     ],
      //   },
      // ],
      // menulist_stu: [
      //   {
      //     id: 2,
      //     name: "课程管理",
      //     children: [
      //       {
      //         id: 20,
      //         name: "查看课程",
      //         path: "/course_stu",
      //       },
      //     ],
      //   }
      // ],

      tagsList: [],
    };
  },
  mounted() {
    let role = window.sessionStorage.getItem("role");
    this.role = role;
    // this.menulist=this.menulist9
    //用于判断哪些角色能看见那些选项-----------------------------------------------
    if (role == 0) {
      this.menulist = this.menulist9;
    }
    // if ((role == 1)) {
    //   this.menulist=this.menulist_tea;
    // }
    console.log("+++++", this.$route.fullPath);
  },
  // created() {
  //   bus.$on("collapse-content", (msg) => {
  //     this.collapse = msg;
  //   });

  //   // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
  //   bus.$on("tags", (msg) => {
  //     let arr = [];
  //     for (let i = 0, len = msg.length; i < len; i++) {
  //       msg[i].name && arr.push(msg[i].name);
  //     }
  //     this.tagsList = arr;
  //   });
  // },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style scoped>

#layout {
  height: 100%;
}
.layout-container {
  height: 100%;
}
.el-header {
  background-color: #1868b9;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
}
.el-aside {
  background-color: #333744;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 250px;
  min-height: 100%;
  /*color: rgb(255, 255, 255);*/
  /*background-color: #aec0cb;*/
}

.el-menu {
  /*width: 100%;*/
  border-right: none;
}
.el-main {
  background-color: #eaedf1;
  display: flex;
}
.el-menu-item {
  width: 100%;
}
.el-menu-item:hover{
    background-color: #8b8f9259 !important;
  }
.iconfont {
  font-size: 20px;
  font-weight: 800;
  margin-right: 10px;
  /*color: red;*/
}
.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
.box-card {
  /*background-color: rgb(230, 199, 199);*/
  flex: 1;
  min-width: 1420px;
  overflow: auto;
}
.move-enter-active,
.move-leave-active {
  transition: opacity 0.5s;
}

.move-enter,
.move-leave {
  opacity: 0;
}
@font-face {
  font-family: "iconfont"; /* Project id 3786553 */
  src: url("//at.alicdn.com/t/c/font_3786553_yxoovuze1h.woff2?t=1669110701012")
      format("woff2"),
    url("//at.alicdn.com/t/c/font_3786553_yxoovuze1h.woff?t=1669110701012")
      format("woff"),
    url("//at.alicdn.com/t/c/font_3786553_yxoovuze1h.ttf?t=1669110701012")
      format("truetype");
}
</style>
