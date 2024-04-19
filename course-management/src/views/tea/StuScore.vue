<template>
  <el-card class="box-card" ref="boxCard">
    <el-page-header
      class="page-header"
      @back="goBack"
      :content="$route.query.username"
    >
    </el-page-header>
    <div class="chart" ref="chart"></div>
  </el-card>
</template>
<script>
// 基于准备好的dom，初始化echarts实例

// 绘制图表

export default {
  data() {
    return {
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
    };
  },
  mounted() {
    console.log(this.$route.query);
    this.stuScoreList();
  },
  methods: {
    async stuScoreList() {
      this.$axios
        .get("course/tea/lookStuTotal", {
          params: { id: this.$route.query.id },
        })
        .then((res) => {
          console.log(res);
          if (res.data.data.length == 0) {
            this.$message.error("该同学没有课程");
            this.$router.go(-1);
          }
          res.data.data.map((item, index) => {
            this.y.push(item.course.course);
            this.x.push(item.total);
          });
          let view = this.$echarts.init(this.$refs.chart);
          this.$nextTick(()=>{
            view.resize();
          })
          window.onresize = function () {
            view.resize();
          }
          if (this.y.length != 0) {
            this.option.yAxis.data = this.y;
          }
          if (this.x.length != 0) {
            this.option.series[0].data = this.x;
          }
          view.setOption(this.option);
        });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.box-card {
  display: flex;
}
>>> .el-card__body {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.page-header {
  flex: 1;
}
.chart {
  flex: 30;
}
</style>