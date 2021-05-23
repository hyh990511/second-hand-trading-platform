<template>
<div>
  <el-tag style="margin-bottom: 30px;">未处理申诉</el-tag>

  <el-table
          :data="undisposedData"
          border
  >
    <el-table-column
            fixed
            prop="appealid"
            label="申诉ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="orderid"
            label="订单ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="userid"
            label="用户ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="appealreason"
            label="申诉原因"
            width="120">
    </el-table-column>
    <el-table-column
            prop="appealtime"
            label="申诉时间"
            width="120">
    </el-table-column>
    <el-table-column
            label="状态"
            width="120">
      <template slot-scope="scope">
        <span v-if = "scope.row.state == 0 ">{{"未处理"}}</span>
        <span v-else-if="scope.row.state == 1">{{'正在处理'}}</span>
        <span v-else>{{'已处理'}}</span>
      </template>
    </el-table-column>
    <el-table-column
            label="操作"
            width="100">
      <template slot-scope="scope">
        <el-button @click="disposing(scope.row)" type="text" size="small">开始处理</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
          background
          :page-size='8'
          layout="prev, pager, next"
          :total="undisposedTotal"
          @current-change="unDisposedPage"
          style="margin-top: 30px;"
  >
  </el-pagination>

  <el-tag style="margin-bottom: 30px;margin-top: 30px;">正在处理申诉</el-tag>

  <el-table
          :data="disposingData"
          border
  >
    <el-table-column
            fixed
            prop="appealid"
            label="申诉ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="orderid"
            label="订单ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="userid"
            label="用户ID"
            width="120">
    </el-table-column>
    <el-table-column
            prop="appealreason"
            label="申诉原因"
            width="120">
    </el-table-column>
    <el-table-column
            prop="appealtime"
            label="申诉时间"
            width="120">
    </el-table-column>
    <el-table-column
            label="状态"
            width="120">
      <template slot-scope="scope">
        <span v-if = "scope.row.state == 0 ">{{"未处理"}}</span>
        <span v-else-if="scope.row.state == 1">{{'正在处理'}}</span>
        <span v-else>{{'已处理'}}</span>
      </template>
    </el-table-column>
    <el-table-column
            label="操作"
            width="100">
      <template slot-scope="scope">
        <el-popconfirm
                title="该申诉处理完成？"
                @onConfirm="disposed(scope.row)"
        >
          <el-button type="text" slot="reference">处理完成</el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
          background
          :page-size='8'
          layout="prev, pager, next"
          :total="disposingTotal"
          @current-change="disposingPage"
          style="margin-top: 30px;"
  >
  </el-pagination>
  <!-- 申诉结果信息 -->
  <el-dialog title="申诉结果" :visible.sync="editDialogVisible" @close="resetForm('eform')">
    <el-form :model="eform" :rules="rules" ref="eform">
      <el-form-item label="请输入反馈信息" prop="message">
        <el-input v-model="eform.message"></el-input>
      </el-form-item>
      <el-button @click="editDialogVisible=false">取消</el-button>
      <el-button @click="doDispose(eform.message)">确定</el-button>
    </el-form>
  </el-dialog>
</div>
</template>

<script>
  import axios from 'axios'
  export default {
    name: 'AppealManager',
    created() {
      let _this=this;
      //获取未处理数据
      axios({
        url:this.GLOBAL.host+'complain/list/0/8/0',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then((res)=>{
        _this.undisposedData=res.data.content
        _this.undisposedTotal=res.data.totalElements
      })
      //获取处理中数据
      axios({
        url:this.GLOBAL.host+'complain/list/0/8/1',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then((res)=>{
        _this.disposingData=res.data.content
        _this.disposingTotal=res.data.totalElements
      })
    },
    data(){
      return{
        editDialogVisible:false,
        disposingData:[],
        undisposedData:[],
        undisposedTotal:null,
        disposingTotal:null,
        Message:{
          receiverid:'',
          message:'',
          type:0,
          isread:0
        },
        appealid:null,
        eform:{
          message:''
        },
        rules:{
          message: [
            { required: true , message:'请输入发聩信息' ,trigger: 'blur' }
          ],
        },

      }
    },
    methods:{
      disposing(row){
        let _this=this;
        axios.put(this.GLOBAL.host+'complain/disposing',
                {userid:row.userid,appealid:row.appealid,orderid:row.orderid,appealtime:row.appealtime,appealreason:row.appealreason}).then((res)=>{
          _this.$message({
            message:'开始处理该申诉',
            type:'success'
          })
          setTimeout(function (){
            window.location.reload();
          }, 800);
        })
      },
      disposed(row){
          this.Message.receiverid=row.userid
          this.appealid=row.appealid
          this.editDialogVisible=true;

      },
      doDispose(message)
      {
        let _this=this;
        this.editDialogVisible=false;
        this.Message.message=message
        axios.put(this.GLOBAL.host+'complain/disposed/'+this.appealid+'',this.Message).then((res)=>{
          _this.$message({
            message:'该申诉已处理',
            type:'success'
          })
          setTimeout(function (){
            window.location.reload();
          }, 800);
        })
      },
      unDisposedPage(currentPage){
          let _this=this;
        axios({
          url:this.GLOBAL.host+'complain/list/'+(currentPage-1)+'/8/0',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then((res)=>{
          _this.undisposedData=res.data.content
        })
      },
      disposingPage(currentPage){
        let _this=this;
        axios({
          url:this.GLOBAL.host+'complain/list/'+(currentPage-1)+'/8/1',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then((res)=>{
          _this.disposingData=res.data.content
        })
      },
      resetForm(Form)
      {
        this.$refs[Form].resetFields();
      },

    },


  }
</script>

<style scoped>

</style>
