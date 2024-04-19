<template>
  <el-card class="box-card" ref="boxCard">
    <!-- 欢迎来到课堂管理系统 -->
    <div class="mc">
      <el-card class="card">
        <div slot="header" class="clearfix">
          <h1>个人信息</h1>
          <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
        </div>
        <div class="text item">
          <h3>姓名：{{ $store.state.user.username }}</h3>
          <h3 v-if="role == 2">学号：{{ $store.state.user.stuJobNum }}</h3>
          <h3 v-if="role == 1">工号：{{ $store.state.user.stuJobNum }}</h3>
          <h3>联系方式：{{ $store.state.user.num }}</h3>
          <span v-if="role == 1" class="c_red">*如需修改请联系管理员</span>
          <span v-if="role == 2" class="c_red">*如需修改请联系管理员或班主任</span>
        </div>
      </el-card>
      <div class="card_div"></div>
      <!--<el-card class="card2">
        <div slot="header" class="clearfix">
          <h1>个人信息</h1>
          <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> 
        </div>
        <div class="text item">
          <h3>姓名：{{ $store.state.user.username }}</h3>
          <h3 v-if="role == 2">学号：{{ $store.state.user.stuJobNum }}</h3>
          <h3 v-if="role == 1">工号：{{ $store.state.user.stuJobNum }}</h3>
          <h3>联系方式：{{ $store.state.user.num }}</h3>
        </div>
      </el-card>-->
    </div>
  </el-card>

  <!-- <el-upload
      class="avatar-uploader"
      action="https://www.imgurl.org/api/v2/upload"
      :data="pdata"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload> -->
</template>

<script>
export default {
  data() {
    return {
      role: window.sessionStorage.getItem("role"),
      // imageUrl: '',
      // pdata:{
      //   uid:"114bccdccf65f6ff67604e0ada9347ac",
      //   token:"b140392458a32c78a939cd79a292d587"
      // }
    };
  },
  methods: {
    // handleAvatarSuccess(res, file) {
    //   console.log(res);
    //   this.imageUrl = URL.createObjectURL(file.raw);
    // },
    // beforeAvatarUpload(file) {
    //   const isJPG = file.type === 'image/jpeg';
    //   const isLt5M = file.size / 1024 / 1024 < 5;
    //   if (!isLt5M) {
    //     this.$message.error('上传头像图片大小不能超过 5MB!');
    //   }
    //   return isLt5M;
    // }
  },
};
</script>

<style scoped>
/*.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}*/
.mc {
  display: flex;
}
.text {
  font-size: 14px;
}
.c_red {
  color: red;
}
.item h3 {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.card {
  flex: 30;
  width: 480px;
}
.card_div {
  flex: 50;
}
.card2 {
  flex: 60;
  width: 480px;
}
</style>