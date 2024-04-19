<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">课堂管理系统</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="stuJobNum">
                    <el-input v-model="param.stuJobNum" placeholder="工号/学号">
                        <el-button class="aa"  slot="prepend" icon="el-icon-user-solid"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        type="password"
                        placeholder="密码"
                        v-model="param.password"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button class="aa" slot="prepend" icon="el-icon-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                <p class="login-tips">Tips : (👉ﾟヮﾟ)👉 &nbsp&nbsp w(ﾟДﾟ)w &nbsp&nbsp  👈(ﾟヮﾟ👈)</p>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    data: function() {
        return {
            param: {
                stuJobNum: '',
                password: '',
            },
            rules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            },
        };
    },
    methods: {
        async submitForm(){
             this.$axios.post("user/login",this.param)
            .then(res=>{
                if(res.data.code==200){
                    this.$message.success("登录成功")
                    console.log("登录成功");
                    // 保存token
                    window.sessionStorage.setItem("token",res.data.data.token);
                    window.sessionStorage.setItem("role",res.data.data.role);
                    // 跳转页面
                    this.$router.push("/");
                }else if(res.data.code==501){
                    this.$message.error("用户不存在")
                    console.log("用户不存在");
                }else if(res.data.code==502){
                    this.$message.error("密码错误")
                    console.log("密码错误");
                }

                console.log(res.data);
            });
        },
    },
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url("../assets/img/wallhaven-nelmz8_1920x1080.png") ;
    background-size: 100% 100%;
    /*background-repeat: repeat !important;*/
    
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
.aa {
    padding: 0;
    
}
.aa >>>i{
    margin-left: 10px;
}
</style>