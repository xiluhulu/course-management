
<template>
  <div class="box-card" ref="boxCard">
  <!-- <el-row class="rrr">
      <span>课程查询：</span><el-input v-model="keyword" class="query_input" placeholder="课程名称 / 老师"></el-input>
      <el-button
        class="query_bt"
        type="primary"
        @click="getPersonnelList"
        >查询</el-button
      >
    </el-row> -->
    <div class="b_main">
      
      <div class="l">
        <!-- 排名卡片 -->
        <el-card class="c-card">
          <div slot="header" class="clearfix">
            <span>申请数量</span>
          </div>
          <div class="ranking">
            <countTo
              :startVal="0"
              :endVal="TotalApprovals"
            ></countTo>
          </div>
        </el-card>
        <!-- 未审批卡片 -->
        <el-tooltip class="item" effect="dark" content="点击跳转到为审批的消息" placement="top-start">
        <el-card class="c-card" @click.native="ink()">
          <div slot="header" class="clearfix">
            <span>未审批</span>
          </div>
          <div class="grade">
            <countTo
              :startVal="0"
              :endVal="unapprovedNum"
            ></countTo>
          </div>
        </el-card>
        </el-tooltip>
      </div>
      <div class="r">
        <div class="infinite-list-wrapper" style="overflow: auto">
          <div class="block">
            <el-timeline id="aaaa">
              <el-timeline-item v-for="item in addInfo" :id="item.id" :key="item.id" :timestamp="item.applicationDate|timeFilters2" placement="top">
                <el-card>
                  <h2>课程：{{item.course.course}} </h2>
                  <br>
                  <h4 v-if="item.comeFrom!=2">因 <span v-if="item.comeFrom==1">{{item.user.username}}</span> {{item.cause}}，<span v-if="item.comeFrom==0">申请加</span><span v-if="item.comeFrom==1">为他加</span>{{item.addTheScores}}分</h4>
                  <h4 v-if="item.comeFrom==2">修改 {{item.user.username}} 该课程分数为{{item.addTheScores}}分</h4>
                  <br /><br />
                  <p v-if="item.comeFrom==0">{{item.user.username}} 提交于 {{item.applicationDate|timeFilters}}</p>
                  <p v-if="item.comeFrom==1 || item.comeFrom==2">{{item.processName}} 提交于 {{item.applicationDate|timeFilters}}</p>
                  <p class="kp">处理状态：
                    <span v-if="item.processState==0">未审批</span>
                    <span v-if="item.processState==1">不同意</span>
                    <span v-if="item.processState==2">同意</span>
                  </p>
                  <p class="cz" v-if="item.processState==0">
                    <el-button type="primary" @click="consentApplication(item)">同 意</el-button>
                    <el-button type="danger" @click="disagreeWithTheApplication(item)">不同意</el-button>
                  </p>
                  <p class="kp" v-if="item.processState!=0">{{item.processDate|timeFilters}}</p>
                </el-card>
              </el-timeline-item>
              <el-empty v-if="!addInfo.length>0" description="没有消息"></el-empty>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>
    
    
  </div>
</template>


<script>
import countTo from "vue-count-to";

export default {
  components: {
    countTo,
  },
  data() {
    return {
      rankingOrTotal: [],
      

      addInfo:[],
    //   未审批数量
      unapprovedNum:0,
    //   总审批数量
      TotalApprovals:0
    };
  },
  mounted() {

    this.teaGetBonusPointsDetails();
    this.teaGetUnapproved();
    this.teaGetTotalApprovals();
  },
  methods: {

    handleChange(value) {
        console.log(value);
      },
      // 教师获取加分详情
    async teaGetBonusPointsDetails(){
      this.$axios.get("add_score_record/tea/teaGetBonusPointsDetails").then(res=>{
        // console.log(res);
        this.addInfo=res.data.data
        console.log(this.addInfo);
      })
    },
    //教师获取未审批数量
    async teaGetUnapproved(){
        this.$axios.get("add_score_record/tea/teaGetUnapproved").then(res=>{
            this.unapprovedNum=res.data.data
            console.log(res)
        })
    },
    //    教师获取总审批数量
    async teaGetTotalApprovals(){
        this.$axios.get("add_score_record/tea/teaGetTotalApprovals").then(res=>{
            this.TotalApprovals=res.data.data
            console.log(res)
        })
    },
    // 同意
    async consentApplication(row){
        this.$axios.post("add_score_record/tea/consentApplication",row).then(res=>{
            this.teaGetBonusPointsDetails();
            this.teaGetUnapproved();
            console.log(res);
        })
    },
    async disagreeWithTheApplication(row){
        this.$axios.post("add_score_record/tea/disagreeWithTheApplication",row).then(res=>{
            this.teaGetBonusPointsDetails();
            this.teaGetUnapproved();
            console.log(res);
        })
    },
    //点击跳转到未审批的消息方便寻找
    ink(){
        this.addInfo.some((item,index)=>{
        if(item.processState==0){
          document.getElementById(item.id).scrollIntoView();
          return true;
        }
      })
      console.log(555555);
    }
  },
  //局部过滤器
  filters: {
    timeFilters(value) {
      let date = new Date(value);
          let y = date.getFullYear();
          let MM = date.getMonth() + 1;
          MM = MM < 10 ? "0" + MM : MM;
          let d = date.getDate();
          d = d < 10 ? "0" + d : d;
          let h = date.getHours();
          h = h < 10 ? "0" + h : h;
          let m = date.getMinutes();
          m = m < 10 ? "0" + m : m;
          let s = date.getSeconds();
          s = s < 10 ? "0" + s : s;
          return y + "-" + MM + "-" + d + " " + h + ":" + m;
    },
    timeFilters2(value) {
      let date = new Date(value);
          let y = date.getFullYear();
          let MM = date.getMonth() + 1;
          MM = MM < 10 ? "0" + MM : MM;
          let d = date.getDate();
          d = d < 10 ? "0" + d : d;
          
          return y + "-" + MM + "-" + d;
    },
  },
};
</script>

<style  scoped>
.box-card {
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: flex-start;
}
.b_main {
  width: 100%;
  height: 90%;
  /*background-color: gold;*/
  display: flex;
  justify-content: space-between;
}
.l {
  min-width: 400px;
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: baseline;
  flex-wrap: wrap;
  padding: 10px;
  /*background-color: rgb(225, 160, 160);*/
}

.r {
  flex: 2;
  min-width: 800px;
  height: 100%;
  background-color: #fff;
}
.infinite-list-wrapper {
  height: 100%;
}
.block {
  margin: 10px;
}
.c-card {
  width: 400px;
  margin: 20px 0;
}
.grade {
  width: 100%;
  height: 100px;
  font-size: 100px;
  text-align: center;
  line-height: 100px;
  color: rgb(255, 47, 47);
}
.ranking {
  width: 100%;
  height: 100px;
  font-size: 100px;
  text-align: center;
  line-height: 100px;
  color: rgb(47, 154, 255);
}
.kp {
  margin-left: 75%;
}
.application {
  display: flex;
  justify-content: end;
  width: 100%;
  margin: 10px 0;
}
.application_bt {
  margin-right: 20px;
}
.cz{
    padding: 20px 0;
}
.cz button{
    float: right;
    margin: 0 10px;
    height: 20px;
    line-height: 0px;
    font-size: 10px;
}
.ww{
  width: 500px;
}
.rrr{
  display: flex;
  flex-direction: row;
  align-items: center;
}
.rrr span{
  width: 160px;
}
.query_input{
  margin-right: 20px;
}
</style>