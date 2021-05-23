<template>
  <div class="noticeManager" >
    <el-tabs v-model="firstName" @tab-click="tabClick">
      <el-tab-pane label="发布公告" name="first" >
        <el-form :model="noticeForm" :rules="rules" ref="noticeForm" label-position="top">
          <el-form-item label="标题" prop="title">
            <el-input v-model="noticeForm.title" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="text">
            <el-input type="textarea" v-model="noticeForm.text" style="width: 500px;"></el-input>
          </el-form-item>
          <el-button @click="addNotice('noticeForm')">确认发布</el-button>
          <el-button @click="resetForm('noticeForm')">重置</el-button>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="管理公告" name="second">
        <el-tag style="margin-bottom: 30px;">已发布公告</el-tag>

        <el-table
          :data="tableData"
          border
        >
          <el-table-column
            fixed
            prop="noticeid"
            label="公告ID"
            width="120">
          </el-table-column>
          <el-table-column
            prop="administratorid"
            label="发布者ID"
            width="120">
          </el-table-column>
          <el-table-column
            prop="title"
            label="标题"
            width="120">
          </el-table-column>
          <el-table-column
            prop="releasetime"
            label="发布时间"
            width="120">
          </el-table-column>
          <el-table-column
            prop="text"
            label="内容"
            width="200">
          </el-table-column>
          <el-table-column

            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-popconfirm
                title="确定要删除该公告吗？"
                @onConfirm="deleteNotice(scope.row.noticeid)"
              >
                <el-button type="text" size="small" slot="reference">删除</el-button>
              </el-popconfirm>
              <el-button type="text" size="small" @click="editNotice(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          background
          :page-size='6'
          layout="prev, pager, next"
          :total=totalPages
          @current-change="page"
        style="margin-top: 30px">
        </el-pagination>

        <el-divider></el-divider>



        <!--编辑对话框-->
        <el-dialog title="编辑公告" :visible.sync="editDialogVisible" @close="resetForm('editForm')">
          <el-form :model="editForm" :rules="rules" ref="editForm">
            <el-form-item label="标题" prop="title">
              <el-input v-model="editForm.title"></el-input>
            </el-form-item>
            <el-form-item label="内容" prop="text">
              <el-input v-model="editForm.text"></el-input>
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
    name: 'PublishNotice',
    created(){
      const _this=this;

      //TODO:页面创建时就向后台请求数据填表格
      axios({
        url:_this.GLOBAL.host+'notice/list/0/6',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then(function (resp){
        _this.tableData=resp.data.content;
        _this.totalPages=resp.data.totalElements;

      });

    },
    data(){
      let validateTitle=(rule,value,callback)=>{
        if(value==='')
        {
          callback(new Error('请输入标题'));
        }
        else
        {
          callback();
        }
      }
      let validateText=(rule,value,callback)=>{
        if(value==='')
        {
          callback(new Error('标题不能为空!'));
        }
        else
        {
          callback();
        }
      }
      return {
        totalPages:null,
        firstName:sessionStorage.getItem('noticeTab')?sessionStorage.getItem('noticeTab'):'first',
        editDialogVisible:false,
        editForm:{
          noticeid:'',
          title:'',
          text:'',
        },

        noticeForm:{
          administratorid:sessionStorage.getItem('administratorID'),
          title:'',
          text:'',
        },

        rules:{
          title: [
            { required: true, validator:validateTitle , message:'请输入标题' ,trigger: 'blur' }
          ],
          text: [
            { required: true, validator:validateText , message:'内容不能为空!' ,trigger: 'blur' }
          ],
        },

        tableData: []
      }
    },
    methods:{
      tabClick(tab, event){
        sessionStorage.setItem('noticeTab',tab.name)
      },
      page(currentPage) {
        const _this=this;
        axios({
          url:_this.GLOBAL.host+'notice/list/'+(currentPage-1)+'/6',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then(function (resp){
          _this.tableData=resp.data.content;
        });
      },
      //添加公告
      addNotice(Form){
        let _this=this;

               //TODO:提交表单信息，添加公告
               axios.post(_this.GLOBAL.host+'notice/issuer',_this.noticeForm).then(function (resp){
                 if(resp)
                 {
                   _this.success('添加成功');
                   _this.resetForm(Form);
                   setTimeout(function (){
                     window.location.reload();
                   }, 800);
                 }

                 else
                  _this.warn('添加失败');
               });

      },
      //删除公告
      deleteNotice(id){
          //TODO:提交id到后台，根据id删除公告
        let _this=this;
          axios.delete(_this.GLOBAL.host+'notice/delete/'+id).then(function (res) {
                if(res)
                {
                  _this.success('删除成功');
                  setTimeout(function (){
                    window.location.reload();
                  }, 800);
                }
                else
                  this.warn('操作失败');
          })
      },
      //编辑公告，这里只是初始化弹框表单的数据
      editNotice(row){
          this.editForm.noticeid=row.noticeid;
          this.editForm.title=row.title;
          this.editForm.text=row.text;
          this.editDialogVisible=true;
      },
      executeEdit(Form){
        this.$refs[Form].validate((valid)=>{
          let _this=this;
          if(valid)
          {

          //TODO:向后台提交修改
           axios.put(_this.GLOBAL.host+'notice/update',_this.editForm).then(function (resp){

             if(resp.data == 'success')
             {
               _this.editDialogVisible=false;
               _this.success('修改成功');
               setTimeout(function (){
                 window.location.reload();
               }, 800);
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
