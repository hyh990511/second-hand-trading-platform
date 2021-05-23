<template>
  <div class="banUser">
    <el-tag style="margin-bottom: 30px;">可封禁用户列表</el-tag>

    <el-table
      :data="tableData"
      border
      >
      <el-table-column
        fixed
        prop="userid"
        label="用户id"
        width="120">
      </el-table-column>
      <el-table-column
        prop="accountnumber"
        label="账号"
        width="120">
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="用户名"
        width="120">
      </el-table-column>
      <el-table-column
        prop="gender"
        label="性别"
        width="120">
      </el-table-column>
      <el-table-column
        label="账户状态"
        width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.isuse === 1 ">
            正常
          </span>
          <span v-else>
           封禁
          </span>
        </template>
      </el-table-column>
      <el-table-column

        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button @click="banUser(scope.row)" type="text" size="small">封禁</el-button>
          <el-button @click="reliveBan(scope.row)" type="text" size="small">解封</el-button>
          <el-button type="text" size="small" @click="renderDialog(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      :page-size='6'
      layout="prev, pager, next"
      :total=totalPages
      @current-change="page"
      style="margin-top: 30px;"
    >
    </el-pagination>

    <el-divider></el-divider>

    <el-tag style="margin-bottom: 40px;">通过id查询进行封禁</el-tag>
    <br/>
    <!--这下面的id就是账号不是顺序号id-->
    <el-form :model="idInput" ref="idInput" :rules="rules">
      <el-form-item prop="input">
      <el-input
      placeholder="请输入要封禁用户的id"
      v-model.number="idInput.input"
      clearable
      style="margin-bottom: 30px;">
      </el-input>
      </el-form-item>
    </el-form>

      <el-popconfirm
        title="确定要封禁该用户吗？"
        @onConfirm="idInputBtnClick('idInput')"
      >
        <el-button slot="reference" type="primary" plain>确定</el-button>
      </el-popconfirm>

    <!--编辑对话框-->
    <el-dialog title="编辑用户" :visible.sync="editDialogVisible" @close="resetForm('editForm')">
        <el-form :model="editForm" :rules="rules" ref="editForm">
            <el-form-item label="用户名" prop="nickname">
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

<script>
  import axios from 'axios'
  export default {
    methods: {
      //封禁
     banUser(row) {
       let _this = this;
      //TODO:提交id到后台，修改封禁状态
      axios.put(_this.GLOBAL.host+'admi/banByid/'+row.userid).then(function (res){
         if(res.data=='success')
       {
        _this.success('操作成功');
         setTimeout(function (){
            window.location.reload();
         }, 800);
       }
         else
         {
           _this.warn('操作失败!');
         }
         })
      },
      reliveBan(row)
      {
        let _this = this;
        //TODO:提交id到后台，修改封禁状态
        axios.put(_this.GLOBAL.host+'admi/reliveBanByid/'+row.userid).then(function (res){
          if(res.data=='success')
          {
            _this.success('操作成功');
            setTimeout(function (){
              window.location.reload();
            }, 800);
          }
          else
          {
            _this.warn('操作失败!');
          }
        })
      },
      page(currentPage) {
       let _this=this;
        axios({
          url:_this.GLOBAL.host+'user/findAll/'+(currentPage-1)+'/6',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then(function (resp){
          _this.tableData=resp.data.content;
          _this.totalPages=resp.data.totalElements;
        });
      },
      //输入id进行封禁的输入框的确认按钮
      idInputBtnClick(iForm){
        let input=this.idInput.input;
        let _this =this;
          // TODO:提交input到后台，修改封禁状态

           axios.put(_this.GLOBAL.host+'admi/banByid/'+input).then(function (res){
           if(res.data=='success')
           {
             _this.success('操作成功');
             setTimeout(function (){
               window.location.reload();
             }, 800);

           }
           else _this.warn('操作失败!');
           })

        this.resetForm(iForm);


      },
      //向弹框提供该行的数据并显示对话框
        renderDialog(row){
          this.editForm.userid=row.userid;
          this.editForm.nickname=row.nickname;
          this.editForm.password=row.password;
          this.editDialogVisible=true;
        },
      //确认编辑用户信息
      executeEdit(formName){
       this.$refs[formName].validate((valid)=>{
         let _this=this;
         if(valid)
         {

         //TODO:向后台提交修改
          axios.put(this.GLOBAL.host+'user/setting',_this.editForm).then(function (resp){

            _this.editDialogVisible=false;
            if(resp.data == 'success')
            {
              _this.success('操作成功');
              setTimeout(function (){
                // window.location.reload();
                _this.$router.push({
                  path: '/refresh',
                  query:{
                    destination: '/BanUser',
                  }
                })
              }, 800);
            }
            else _this.warn('操作失败!');
         });

         }
         })


      },
      //清空表单
      resetForm(Form)
      {
        this.$refs[Form].resetFields();
      },
      notice(message){
        this.$message(message);
      },
      warn(message){
          this.$message({
            message:message,
            type:'warning'
          })
      },
      success(message){
        this.$message({
          message:message,
          type:'success'
        })
      }
    },

    created () {
      const _this=this;


      //TODO:页面创建时就向后台请求数据填表格
      axios({
        url:_this.GLOBAL.host+'user/findAll/0/6',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then(res=>{
        _this.tableData=res.data.content;
        _this.totalPages=res.data.totalElements;
      })


    },
    data() {
      let validateName=(rule,value,callback)=>{
        if(value==='')
        {
          callback(new Error('请输入用户名'));
        }
        else
        {
          callback();
        }
      }
      return {
        totalPages:null,
        idInput: {
          input: ''
        },
        editDialogVisible:false,
        dialogInitData:{
              nickname:'',
        },
        //对话框的表单
        editForm:{
        userid:'' ,
        nickname:'',
        password:''
        },
        rules:{
          nickname: [
            { required: true, validator:validateName , message:'请输入用户名' ,trigger: 'blur' }
          ],
          input: [
            {
              required: true,message:'请输入账号' ,trigger: 'blur'
            },
            {
              type:'number',message:'账号为数字',trigger: 'blur'
            }

          ]
        },
        total: null,

        tableData: []
      }
    }
  }
</script>
<style scoped>
.idInput{
  width:300px;
}
</style>
