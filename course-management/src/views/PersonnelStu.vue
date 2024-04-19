<template>
  <el-card class="box-card" ref="boxCard">
    <el-row>
      <span>查询学生：</span><el-input v-model="keyword" class="query_input" placeholder="学号 / 姓名"></el-input>
      <el-button
        class="query_bt"
        type="primary"
        @click="getPersonnelList"
        >查询</el-button
      >
      <div class="grid-content bg-purple-dark">
        <el-button class="add_bt" type="primary" @click="addStu"
          >添加学生</el-button
        >
      </div>
    </el-row>
    <el-row>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <el-table-column prop="stuJobNum" label="学号"> </el-table-column>
        <el-table-column prop="username" label="姓名"> </el-table-column>
        <el-table-column prop="password" label="密码"> </el-table-column>
        <el-table-column prop="className" label="所在班级"> </el-table-column>
        <el-table-column label="性别" prop="sex">
          <template slot-scope="tableData">
            {{ tableData.row.sex | sexFilters }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="手机号码"> </el-table-column>
        <el-table-column prop="" label="操作">
          <template slot-scope="scope">
            <el-button type="warning" @click="updateWindow(scope.row)"
              >修改</el-button
            >
            <el-button type="danger" @click="deleteStu(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 添加窗口 -->
      <el-dialog
        title="添加学生"
        :visible.sync="centerDialogVisible"
        width="1000px"
        center
      >
        <el-form ref="addForm" :model="addForm" label-width="80px">
          <el-form-item label="姓名">
            <el-input v-model="addForm.username"></el-input>
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="addForm.stuJobNum"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="addForm.password"></el-input>
          </el-form-item>
          <el-form-item label="所在班级">
            <el-select v-model="addForm.classId" placeholder="请选着班级">
              <el-option
                v-for="item in classList"
                :key="item.id"
                :label="item.classname"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="addForm.sex">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input v-model="addForm.num"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="insertStu()">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 修改窗口 -->
      <el-dialog
        title="添加学生"
        :visible.sync="updateDialogVisible"
        width="1000px"
        center
      >
        <el-form ref="updateForm" :model="updateForm" label-width="80px">
          <el-form-item label="姓名">
            <el-input v-model="updateForm.username"></el-input>
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="updateForm.stuJobNum"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="updateForm.password"></el-input>
          </el-form-item>
          <el-form-item label="所在班级">
            <el-select v-model="updateForm.classId" placeholder="请选着班级">
              <el-option
                v-for="item in classList"
                :key="item.id"
                :label="item.classname"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="updateForm.sex">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input v-model="updateForm.num"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="updateDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateUser()">确 定</el-button>
        </span>
      </el-dialog>
    </el-row>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      keyword:"",
      multipleSelection: [],
      classList: [],
      updateForm: {},
      centerDialogVisible: false,
      updateDialogVisible: false,
      addForm: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "2",
        sex: 1,
        num: "",
        classId: null,
      },
      addForm1: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "2",
        sex: 1,
        num: "",
        classId: null,
      },
    };
  },
  mounted() {
    this.getPersonnelList();
    this.getClassList();
    // this.$message.success
  },
  methods: {
    // 获取所有人员信息的请求
    async getPersonnelList() {
      await this.$axios.get("user/admin/getallstuinfo",{params:{keyword:this.keyword}}).then((res) => {
        console.log(res);
        res.data.data.stuInfo.map((item, index) => {
          item.className = res.data.data.className[index];
        });
        this.tableData = res.data.data.stuInfo;
        console.log(res);
      });

      // this.tableData = result.data.data;
    },
    // 获取所有班级
    async getClassList() {
      this.$axios.get("all_lass//admin/get_all_class").then((res) => {
        this.classList = res.data.data.c;
        console.log("@@@@@@", this.classList);
      });
    },
    // 添加学生
    async insertStu() {
      if (
        this.addForm.username == "" ||
        this.addForm.stuJobNum == "" ||
        this.addForm.password == "" ||
        this.addForm.num == ""
      ) {
        // console.log(this.addForm);
        this.$message.error("表单还有空值");
        return;
      }

      this.$axios.post("user/insertstu", this.addForm).then((res) => {
        if (res.data.code == 508) {
          this.$message.error("用户已存在，不能添加");
        }
        if (res.data.code == 200) {
          this.centerDialogVisible = false;
          this.$message.success("添加成功");
          this.addForm = this.addForm1;
          this.getPersonnelList();
        }
        // console.log(res);
      });
    },
    // 用于删除学生
    async deleteStu(row) {
      this.$confirm("此操作将永久学生，请谨慎！", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // console.log("-----",row);
          this.$axios.post("user/tea/deleteUser", row).then((res) => {
            // console.log("==",res)
            this.getPersonnelList();
          });
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 用于展示添加页面
    addStu() {
      this.centerDialogVisible = true;
    },
    //打开修改窗口
    updateWindow(row) {
      this.$axios
        .get("user/tea/useIdGetUser", { params: { id: row.id } })
        .then((res) => {
          this.updateForm = res.data.data;
          this.updateDialogVisible = true;
        });
    },
    // 执行修改
    updateUser() {
      this.$axios.post("user/tea/update", this.updateForm).then((res) => {
        console.log(res);
        if (res.data.code == 200) {
          this.$message.success("修改成功");
          this.updateDialogVisible = false;
          this.getPersonnelList();
        }
        if (res.data.code != 200) {
          this.$message.error(res.data.msg);
        }
      });
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
  //局部过滤器
  filters: {
    sexFilters(value) {
      if (value == 1) return "男";
      if (value == 0) return "女";
    },
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.grid-content {
  border-radius: 4px;
  min-height: 40px;
}
/*.bg-purple-dark {
    background: #99a9bf;
  }*/
.add_bt {
  float: right;
}
.query_input{
  width: 200px;
  margin-right: 15px;
}
</style>