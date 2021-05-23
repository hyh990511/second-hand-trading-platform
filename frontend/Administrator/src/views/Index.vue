<template>
    <div>
        <el-header class="el-header">
                <span style="font-size: x-large;margin-left: 100px;float: left;">校园二手交易平台-管理平台</span>

                <el-button style="float: right;margin-top: 16px;margin-right: 20px;" type="info" size="mini" @click="logOut">登出</el-button>
            <el-button style="float: right;margin-right: 20px;margin-top: 15px;" type="info" icon="el-icon-edit" size="small" @click="editInfo" circle></el-button>
            <div style="float: right;margin-right: 100px;"><span>{{this.welcome}}</span></div>
        </el-header>
    <el-container style="height: 900px; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu  router :default-openeds="['0','1','2','3','4']">
          <!--权限0是超管，有所有权限-->
          <template v-if="permissionGrade=='0'">
        <el-submenu v-for="(item,index) in $router.options.routes[1].children" :index=" index+'' "
                    >

            <template slot="title"><i class="el-icon-menu"></i>{{item.name}}</template>
            <el-menu-item v-for="(item1,index1) in item.children" :index="item1.path" :class="$route.path==item1.path?'is-active':'' ">
              {{item1.name}}
            </el-menu-item>

            </el-submenu>
          </template>
          <!--权限1是普通管理，有除了管理员管理外的所有权限-->
          <template v-else-if="permissionGrade=='1'">
          <el-submenu v-for="(item,index) in $router.options.routes[1].children" :index=" index+'' "
                      v-if="item.name!='管理员管理'">

              <template slot="title"><i class="el-icon-menu"></i>{{item.name}}</template>
              <el-menu-item v-for="(item1,index1) in item.children" :index="item1.path" :class="$route.path==item1.path?'is-active':'' ">
                  {{item1.name}}
              </el-menu-item>

          </el-submenu>
          </template>
          <!--权限2是最低级管理，只有对商品相关的处理权限-->
          <template v-else>
          <el-submenu v-if="item.name=='商品管理' || item.name=='公告管理' || item.name=='违规管理'"
                      v-for="(item,index) in $router.options.routes[1].children" :index=" index+'' ">

              <template slot="title"><i class="el-icon-menu"></i>{{item.name}}</template>
              <el-menu-item v-for="(item1,index1) in item.children" :index="item1.path" :class="$route.path==item1.path?'is-active':'' ">
                  {{item1.name}}
              </el-menu-item>

          </el-submenu>
          </template>
      </el-menu>

    </el-aside>
    <el-main >
      <router-view></router-view>
    </el-main>
  </el-container>
        <el-dialog title="编辑信息" :visible.sync="editDialogVisible" @close="resetForm('editForm')">
            <el-form :model="editForm" :rules="rules" ref="editForm">
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="editForm.nickname"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="editForm.password"></el-input>
                </el-form-item>
                <el-button @click="editDialogVisible=false">取消</el-button>
                <el-button @click="executeEdit('editForm')">确定</el-button>
            </el-form>
        </el-dialog>
    </div>
</template>

<style>
  .el-header {
    background-color: #B3C0D1;
    color: white;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }
</style>

<script>
  import axios from 'axios'
  export default {



      created() {
          axios({
              url:this.GLOBAL.host+'admi/getInfoById/'+sessionStorage.getItem('administratorID'),
              method:'get',
              headers:{
              token:localStorage.getItem('token')
          }
          }).then(resp=>{
              let date=new Date();
              let timeNow=date.getHours();
              if(timeNow>0&&timeNow<=11)this.welcome+='早上';
              else if(timeNow>11&&timeNow<=13)this.welcome+='中午';
              else if(timeNow>13&&timeNow<=18)this.welcome+='下午';
              else this.welcome+='晚上';
              this.welcome+='好，'+resp.data.nickname;
              sessionStorage.setItem('password',resp.data.password);
              sessionStorage.setItem('nickname',resp.data.nickname);
          })
          if(sessionStorage.getItem('first')=='null')
          {
              sessionStorage.setItem('first','fuck you');
              window.location.reload();
          }

      },
      data(){
      return{
          editDialogVisible:false,
          rules:{
              nickname: [
                  { required: true,  message:'请输入名称' ,trigger: 'blur' }
              ],
              password: [
                  { required: true,  message:'密码不能为空!' ,trigger: 'blur' }
              ],
          },
          editForm:{
              administratorid:'',
              nickname:'',
              password:'',
          },
        //管理员路由的下标，管理员管理路由总是在数组最后面
        indx: this.$router.options.routes[1].children.length-1,
        permissionGrade:sessionStorage.getItem('permissionGrade'),
         welcome:'',
      }
    },
      methods:{
        logOut()
        {
            //登出，直接跳到登录页面
            this.$router.push({path:'/'});
        },
          editInfo()
          {
              this.editForm.administratorid = sessionStorage.getItem('administratorID');
              this.editForm.nickname = sessionStorage.getItem('nickname');
              this.editDialogVisible = true
          },
          executeEdit(Form){
              let _this=this;
              this.$refs[Form].validate((valid)=>{
                  if(valid)
                  {

                      //TODO:向后台提交修改
                      axios.put(this.GLOBAL.host+'admi/setting/',_this.editForm).then(function (resp){
                          console.log(resp.data)
                          if(resp.data=='success')
                          {
                              _this.editDialogVisible=false;
                              _this.$message({
                                  message:'修改成功',
                                  type:'success'
                              })
                              setTimeout(function () {
                                  window.location.reload()
                              },800)
                          }
                      });


                  }
              })
          },
          resetForm(Form)
          {
              this.$refs[Form].resetFields();
          },


      }


  }
</script>
