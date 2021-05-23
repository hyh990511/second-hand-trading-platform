<template>
  <div class="AdminManager">
    <el-tabs v-model="firstName" @tab-click="tabClick">
      <el-tab-pane label="添加管理员" name="first">
        <el-form :model="addForm" :rules="rules" ref="addForm" label-position="top">
          <el-form-item label="管理员账号" prop="account">
            <el-input v-model="addForm.account" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input  v-model="addForm.password" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input  v-model="addForm.nickname" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="权限等级" prop="accesslevel">
            <el-select v-model="addForm.accesslevel" placeholder="权限等级">
              <el-option label="超级管理员" value=0></el-option>
              <el-option label="高级管理员" value=1></el-option>
              <el-option label="普通管理员" value=2></el-option>
            </el-select>
          </el-form-item>
          <el-button @click="addAdmin('addForm')">确认添加</el-button>
          <el-button @click="resetForm('addForm')">重置</el-button>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="编辑管理员" name="second">
        <el-tag style="margin-bottom: 30px;">所有可编辑管理员</el-tag>

        <el-table
          :data="tableData"
          border
        >
          <el-table-column
            fixed
            prop="administratorid"
            label="管理员ID"
            width="120">
          </el-table-column>
          <el-table-column
            prop="account"
            label="账号"
            width="120">
          </el-table-column>
          <el-table-column
            prop="nickname"
            label="昵称"
            width="120">
          </el-table-column>
          <el-table-column
            label="权限等级"
            width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.accesslevel == 0">
                {{"超级管理员"}}
              </span>
              <span v-else-if="scope.row.accesslevel == 1">
                {{"高级管理员"}}
              </span>
              <span v-else>
                {{"普通管理员"}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-popconfirm
                title="确定要删除该管理员吗？"
                @onConfirm="deleteAdmin(scope.row.administratorid,scope.row.accesslevel)"
              >
                <el-button type="text" size="small" slot="reference">删除</el-button>
              </el-popconfirm>
              <el-button type="text" size="small" @click="editAdmin(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          :page-size='6'
          layout="prev, pager, next"
          :total="totalPages"
          @current-change="page"
          style="margin-top: 30px">
        </el-pagination>

        <el-divider></el-divider>



        <!--编辑对话框-->
        <el-dialog title="编辑管理员" :visible.sync="editDialogVisible" @close="resetForm('editForm')">
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
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import axios from 'axios'
  export default {
    name: 'AdminManager',
    data(){
      return {
        totalPages:null,
        firstName:sessionStorage.getItem('adminTab')?sessionStorage.getItem('adminTab'):'first',
        editDialogVisible:false,
        editForm:{
          administratorid:'',
          nickname:'',
          password:'',
        },

        addForm:{
          account:'',
          nickname:'',
          password: '',
          accesslevel:1,
        },

        rules:{
          nickname: [
            { required: true,  message:'请输入名称' ,trigger: 'blur' }
          ],
          password: [
            { required: true,  message:'密码不能为空!' ,trigger: 'blur' }
          ],
          account: [
            { required: true, message:'账号不能为空!' ,trigger: 'blur' },
            {min:5 ,max:10,message: '最少5位最多10位'},
          ],
          accessLevel: [
            { required: true, message:'请选择权限等级' ,trigger: 'blur' }
          ],
        },

        tableData: []
      }
    },
    created(){
      const _this=this;


      //TODO:页面创建时就向后台请求管理员数据填表格
      axios({
        url:this.GLOBAL.host+'admi/list/0/6',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then(function (resp){
        _this.tableData=resp.data.content;
        _this.totalPages=resp.data.totalElements;
      });

    },
    methods:{
      tabClick(tab,event){
        sessionStorage.setItem('adminTab',tab.name)
      },
      page(currentPage) {
        const _this=this;
        axios({
          url:this.GLOBAL.host+'admi/list/'+(currentPage-1)+'/6',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then(function (resp){
          _this.tableData=resp.data.content;
        });
      },
      //添加管理员
      addAdmin(Form){
        let _this=this;
        this.$refs[Form].validate((valid)=>{
          if(valid)
          {
            //TODO:提交表单信息，添加管理员
            axios.post(this.GLOBAL.host+'admi/create',_this.addForm).then(function (resp){
              if(resp.data == 'success')
              {
                _this.success('添加成功');
                _this.resetForm(Form);
                setTimeout(function () {
                  window.location.reload()
                },800)
              }
              else if(resp.data == 'account existed')
              {
                _this.warn('账号已存在');
              }

            });
          }

        })


      },
      //删除管理员
      deleteAdmin(id,accessLevel){
        //权限等级和自己相同则无法删除,0最高级，0才可以编辑管理员
        let _this=this;
        if(accessLevel=='0')
        {
          this.warn('你无法删除权限等于自己权限的管理员');
        }
        else
        {
          //TODO:提交id到后台，根据id删除管理员
        axios.delete(this.GLOBAL.host+'admi/delete/'+id).then(function (res) {
              if(res)
              {
                _this.success('删除成功!');
                setTimeout(function (){
                  window.location.reload();
                }, 800);
              }
              else
                _this.warn('删除失败');
        });
        }
      },
      //编辑,这里只是初始化弹框表单的数据
      editAdmin(row){
        if(row.accessLevel=='0')
        {
          this.warn('你无法编辑权限等级和自己相同的管理员');
        }
        else
          {
          this.editForm.administratorid = row.administratorid;
          this.editForm.nickname = row.nickname
          this.editDialogVisible = true
        }
      },
      executeEdit(Form){
        let _this=this;
        this.$refs[Form].validate((valid)=>{
          if(valid)
          {

          //TODO:向后台提交修改
           axios.put(this.GLOBAL.host+'admi/setting/',_this.editForm).then(function (resp){
              if(resp.data=='success')
              {
                _this.editDialogVisible=false;
                _this.success('修改成功');
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
      },
    }
  }
</script>

<style scoped>

</style>
