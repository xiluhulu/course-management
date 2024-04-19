<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChage">
      <i v-if="!collapse" class="el-icon-s-fold"></i>
      <i v-else class="el-icon-s-unfold"></i>
    </div>
    <div class="logo">课堂管理系统
      <span v-if="role==1">（教师端）</span>
      <span v-if="role==2">（学生端）</span>
      <span v-if="role==0">（管理员端）</span>
    </div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 全屏显示 -->
        <div class="btn-fullscreen" @click="handleFullScreen">
          <el-tooltip
            effect="dark"
            :content="fullscreen ? `取消全屏` : `全屏`"
            placement="bottom"
          >
            <i class="el-icon-rank"></i>
          </el-tooltip>
        </div>
        <!-- 消息中心 -->
        <!-- <div class="btn-bell">
          <el-tooltip
            effect="dark"
            :content="message ? `有${message}条未读消息` : `消息中心`"
            placement="bottom"
          >
            <router-link to="/tabs">
              <i class="el-icon-bell"></i>
            </router-link>
          </el-tooltip>
          <span class="btn-bell-badge" v-if="message"></span>
        </div> -->
        <!-- 用户头像 -->
        <div class="user-avator">
          <!-- <img src="../../assets/img/img.jpg" /> -->
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{ $store.state.user.username }}
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <a @click="updateDialogVisible=true">
              <el-dropdown-item>修改密码</el-dropdown-item>
            </a>
            <el-dropdown-item divided command="loginout"
              >退出登录</el-dropdown-item
            >
          </el-dropdown-menu>
          <!-- 修改弹窗 -->
          <el-dialog
            title="修改密码"
            :visible.sync="updateDialogVisible"
            width="1000px"
            center
          >
            <el-form
              ref="changePasswordFrom"
              :model="changePasswordFrom"
              label-width="80px"
            >
              <el-form-item label="旧密码 :">
                <el-input v-model="changePasswordFrom.oldPassword"></el-input>
              </el-form-item>
              <el-form-item label="新密码 :">
                <el-input v-model="changePasswordFrom.newPassword"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="updateDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="changePassword()">确 定</el-button>
            </span>
          </el-dialog>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import bus from "../common/bus";
export default {
  data() {
    return {
      collapse: false,
      fullscreen: false,
      name: "linxin",
      message: 2,
      updateDialogVisible: false,
      changePasswordFrom: {
        oldPassword: "",
        newPassword: "",
      },
      changePasswordFrom1: {
        oldPassword: "",
        newPassword: "",
      },
      role:3
    };
  },
  computed: {
    username() {
      let username = localStorage.getItem("ms_username");
      return username ? username : this.name;
    },
  },
 
  methods: {
    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command == "loginout") {
        window.sessionStorage.removeItem("token");
        this.$router.push("/login");
      }
    },
    // 侧边栏折叠
    collapseChage() {
      this.collapse = !this.collapse;
      this.$store.state.collapse=this.collapse

      
      // bus.$emit("collapse", this.collapse);
    },
    // 全屏事件
    handleFullScreen() {
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
    // 修改密码窗口
    async changePassword() {
        if(this.changePasswordFrom.oldPassword!="" && this.changePasswordFrom.newPassword!=""){
            
            this.$axios.post("user/changePassword",this.changePasswordFrom).then(res=>{
                console.log(res);
                if(res.data.code==654){
                    this.$message.error(res.data.msg);
                }
                if(res.data.code==200){
                    this.$message.success(res.data.msg);
                    this.updateDialogVisible=false;
                    this.changePasswordFrom=this.changePasswordFrom1;
                }
            })
        }else{
            this.$message.error("不能为空")
        }
      
    },
    //  获取用户信息
    async getUserInfo(){
      await this.$axios.get("user/getUserInfo").then(res=>{
        console.log("用户信息》》》",res)
        if(res.data.code==200){
          this.$store.state.user=res.data.data
        }
      })
    }
  },
  mounted() {
    if (document.body.clientWidth < 1500) {
      this.collapseChage();
    }
    this.role=window.sessionStorage.getItem("role")
    this.getUserInfo();
    console.log(this.role);
  },
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}
.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}
.header .logo {
  float: left;
  width: 350px;
  line-height: 70px;
}
.header-right {
  float: right;
  padding-right: 50px;
}
.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}
.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}
.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}
.btn-bell .el-icon-bell {
  color: #fff;
}
.user-name {
  margin-left: 10px;
}
.user-avator {
  margin-left: 20px;
}
.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.el-dropdown-link {
  color: #fff;
  cursor: pointer;
  font-size: 20px;
}
.el-dropdown-menu__item {
  text-align: center;
}
</style>
