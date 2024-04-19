
<template>
  <el-card class="box-card" ref="boxCard">
    <div class="aaa">
      
      <el-card class="course_card" :body-style="{ padding: '0px' }" v-for="(item,index) in course" :key="index" >
        <img
          :src="item.imgUrl"
          class="image"
        />
        <div style="padding: 8px">
          <span>{{item.course}}</span>
          <div class="bottom clearfix">
            <span class="time">{{item.user.username}}</span>
            <span class="addClass" @click="addCourse(item)">加入课程</span>
          </div>
        </div>
      </el-card>
    
    </div>
  </el-card>
</template>


<script>
export default {
  data() {
    return {
      currentDate: new Date(),
      course:[]
    };
  },
  mounted(){
    this.getAllCourse();
  },
  methods:{
    // 学生获取对应的课程
    async getAllCourse() {
      await this.$axios.get("course/stu/getNotSelectedAndPublicCourse").then(res=>{
        this.course = res.data.data;
        // console.log(this.data);
        console.log(res)
      });
      
    },
    addCourse(item){
      this.$axios.get("course/stu/stuAddCourse",{
        params:{
          id:item.id
        }
      }).then(res=>{
        this.$message.success("成功加入课程");
        this.getAllCourse();
        // console.log(res);
      })
      console.log(item);
    },
    enterInfo(row){
      this.$router.push({
        name:"course_detail_pages",
        query:{
          id:row.id,
          course:row.course,
          tea:row.user.username
        }
      })
    }
      
    // await this.$axios
    //     .get("role/input")
    //     .then((res) => {
    //       console.log("!!!", res);
    //       this.inputPermissionsData = res.data.data.permissions;
    //     })
    //     .catch((err) => {
    //       this.$message.error("获取数据失败！");
    //     });
  }
};
</script>

<style scoped>
.aaa {
  display: flex;
  flex-wrap: wrap;
  align-self: flex-start;
  justify-content: flex-start;
}
.course_card {
  width: 248px;
  margin: 10px 33px;
  display: block;
 
}
.image {
  width: 250px;
  height: 200px;
  display: block;
}
.time {
  font-size: 13px;
  color: #999;
}
.bottom {
  margin-top: 13px;
  line-height: 12px;
}
.button {
  padding: 0;
  float: right;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
.addClass{
  float: right;
   cursor: pointer;
   font-weight: 900;
   font-size: 15px;
   color: rgb(49, 124, 245);
}
</style>