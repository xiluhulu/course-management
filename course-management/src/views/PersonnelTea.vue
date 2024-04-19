<template>
  <el-card class="box-card" ref="boxCard">
    <el-row>
      <div class="grid-content bg-purple-dark">
        <span>查询教师：</span><el-input v-model="keyword" class="query_input" placeholder="工号 / 姓名"></el-input>
      <el-button
        class="query_bt"
        type="primary"
        @click="getPersonnelList"
        >查询</el-button
      >
        <el-button class="add_bt" type="primary" @click="addTea"
          >添加教师</el-button
        >
        <el-upload
        class="add_bt"
        action
        accept=".xlsx,.xls"
        :auto-upload="false"
        :show-file-list="false"
        :on-change="handle"
      >
        <el-button type="primary" >批量导入</el-button>
      </el-upload>
      </div>
    </el-row>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      ref="multipleTable"
      
    >
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="stuJobNum" label="工号"> </el-table-column>
      <el-table-column prop="username" label="姓名"> </el-table-column>
      <el-table-column prop="password" label="密码"> </el-table-column>
      <el-table-column label="性别" prop="sex">
        <template slot-scope="tableData">
          {{ tableData.row.sex |sexFilters }}
        </template>
      </el-table-column>
      <el-table-column prop="num" label="手机号码"> </el-table-column>
      <el-table-column prop="" label="操作">
          <template slot-scope="scope">
            <el-button type="warning" @click="updateWindow(scope.row)"
              >修改</el-button
            >
            <el-button type="danger" @click="deleteTea(scope.row)"
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
          <el-form-item label="工号">
            <el-input v-model="addForm.stuJobNum"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="addForm.password"></el-input>
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
          <el-button type="primary" @click="insertTea()">确 定</el-button>
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
      updateForm: {},
      keyword:"",
      centerDialogVisible: false,
      updateDialogVisible: false,
      batchDialogVisible: false,
      cfDialogVisible:false,
      addForm: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "1",
        sex: 1,
        num: "",
        classId: null,
      },
      addForm1: {
        username: "",
        stuJobNum: "",
        password: "12346",
        role: "1",
        sex: 1,
        num: "",
        classId: null,
      },
      pp: [],
      cfList:[]
    };
  },
  mounted() {
    this.getPersonnelList();
    console.log(this.$router);
  },
  methods: {
    // 获取所有人员信息的请求
    async getPersonnelList() {
      await this.$axios.get("user/admin/getallteacherinfo", {
        params: {keyword:this.keyword}
      }).then(res=>{
        this.tableData = res.data.data;
        console.log(this.tableData);
      });
      
    },
    addTea(){
      this.centerDialogVisible = true;
    },
    // 添加教师
    async insertTea() {
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
    // 用于删除教师
    async deleteTea(row) {
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
    async updateWindow(row) {
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
        stuJobNum: { text: "工号", type: "string" },
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
        classId: 0,
        role:1
      };
      console.log(map);
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
      if (value==1) return "男";
      if (value==0) return "女";
    },
  },
};
</script>

<style scoped>
@import "../assets/css/common.css";

.el-row {
  margin-bottom: 10px;
  
}
.add_bt {
  float: right;
  margin-left: 20px;
}
.pq {
  margin: 5px 0;
  color: red;
}
.query_input{
  width: 200px;
  margin-right: 15px;
}
</style>