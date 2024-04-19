<template>
  <el-card class="box-card" ref="boxCard">
    <el-table 
    :data="tableData"
     border 
     style="width: 100%"
     ref="multipleTable"
     @selection-change="handleSelectionChange"
      >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column type="index" width="50" label="序号"> </el-table-column>
      <el-table-column prop="classname" label="班级"> </el-table-column>
      <el-table-column prop="user.username" label="班主任"> </el-table-column> 
      <el-table-column prop="" label="操作">
        <el-button type="primary" icon="el-icon-edit" circle></el-button>
        <el-button type="danger" icon="el-icon-delete" circle></el-button>
      </el-table-column>

    </el-table>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: []
    };
  },
  mounted() {
    this.getPersonnelList();
  },
  methods: {
    // 获取所有人员信息的请求
    async getPersonnelList() {
      const result = await this.$axios.get("all_lass/admin/get_all_class");
      this.tableData = result.data.data;
      console.log(this.tableData);
    },
    // 将选中的信息添加到multipleSelection中
    handleSelectionChange(val) {
        this.multipleSelection = val;
      },
    
    
  },
};
</script>

<style scoped>

</style>