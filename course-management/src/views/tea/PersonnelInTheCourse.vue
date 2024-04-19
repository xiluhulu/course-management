<template>
  <el-card class="box-card" ref="boxCard">
    <div class="b_main_header">
      <el-page-header @back="$router.go(-1)" :content="$route.query.course">
      </el-page-header>
    </div>
    <div class="fr">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="选择班级">
          <el-select v-model="formInline.classId" placeholder="全部">
            <el-option label="全部" :value="0"></el-option>
            <el-option 
              v-for="(item,index) in classList"
              :label="item.classname"
              :value="item.id"
              :key="index"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-tooltip
      class="item"
      effect="dark"
      content="切换展示模式"
      placement="top-start"
    >
      <el-button type="primary" @click="switchViews_bt"> 切换视图</el-button>
    </el-tooltip>
    <el-button v-if="role!=2" class="delClass" type="danger" @click="delDialogVisible = true"
      >删除班级</el-button
    >
    <el-button v-if="role!=2" class="addClass" type="primary" @click="openAddWindow()"
      >添加班级</el-button
    >
    <el-table
      v-if="switchViews"
      :data="tableData"
      border
      style="width: 100%"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="user.username" label="姓名"> </el-table-column>
      <el-table-column prop="user.stuJobNum" label="学号"> </el-table-column>
      <el-table-column prop="allClass.classname" label="班级">
      </el-table-column>
      <el-table-column prop="total" label="分数"> </el-table-column>
      <el-table-column v-if="role!=2" align="center" width="350" prop="" label="操作">
        <template slot-scope="scope">
          <el-button type="warning" @click="updateTotalOpen(scope.row)">修改分值</el-button>
          <el-button type="primary" @click="bonusPointsOpen(scope.row)"
            >加分</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!switchViews" class="histogram" :style="hhh" ref="chart"></div>
    <!-- 添加窗口 -->
    <el-dialog
      title="添加班级"
      :visible.sync="centerDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="addForm" :model="addForm" label-width="80px">
        <el-form-item label="可选班级">
          <el-select v-model="addForm.classId" placeholder="请选择班级">
            <el-option  
              v-for="item in optionalClass"
              :key="item.id"
              :label="item.classname"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="insert()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 删除窗口 -->
    <el-dialog
      title="删除班级"
      :visible.sync="delDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="delForm" :model="delForm" label-width="80px">
        <el-form-item label="可选班级">
          <el-select v-model="delForm.classId" placeholder="请选择班级">
            
            <el-option
              v-for="item in classList"
              :key="item.id"
              :label="item.classname"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="delClass()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 加分窗口 -->
    <el-dialog
      title="加分申请"
      :visible.sync="bonusPointsWindow"
      width="1000px"
      center
    >
      <el-form
        ref="bonusPointsFrom"
        :model="bonusPointsFrom"
        label-width="100px"
      >
        <el-form-item label="申请分数：">
          <el-input-number
            v-model="bonusPointsFrom.addTheScores"
            @change="handleChange"
            :min="1"
            :max="5"
            label="描述文字"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="bonusPointsWindow = false">取 消</el-button>
        <el-button type="primary" @click="bonusPoints()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 改分窗口 -->
    <el-dialog
      title="修改分数"
      :visible.sync="updateWindow"
      width="1000px"
      center
    >
      <el-form
        ref="updateFrom"
        :model="updateFrom"
        label-width="100px"
      >
        <el-form-item label="分数：">
          <el-input-number
            v-model="updateFrom.total"
            @change="handleChange"
            :min="0"
            :max="200"
            label="描述文字"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateWindow = false">取 消</el-button>
        <el-button type="primary" @click="updateTotal()">确 定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      role:window.sessionStorage.getItem("role"),
      classList: [    
      ],

      formInline: {
        classId: '',
      },
      addForm: {
        courseId: this.$route.query.id,
        classId: '',
      },
      addForm1: {
        courseId: this.$route.query.id,
        classId: '',
      },
      delForm: {
        courseId: this.$route.query.id,
        classId: '',
      },
      delForm1: {
        courseId: this.$route.query.id,
        classId: '',
      },
      optionalClass: [],
      centerDialogVisible: false,
      delDialogVisible: false,
      // 用于切换视图
      switchViews: true,
      hhh: "",
      x: [],
      y: [],
      option: {
        xAxis: {
          max: 200,
        },
        yAxis: {
          type: "category",
          data: [],
          inverse: true,
          animationDuration: 300,
          animationDurationUpdate: 300,
          // max: 2 // only the largest 3 bars will be displayed
        },
        series: [
          {
            realtimeSort: true,
            name: "X",
            type: "bar",
            data: [],
            label: {
              show: true,
              position: "right",
              valueAnimation: true,
            },
            barWidth: 50,
          },
        ],
        legend: {
          show: true,
        },
        animationDuration: 0,
        animationDurationUpdate: 3000,
        animationEasing: "linear",
        animationEasingUpdate: "linear",
      },
      bonusPointsWindow: false,
      bonusPointsFrom: {
        courseId: this.$route.query.id,
        userId: 0,
        addTheScores: 1,
        cause: "表现良好",
      },
      bonusPointsFrom1: {
        courseId: this.$route.query.id,
        userId: 0,
        addTheScores: 1,
        cause: "表现良好",
      },
      updateWindow:false,
      updateFrom:{    
      }
    };
  },
  mounted() {
    // console.log(this.$route.query);
    // 获取选了该课程的人员
    this.getList();
    // 获取班级列表用于查询
    this.getClass();
    this.asdasd();
    // this.barGraphHeight();
  },
  methods: {
    // 获取悬选择了该课程的人员
    async getList() {
      this.$axios
        .get("course/tea/useCourseIdGetCoursePersonnel", {
          params: {
            courseId: this.$route.query.id,
            classId: 0,
          },
        })
        .then((res) => {
          console.log(res);
          this.tableData = res.data.data;
          // 用于控制柱状图高度
          this.hhh = "height:" + 100 * this.tableData.length + "px;";
          this.y = [];
          this.x = [];
          res.data.data.map((item, index) => {
            this.y.push(item.user.username);
            this.x.push(item.total);
          });
          // console.log(this.y,this.x);
        });
    },
    // 获取选择了该课程的班级
    async getClass() {
      this.$axios
        .get("all_lass/tea/useCourseIdGetClass", {
          params: { id: this.$route.query.id },
        })
        .then((res) => {
          console.log("aaaaaa",res);
          if (res.data.code==200) {
            this.classList = res.data.data;
          }
          if(res.data.code==888){
            this.classList = res.data.data;
          }

          
        });
    },
    // 获取可添加的课程
    async asdasd(){
      await this.$axios
        .get("all_lass/tea/useCourseIdGetOptionalClass", {
          params: { id: this.$route.query.id },
        })
        .then((res) => {
          console.log("获取添加课程：",res);
          this.optionalClass = res.data.data;
          console.log(this.optionalClass);
        });
    },
    //打开添加班级对话框并获取可添加的课程
    openAddWindow() {
      this.centerDialogVisible = true;
      this.asdasd;
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 查询某个班级的学生
    async onSubmit() {
      this.$axios
        .get("course/tea/useCourseIdGetCoursePersonnel", {
          params: {
            courseId: this.$route.query.id,
            classId: this.formInline.classId,
          },
        })
        .then((res) => {
          console.log(res);
          this.tableData = res.data.data;
          this.switchViews=true;
          // 用于控制柱状图高度
          this.hhh = "height:" + 100 * this.tableData.length + "px;";
          this.y = [];
          this.x = [];
          res.data.data.map((item, index) => {
            this.y.push(item.user.username);
            this.x.push(item.total);
          });
        });
    },
    // 确认后添加所选班级学生
    async insert() {
      if (this.addForm.classId != 0) {
        this.$axios
          .get("all_lass/tea/useClassIdLetCourseInsertStu", {
            params: {
              classId: this.addForm.classId,
              courseId: this.$route.query.id,
            },
          })
          .then((res) => {
            console.log(res);
            if (res.data.code == 888) {
              this.$message.error(res.data.msg);
            }
            if (res.data.code == 200) {
              this.$message.success("添加成功");
              this.getList();
              this.getClass();
              this.asdasd();
              this.addForm.classId='';
              this.centerDialogVisible = false;
            }
          });
      } else {
        this.$message.error("请选择再进行确定！");
      }
    },
    // 删除班级
    async delClass() {
      if (this.delForm.classId != 0) {
        await this.$confirm("此操作将永久删除该班级的所有学生, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            console.log(this.delForm);
            this.$axios
              .get("course/tea/useCourseIdAndClassIdDelStuTotal", {
                params: {
                  courseId: this.delForm.courseId,
                  classId: this.delForm.classId,
                },
              })
              .then((res) => {
                if (res.data.code == 200) {
                  this.$message.success("删除成功！");
                  this.getList();
                  this.getClass();
                  this.asdasd();
                  this.delForm.classId='';
                  this.delDialogVisible = false;
                }
              });
            // this.$message({
            //   type: 'success',
            //   message: '删除成功!'
            // });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        this.$message.error("请选择班级");
      }
    },
    // 切换按钮
    switchViews_bt() {
      this.switchViews = !this.switchViews;
      if (!this.switchViews) {
        let view = this.$echarts.init(this.$refs.chart);
        console.log(this.$refs.chart.offsetWidth);
        this.$nextTick(() => {
          view.resize();
        });

        window.onresize = function () {
          view.resize();
        };
        if (this.y.length != 0) {
          this.option.yAxis.data = this.y;
        }
        if (this.x.length != 0) {
          this.option.series[0].data = this.x;
        }
        view.setOption(this.option);
      }
    },
    handleChange(value) {
      console.log(value);
    },
    // 打开窗口
    bonusPointsOpen(row) {
      this.bonusPointsWindow = true;
      // console.log(row);
      this.bonusPointsFrom.userId = row.userId;
    },
    //确认加分
    bonusPoints() {
      // console.log(this.bonusPointsFrom);
      this.$axios
        .post("add_score_record/tea/teaBonusPoints", this.bonusPointsFrom)
        .then((res) => {
          this.bonusPointsWindow = false;
          this.$message.success("加分成功");
          this.bonusPointsFrom = this.bonusPointsFrom1;
          this.getList();
        });
    },
    // 打开修改分数窗口
    updateTotalOpen(row){
      this.updateWindow=true;
      this.updateFrom=row;
      
      console.log(row);
    },
    // 确认后修改
    async updateTotal(){
      this.$axios.post("course/tea/teaUpdataTotal",this.updateFrom).then(res=>{
        console.log(res);
        if(res.data.code==200){
          this.$message.success("修改成功")
          this.updateWindow=false
          this.getList();
        }
      })
    }
  },
};
</script>

<style scoped>
.fr {
  display: block;
  float: left;
  width: 50%;
}
.addClass {
  float: right;
  margin-right: 20px;
}
.delClass {
  float: right;
}
.histogram {
  width: 100%;
  height: 100px;
  margin-top: 23px;
  background-color: #fff;
}
.b_main_header {
  margin-bottom: 10px;
}
</style>