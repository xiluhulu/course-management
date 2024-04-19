<template>
  <el-card class="box-card" ref="boxCard">
    <el-page-header @back="goBack" :content="$route.query.classname">
    </el-page-header>
    <div class="h_box">
      <el-upload
        class="upload-demo"
        action
        accept=".xlsx,.xls"
        :auto-upload="false"
        :show-file-list="false"
        :on-change="handle"
      >
        <el-button type="primary" class="pl_bt">批量导入</el-button>
      </el-upload>
      <el-button type="primary" @click="addStu">添加学生</el-button>
    </div>

    <el-table
      :data="tableData"
      border
      style="width: 100%"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="username" label="姓名"> </el-table-column>
      <el-table-column prop="stuJobNum" label="学号\工号"> </el-table-column>
      <el-table-column prop="password" label="密码"> </el-table-column>
      <el-table-column label="性别" prop="sex">
        <template slot-scope="tableData">
          {{ tableData.row.sex | sexFilters }}
        </template>
      </el-table-column>
      <el-table-column prop="num" label="手机号码"> </el-table-column>
      <el-table-column align="center" width="350" prop="" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" @click="lookStuScore(scope.row)"
            >查看分数详情</el-button
          >
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
        <el-form-item label="特殊资源">
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
      title="修改学生"
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
        <el-form-item label="特殊资源">
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
    <!-- 批量导入窗口 -->
    <el-dialog
      title="批量导入"
      :visible.sync="batchDialogVisible"
      width="1000px"
      center
    >
      <p class="pq">* 确认无误后添加</p>
      <el-table
        :data="pp"
        border
        style="width: 100%"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <el-table-column prop="username" label="姓名"> </el-table-column>
        <el-table-column prop="stuJobNum" label="学号\工号"></el-table-column>
        <el-table-column prop="password" label="密码"> </el-table-column>
        <el-table-column label="性别" prop="sex">
          <template slot-scope="tableData">
            {{ tableData.row.sex | sexFilters }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="手机号码"> </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="teaBatchAddStu">确 定</el-button>
      </span>
    </el-dialog>
        <!-- 有重复后弹出窗口 -->
    <el-dialog
      title="重复人员"
      :visible.sync="cfDialogVisible"
      width="1000px"
      center
    >
      <p class="pq">* 请在Excel表格修改以下同学的学号</p>
      <el-table
        :data="cfList"
        border
        style="width: 100%"
        ref="multipleTable"
        :row-class-name="tableRowClassName"
        
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <el-table-column prop="username" label="姓名"> </el-table-column>
        <el-table-column prop="stuJobNum" label="学号\工号"></el-table-column>
        <el-table-column prop="password" label="密码"> </el-table-column>
        <el-table-column label="性别" prop="sex">
          <template slot-scope="tableData">
            {{ tableData.row.sex | sexFilters }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="手机号码"> </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="gb">取 消</el-button>
        <el-button type="primary" @click="gb">确 定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import xlsx from "xlsx";

export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      centerDialogVisible: false,
      addForm: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "2",
        sex: 1,
        num: "",
        classId: this.$route.query.id,
      },
      addForm1: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "2",
        sex: 1,
        num: "",
        classId: this.$route.query.id,
      },
      updateDialogVisible: false,
      updateForm: {},
      batchDialogVisible: false,
      cfDialogVisible:false,
      pp: [],
      cfList:[]
    };
  },
  mounted() {
    this.getPersonnelList();
  },
  methods: {
    // 获取班级的信息
    async getPersonnelList() {
      // console.log(this.$route.query.id);
      this.$axios
        .get("user/tea/useClassIdGetStu", {
          params: { classId: this.$route.query.id },
        })
        .then((res) => {
          this.tableData = res.data.data;
          // console.log(res);
        });
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 返回上一个页面
    goBack() {
      this.$router.go(-1);
    },
    // 用于展示添加页面
    addStu() {
      this.centerDialogVisible = true;
    },
    // 查看学生分数详情
    lookStuScore(row) {
      // console.log(row)
      this.$router.push({
        name: "tea_look_stu_score",
        query: {
          id: row.id,
          username: row.username,
        },
      });
    },
    // 添加学生
    insertStu() {
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
    deleteStu(row) {
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
        if(res.data.code==200){
          this.$message.success("修改成功")
          this.updateDialogVisible=false
        }
      });
    },
    // 将xlsx文件内容转成json
    async handle(ev) {
      console.log(ev);
      let file = ev.raw;
      if (!file) return;
      // // 读取file中的数据
      let data = await this.readFile(file);
      let workbook = xlsx.read(data, { type: "binary" }),
        worksheet = workbook.Sheets[workbook.SheetNames[0]];
      data = xlsx.utils.sheet_to_json(worksheet);
      let arr = [];
      let character = {
        username: { text: "姓名", type: "string" },
        stuJobNum: { text: "学号", type: "string" },
        password: { text: "密码", type: "string" },
        sex: { text: "性别", type: "number" },
        num: { text: "手机号码", type: "string" },
      };
      data.forEach((item) => {
        let obj = {};
        for (let i in character) {
          if (!character.hasOwnProperty(i)) break;
          let v = character[i],
            text = v.text,
            type = v.type;
          v = item[text] || "";
          if (text == "性别") {
            if (v == "男") {
              type === "number" ? (v = Number(1)) : null;
            }
            if (v == "女") {
              type === "number" ? (v = Number(0)) : null;
            }
          } else {
            type === "string" ? (v = String(v)) : null;
            type === "number" ? (v = Number(v)) : null;
          }
          obj[i] = v;
        }
        arr.push(obj);
      });
      this.pp = arr;
      this.batchDialogVisible = true;
      // console.log(arr);
    },
    // 使用二进制形式读取xlsx文件
    readFile(file) {
      return new Promise((resolve) => {
        let reader = new FileReader();
        reader.readAsBinaryString(file);
        reader.onload = (ev) => {
          resolve(ev.target.result);
        };
      });
    },
    // 批量提交
    async teaBatchAddStu() {
      let map = {
        userList: this.pp,
        classId: this.$route.query.id,
        role:2
      };
      this.$axios.post("user//tea/teaBatchAddStu", map).then((res) => {
        if (res.data.code == 200) {
          this.$message.success("添加成功");
          this.batchDialogVisible = false;
          this.getPersonnelList();
        }
        if (res.data.code == 654) {
          this.cfList=res.data.data
          this.cfDialogVisible=true
        }
        console.log(this.cfList);
      });
    },
    //用于修改表格字体颜色
    tableRowClassName({row, rowIndex}) {
      console.log(row);
        // if (rowIndex === 1) {
          return 'warning';
        // } else if (rowIndex === 3) {
          // return 'success-row';
        // }
        // return '';
      },
    gb(){
      this.cfDialogVisible = false 
      this.batchDialogVisible=false
    }
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
@import "../../assets/css/common.css";

.h_box {
  width: 100%;
  padding: 10px 0;
  background-color: #fff;
  display: flex;
  justify-content: flex-end;
}
.pl_bt {
  margin: 0 10px;
}
.pq {
  margin: 5px 0;
  color: red;
}

</style>