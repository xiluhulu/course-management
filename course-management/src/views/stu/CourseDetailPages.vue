
<template>
  <div class="box-card" ref="boxCard">
    <div class="b_main_header">
      <el-page-header @back="$router.go(-1)" :content="$route.query.course">
      </el-page-header>
    </div>
    <div class="escCourseDiv">
      <el-button class="escCourse" type="primary" @click="lookClass()"
        >查看排名详情</el-button
      >
      <el-button
        v-if="$route.query.isPublic == 1"
        class="escCourse"
        type="danger"
        @click="escCourse()"
        >退出课程</el-button
      >
    </div>

    <div class="b_main">
      <div class="l">
        <div class="tttt">
          <!-- 排名卡片 -->
          <el-card class="c-card">
            <div slot="header" class="clearfix">
              <span>排名</span>
            </div>
            <div class="ranking">
              <countTo
                :startVal="Math.round(Math.random() * 200)"
                :endVal="rankingOrTotal.ranking"
              ></countTo>
            </div>
          </el-card>
          <!-- 课程分数卡片 -->
          <el-card class="c-card">
            <div slot="header" class="clearfix">
              <span>课程分数</span>
            </div>
            <div class="grade">
              <countTo
                :startVal="Math.round(Math.random() * 200)"
                :endVal="rankingOrTotal.total"
              ></countTo>
            </div>
          </el-card>
        </div>
        <!-- 分数曲线图 -->
        <el-card class="fq_card">
          <div slot="header" class="clearfix">
            <span>月分数分析</span>
          </div>
          <div class="ranking">
            <div class="chart" ref="chart"></div>
          </div>
        </el-card>
      </div>
      <div class="r">
        <div class="infinite-list-wrapper" style="overflow: auto">
          <div class="block">
            <el-timeline>
              <el-timeline-item
                v-for="item in addInfo"
                :key="item.id"
                :timestamp="item.applicationDate | timeFilters2"
                placement="top"
              >
                <el-card>
                  <h4 v-if="item.comeFrom != 2">
                    因{{ item.cause }}，<span v-if="item.comeFrom == 0"
                      >申请加</span
                    ><span v-if="item.comeFrom == 1">老师为你加</span
                    >{{ item.addTheScores }}分
                  </h4>
                  <h4 v-if="item.comeFrom == 2">
                    老师将你的分数修改为{{ item.addTheScores }}分
                  </h4>

                  <br /><br />
                  <p v-if="item.comeFrom == 0">
                    {{ item.user.username }} 提交于
                    {{ item.applicationDate | timeFilters }}
                  </p>
                  <p v-if="item.comeFrom == 1 || item.comeFrom == 2">
                    {{ item.processName }} 提交于
                    {{ item.applicationDate | timeFilters }}
                  </p>
                  <p class="kp">
                    处理状态：
                    <span v-if="item.processState == 0">审批中</span>
                    <span v-if="item.processState == 1">不同意</span>
                    <span v-if="item.processState == 2">同意</span>
                  </p>
                  <p class="kp" v-if="item.processState != 0">
                    处理人：{{ item.processName }}
                  </p>
                  <p class="kp" v-if="item.processState != 0">
                    {{ item.processDate | timeFilters }}
                  </p>
                </el-card>
              </el-timeline-item>
              <!-- <h1  >没有消息</h1> -->
              <el-empty
                v-if="!addInfo.length > 0"
                description="没有消息"
              ></el-empty>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>
    <div class="application">
      <el-button
        class="application_bt"
        type="primary"
        @click="centerDialogVisible = true"
        >加分申请</el-button
      >
    </div>
    <!-- 加分窗口 -->
    <el-dialog
      title="加分申请"
      :visible.sync="centerDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="addForm" :model="addForm" label-width="100px">
        <el-form-item label="理由：">
          <el-input
            class="ww"
            :rows="5"
            type="textarea"
            v-model="addForm.cause"
          ></el-input>
        </el-form-item>
        <el-form-item label="申请分数：">
          <el-input-number
            v-model="addForm.addTheScores"
            @change="handleChange"
            :min="1"
            :max="5"
            label="描述文字"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="applyForExtraPoints()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
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
      centerDialogVisible: false,
      addForm: {
        courseId: this.$route.query.id,
        cause: "",
        addTheScores: 1,
      },
      addForm1: {
        courseId: this.$route.query.id,
        cause: "",
        addTheScores: 1,
      },
      addInfo: [],
      option : {
        title: {
          text: this.$route.query.course,
          left: '0'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '5%',
          right: '1%',
          bottom: '30%'
        },
        xAxis: {
          data:[]
        },
        yAxis: {},
        toolbox: {
          right: 0,
          feature: {
            restore: {},
            saveAsImage: {}
          }
        },
        dataZoom: [
          //必须要有这个配置项
          {
            // startValue: '1'
            endValue:"1"
          },
          {
            //鼠标缩放
            type: 'inside'
          }
        ], 
        series: {
          name: '今日加分',
          type: 'line',
          data: [1,2,5,3],

        }
      }
    };
  },
  mounted() {
    this.stuGetTotlaAndRanking();
    this.getBonusPointsDetails();
    this.getGraphData();
    
    
  },
  methods: {
    // 学术获取分数及排名
    async stuGetTotlaAndRanking() {
      this.$axios
        .get("course/stu/stuGetTotlaAndRanking", {
          params: { courseId: this.$route.query.id },
        })
        .then((res) => {
          this.rankingOrTotal = res.data.data;
          console.log(res);
        });
    },
    // 学生申请加分
    async applyForExtraPoints() {
      if (this.addForm.cause != "") {
        this.$axios
          .post("add_score_record/stu/applyForExtraPoints", this.addForm)
          .then((res) => {
            console.log(res);
            this.centerDialogVisible = false;
            this.getBonusPointsDetails();
            this.addForm = this.addForm1;
          });
      } else {
        this.$message.error("请填写理由");
      }
    },
    handleChange(value) {
      console.log(value);
    },
    // 学生获取加分详情
    async getBonusPointsDetails() {
      this.$axios
        .get("add_score_record/stu/getBonusPointsDetails", {
          params: { courseId: this.$route.query.id },
        })
        .then((res) => {
          this.addInfo = res.data.data;

          console.log(this.addInfo);
        });
    },
    // 学生获取曲线图
    async getGraphData() {
      this.$axios
        .get("add_score_record/stu/getGraphData", {
          params: { courseId: this.$route.query.id },
        })
        .then((res) => {
          res.data.data.map(item=>{
            item.processDate=this.timeFilters3(item.processDate)
          })
          // 当前时间
          let date = new Date()        

          // x坐标
          let xdata=[]
          let month= this.timeFilters4(date)

          for(let i = 0;i<new Date(date.getFullYear(),date.getMonth()+1,0).getDate();i++){
            if(i<9){
              xdata.push(month+"0"+(i+1))
            }else{
              xdata.push(month+(i+1))
            }
            
          }
          let ydata=[]
          xdata.map(item=>{
            let fen=0
            for (let i = 0; i < res.data.data.length; i++) {
              // console.log(res.data.data[i].processDate==item);
              if(res.data.data[i].processDate==item ){
                // console.log("9999",res.data.data[i].addTheScores);
                fen=res.data.data[i].addTheScores+fen
              }
            }
            ydata.push(fen)
          })
          this.option.xAxis.data=xdata
          this.option.series.data=ydata
          this.option.dataZoom[0].endValue=this.timeFilters3(date);
          console.log("aaaaa",this.option.dataZoom[0].endValue);
          let view = this.$echarts.init(this.$refs.chart);
          this.$nextTick(()=>{
            view.resize();
          })
          window.onresize = function () {
            view.resize();
          }
          view.setOption(this.option);


          // console.log("y",ydata);
          // console.log("x",xdata);
          // console.log(this.timeFilters4(date));
            
          // console.log("当月的天数：",date.getDate());
          // console.log(date.getFullYear());
          // let aaaaaaaa =
          // console.log("aaaaaa",aaaaaaaa);
          // console.log("当前月",date.getMonth()+1);

          // console.log("pppp",res);
          // console.log("pppp",this.timeFilters3());
        });
    },
    
    // 退出课程
    async escCourse() {
      this.$axios
        .get("course/stu/escPublicCourse", {
          params: {
            id: this.$route.query.id,
          },
        })
        .then((res) => {
          if (res.data.code == 200) {
            this.$message.success("成功退出");
            this.$router.go(-1);
          }
        });
    },
    // 查看课内人员详情
    lookClass() {
      this.$router.push({
        name: "personnel_in_the_course",
        query: {
          id: this.$route.query.id,
          course: this.$route.query.course,
        },
      });
    },
    timeFilters3(value){
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;

      return y + "-" + MM + "-" + d;
    },
    timeFilters4(value){
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      return y + "-" + MM + "-";
    }
  }, //局部过滤器
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
  height: 80%;
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
.tttt {
  flex: 1;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  /*background-color: #fff;*/
}
.c-card {
  min-width: 48%;
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
.ww {
  width: 500px;
}
.escCourseDiv {
  width: 100%;
}
.escCourse {
  float: right;
  margin: 0 0 5px 15px;
}
.fq_card {
  min-width: 100%;
  height: 320px;
}
.chart{
  width: 100%;
  height: 240px;
}
</style>