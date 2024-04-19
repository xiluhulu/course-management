<template>
  <el-card class="box-card" ref="boxCard">
    <div class="h_box">
      <el-button type="primary" @click="centerDialogVisible = true"
        >添加班级</el-button
      >
    </div>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="classname" label="班级"> </el-table-column>
      <el-table-column prop="sum" label="班级的人数"></el-table-column>
      <el-table-column width="400" align="center" prop="" label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            @click="lookClassPeople(scope.row.id, scope.row.classname)"
            >查看班级人员</el-button
          >
          <el-button type="warning" @click="updateOpen(scope.row)">修改</el-button>
          <el-button type="danger" @click="deleteClass(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加弹窗 -->
    <el-dialog
      title="添加班级"
      :visible.sync="centerDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="updateForm" :model="addForm" label-width="80px">
        <el-form-item label="班级名称 :">
          <el-input v-model="addForm.classname"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="insertClass()">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改弹窗 -->
    <el-dialog
      title="添加班级"
      :visible.sync="updateDialogVisible"
      width="1000px"
      center
    >
      <el-form ref="updateForm" :model="updateForm" label-width="80px">
        <el-form-item label="班级名称 :">
          <el-input v-model="updateForm.classname"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateClass()">确 定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      centerDialogVisible: false,
      addForm: {
        id: 0,
        classname: "",
      },
      addForm1: {
        id: 0,
        classname: "",
      },
      updateForm:{},
      updateDialogVisible: false,
    };
  },
  mounted() {
    this.getClassList();
  },
  methods: {
    // 获取所有人员信息的请求
    async getClassList() {
      this.$axios.get("all_lass//tea/getClassList").then((res) => {
        res.data.data.c.map((item, index) => {
          item.sum = res.data.data.sum[index];
        });
        this.tableData = res.data.data.c;
        console.log(this.tableData);
      });
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 查看班级人员
    lookClassPeople(classId, classname) {
      this.$router.push({
        name: "class_people",
        query: {
          id: classId,
          classname: classname,
        },
      });
      console.log(classname, classId);
    },
    // 添加课程
    insertClass() {
      this.$axios
        .post("all_lass//tea/insertClass", this.addForm)
        .then((res) => {
          console.log(res);
          if (res.data.code == 505) {
            this.$message.error("已有该课程不能重复添加");
          }
          if (res.data.code == 200) {
            this.$message.success("添加成功");
            this.getClassList();
            this.addForm=this.addForm1;
            this.centerDialogVisible = false;
          }
        });
    },
    // 用于删除班级
    deleteClass(row) {
      this.$confirm("此操作将永久删除班级及班级的人员，请谨慎！", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // console.log("-----",row);
          this.$axios.post("all_lass/tea/deleteClass", row).then((res) => {
            // console.log("==",res)

            this.getClassList();
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
    // 打开修改窗口并获取信息
    updateOpen(row){
      this.updateDialogVisible=true;
      this.$axios.get("all_lass/tea/useIdGetClass",{params:{id:row.id}}).then(res=>{
        this.updateForm=res.data.data
      })
    },
    // 确认后修改信息
    updateClass(){
      this.$axios.post("all_lass/tea/useIdUpdeteClass",this.updateForm).then(res=>{
        console.log(res);
        if(res.data.code==505){
          this.$message.error("课程已存在！");
        }
        if(res.data.code==200){
          this.$message.success("修改成功");
          this.updateDialogVisible=false;
          this.getClassList();
        }
      })
    }

  },
};
</script>

<style scoped>
.h_box {
  width: 100%;
  padding: 10px 0;
  background-color: #fff;
  display: flex;
  justify-content: flex-end;
}
.el-input {
  width: 800px;
}
</style>