<template>
  <el-card class="box-card" ref="boxCard">
    <el-row>
      <span>课程查询：</span><el-input v-model="keyword" class="query_input" placeholder="课程名称 / 老师"></el-input>
      <el-button
        class="query_bt"
        type="primary"
        @click="getPersonnelList"
        >查询</el-button
      >
      <el-button
        class="add_bbt"
        type="primary"
        @click="centerDialogVisible = true"
        >添加课程</el-button
      >
    </el-row>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="course" label="课程名称"> </el-table-column>

      <el-table-column width="150" prop="course" label="课程图片">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.imgUrl"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="user.username" label="任课教师"> </el-table-column>

      <el-table-column width="100" prop="sum" label="课程人数">
      </el-table-column>
      <el-table-column label="是否公开" prop="isPublic">
        <template slot-scope="scope">
          {{ scope.row.isPublic | ifPublicFilters }}
        </template>
      </el-table-column>
      <el-table-column width="300" prop="" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" @click="lookClass(scope.row)"
            >查看人员</el-button
          >
          <el-button type="warning" @click="openUpdate(scope.row)"
            >修改</el-button
          >
          <el-button type="danger" @click="delCourse(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加窗口 -->
    <el-dialog
      title="添加课程"
      :visible.sync="centerDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="addForm" :model="addForm" label-width="80px">
        <el-form-item label="课程名称">
          <el-input v-model="addForm.course"></el-input>
        </el-form-item>
        <el-form-item label="任课教师">
          <el-select v-model="addForm.userId" placeholder="请选择任课教师">
            <el-option v-for="item in teaList" :key="item.id" :label="item.username" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否公开">
          <el-radio-group v-model="addForm.isPublic">
            <el-radio :label="0">不公开</el-radio>
            <el-radio :label="1">公开</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程图片">
          <el-upload
            class="avatar-uploader"
            action="https://www.imgurl.org/api/v2/upload"
            accept=".png,.jpg"
            :data="pdata"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>

        </el-form-item>
        
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="insert()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改窗口 -->
    <el-dialog
      title="修改课程"
      :visible.sync="updateDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="updateForm" :model="updateForm" label-width="80px">
        <el-form-item label="课程名称">
          <el-input v-model="updateForm.course"></el-input>
        </el-form-item>
        <el-form-item label="任课教师">
          <el-select v-model="updateForm.userId" placeholder="请选择任课教师">
            <el-option v-for="item in teaList" :key="item.id" :label="item.username" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否公开">
          <el-radio-group v-model="updateForm.isPublic">
            <el-radio :label="0">不公开</el-radio>
            <el-radio :label="1">公开</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程图片">
          <el-upload
            class="avatar-uploader"
            action="https://www.imgurl.org/api/v2/upload"
            accept=".png,.jpg"
            :data="pdata"
            :show-file-list="false"
            :on-success="handleAvatarSuccessUpdate"
            :before-upload="beforeAvatarUpload"
          >
            <img
              v-if="updateForm.imgUrl"
              :src="updateForm.imgUrl"
              class="avatar"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="update()">确 定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      keyword:"",
      multipleSelection: [],
      centerDialogVisible: false,
      addForm: { course: "", isPublic: 0, imgUrl: "" },
      addForm1: { course: "", isPublic: 0, imgUrl: "" },
      updateForm: {},
      updateDialogVisible: false,
      imageUrl: "",
      teaList:[],
      pdata: {
        uid: "114bccdccf65f6ff67604e0ada9347ac",
        token: "b140392458a32c78a939cd79a292d587",
      },
    };
  },
  mounted() {
    this.getPersonnelList();
    this.getTeaList();
  },
  methods: {
    // 获取所有课程
    async getPersonnelList() {
      await this.$axios.get("course/admin/getAllCourse",{params:{keyword:this.keyword}}).then((res) => {
        res.data.data.Courses.map((item, index) => {
          item.sum = res.data.data.sum[index];
        });
        this.tableData = res.data.data.Courses;
        console.log(res);
      });

      // this.tableData = result.data.data;
      // console.log(this.tableData);
    },
    // 获取教师列表
    async getTeaList(){
      await this.$axios.post("user/admin/adminGetTeaList").then(res=>{
        // console.log(res);
        this.teaList=res.data.data
      })
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 查看课内人员详情
    lookClass(row) {
      this.$router.push({
        name: "personnel_in_the_course",
        query: {
          id: row.id,
          course: row.course,
        },
      });
    },
    // 删除课程
    async delCourse(row) {
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .get("course/tea/delCourse", { params: { id: row.id } })
            .then((res) => {
              if (res.data.code == 200) {
                this.$message.success("删除成功");
                this.getPersonnelList();
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 添加课程
    async insert() {
      this.$axios.post("course/admin/adminInsertCourse", this.addForm).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("添加成功");
          this.addForm = this.addForm1;
          // this.$nextTick(()=>{
          //   this.$refs.addForm.resetFields();
          // })
          this.imageUrl = "";
          this.centerDialogVisible = false;
          this.getPersonnelList();
        }
        if (res.data.code == 888) {
          this.$message.error("已有该课程，不能重复添加！");
        }
        console.log(res);
      });
    },
        // 打开修该窗口
    async openUpdate(row) {
      this.$axios
        .get("course/tea/useCourseIdGetCourse", { params: { id: row.id } })
        .then((res) => {
          this.updateForm = res.data.data;
          this.updateDialogVisible = true;
          console.log(res);
        });
    },
    // 确认后修改
    async update() {
      this.$axios
        .post("course/tea/updateCourseName", this.updateForm)
        .then((res) => {
          console.log(res);
          console.log(this.updateForm);
          if (res.data.code == 888) {
            this.$message.error(res.data.msg);
          }
          if (res.data.code == 200) {
            this.getPersonnelList();
            this.updateDialogVisible = false;
            this.$message.success("修改成功");
          }
        });
    },
    // 图片上传成功后调用的钩子
    handleAvatarSuccess(res, file) {
      console.log(res);
      this.addForm.imgUrl = res.data.url;
      this.imageUrl = res.data.url;
    },
    handleAvatarSuccessUpdate(res, file) {
      this.updateForm.imgUrl = res.data.url;
    },
    // 图片上传前调用的钩子
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isLt5M) {
        this.$message.error("上传头像图片大小不能超过 5MB!");
      }
      return isLt5M;
    },
  },
  // watch:{
  //   keyword(nv,ov){
  //     this.getPersonnelList();
  //   }
  // },
  //局部过滤器
  filters: {
    sexFilters(value) {
      if (value == 1) return "男";
      if (value == 2) return "女";
    },
    ifPublicFilters(value) {
      if (value == 1) return "公开";
      if (value == 0) return "不公开";
    },
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
.add_bbt {
  float: right;
}
.avatar-uploader >>> .el-upload {
  border: 1px dashed #555555;
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
}
.query_input{
  width: 200px;
  margin-right: 15px;
}

</style>