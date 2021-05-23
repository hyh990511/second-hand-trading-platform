<template>

<div  >
    <div class="title">
    <h1 style="font-size: 60px;">校园二手交易平台</h1>
      <h4 style="font-size: 20px;">管理系统</h4>
    </div>
    <div class="login_form">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-position="right" label-width="60px">
                <el-form-item label="账号" prop="account" class="input_style">
                    <el-input type="" v-model="ruleForm.account" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" class="input_style">
                    <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item class="login_style" label-width="0">
                    <el-button type="primary" @click="submitForm('ruleForm')" style="position: center">登录</el-button>
                </el-form-item>
            </el-form>

    </div>

</div>
</template>

<script>
  import axios from 'axios'
  import { mapMutations } from 'vuex';
  export default {
    data() {

      /*let validateUserName=(rule,value,callback)=>{
            if(value==='')
            {
              callback(new Error('请输入账号'));
            }
            else
            {

              callback();
            }
      }
      let validatePass = (rule, value, callback) => {
        if (value === '')
        {
          callback(new Error('请输入密码'));
        }
        else
        {

          callback();
        }
      };*/

      return {
        ruleForm: {
          password: '',
          account:''
        },
        rules: {
          account: [
            { required: true,  message:'请输入账号' ,trigger: 'blur' }
          ],
          password:[
           {required:true,  message:'请输入密码' ,trigger: 'blur'}
         ]
        }
      };
    },
    methods: {

      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          let _this=this;
          if (valid) {

            //TODO:向后端发送用户输入信息进行验证，返回验证结果
              axios.post(_this.GLOBAL.host+'admi/login/',_this.ruleForm).then(function (resp) {
              if (resp.data.message == 'Account does not exist') {
                alert("账号不存在!")
              } else if (resp.data.message == 'Pw Error') {
                alert("密码错误!")
              } else {
                //成功登录返回的是管理员的id
                sessionStorage.setItem('administratorID',resp.data.message);
                //设置登录成功的标志
                sessionStorage.setItem('isLogin',_this.ruleForm.account);
                //设置token
                localStorage.setItem('token',resp.data.token)
                let grade='0';
                //TODO:向后台请求当前登录管理员的权限等级
                axios.get(_this.GLOBAL.host+'admi/getLevel/'+sessionStorage.getItem('administratorID')).then(function (res) {
                  grade=res.data;
                  sessionStorage.setItem('permissionGrade',grade);
                    sessionStorage.setItem('first','null');
                    _this.$router.push({path:'/Index'});
                });

              }
            });

          } else {
            this.$message({
                message:'请输入账号密码',
                type:'warning'
            })

            return false;
          }
        });
      },
    }
  }
</script>

<style scoped>
  .login_form{
        text-align: center;
        width: 300px;
      position: center;
      margin: auto;
  }
  .title{
    margin-top: 200px;
    text-align: center;
    margin-bottom: 100px;
  }

  .input_style{
    margin-bottom: 20px;

  }
  .login_style {
      margin: auto;
  }
</style>
