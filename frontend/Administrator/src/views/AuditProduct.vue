<template>
<div>
  <el-tag style="margin-bottom: 30px;">待审核列表</el-tag>

  <el-table
    :data="tableData"
    border
    style="width: 100%"
  >
    <el-table-column
      fixed
      prop="name"
      label="商品名"
      width="120">
    </el-table-column>
    <el-table-column
      prop="title"
      label="商品标题"
      width="120">
    </el-table-column>
    <el-table-column
      prop="tag"
      label="标签"
      width="120">
    </el-table-column>
    <el-table-column
      prop="goodsprice"
      label="价格"
      width="120">
    </el-table-column>
    <el-table-column
      prop="text"
      label="描述"
      width="120">
    </el-table-column>
    <el-table-column
      label="操作"
      width="200">
      <template slot-scope="scope" >
        <el-button @click="seeDetail(scope.row)" type="text" >查看详情</el-button>
        <el-popconfirm
          title="确定通过该商品审核？"
          @onConfirm="passRequest(scope.row.goodsid)"
        >
          <el-button type="text" slot="reference">通过</el-button>
        </el-popconfirm>
        <el-button type="text" @click="open(scope.row.goodsid)">驳回</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
    background
    :page-size='8'
    layout="prev, pager, next"
    :total="totalPages"
    @current-change="page"
    style="margin-top: 30px;"
  >
  </el-pagination>

  <!--编辑对话框-->
  <el-dialog title="驳回理由" :visible.sync="editDialogVisible" @close="resetForm('editForm')">
    <el-form :model="editForm" :rules="rules" ref="editForm">
      <el-form-item label="请输入驳回理由" prop="nopassReason">
        <el-input v-model="editForm.nopassreason"></el-input>
      </el-form-item>
      <el-button @click="editDialogVisible=false">取消</el-button>
      <el-button @click="refuse(editForm.nopassreason)">确定</el-button>
    </el-form>
  </el-dialog>

  <!--详情对话框-->
  <el-dialog title="商品详情" :visible.sync="detailVisible">
   <p>商品名:{{detailInfo.name}}</p>
    <p>标题:{{detailInfo.title}}</p>
    <p>描述:{{detailInfo.text}}</p>
    <el-image v-if="detailInfo.imgfirst != null" :src="detailInfo.imgfirst"></el-image>
    <el-image v-if="detailInfo.imgsecond != null" :src="detailInfo.imgsecond"></el-image>
    <el-image v-if="detailInfo.imgthird != null" :src="detailInfo.imgthird"></el-image>
  </el-dialog>
</div>
</template>

<script>
  import axios from 'axios'
  export default {
    name: 'AuditProduct',
    methods:{
      open(goodsID){
        this.editDialogVisible=true;
        this.goodsIdNow=goodsID;
      },
      page(currentPage) {
        let _this=this;
        axios({
          url:this.GLOBAL.host+'/admi/zeroExamGoods/list/'+(currentPage-1)+'/8',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then(function (res) {
          _this.tableData=res.data.content;
        })
      },
      passRequest(goodsid){

        this.auditResult.isexamine=1;
        let _this=this;
        //TODO:通过该商品审核，向后台发送相关信息
        axios.put(this.GLOBAL.host+'/admi/GoodsExam/'+goodsid,this.auditResult).then(function (res) {
            if(res.data =='success')
            {
              _this.$message({
                message:'该商品审核已通过',
                type:'success'
              })
              setTimeout(function (){
                window.location.reload();
              }, 800);
            }
        });

      },
      refuse(reason){
        let _this=this;
        this.auditResult.isexamine=2;
        this.auditResult.nopassreason=reason;
        //TODO:拒绝该商品审核，向后台发送相关信息

        axios.put(this.GLOBAL.host+'/admi/GoodsExam/'+this.goodsIdNow,this.auditResult).then(function (res) {
          if(res.data =='success')
          {
            _this.$message({
              message:'该商品审核已驳回',
              type:'success'
            })
            setTimeout(function (){
              window.location.reload();
            }, 800);
          }
        });
        this.editDialogVisible=false;
      },
      seeDetail(row){
        this.detailInfo.goodsid=row.goodsid;
        this.detailInfo.price=row.price;
        this.detailInfo.title=row.title;
        this.detailInfo.text=row.text;
        this.detailInfo.name=row.name;
        this.detailInfo.imgfirst=row.imgfirst;
        this.detailInfo.imgsecond=row.imgsecond;
        this.detailInfo.imgthird=row.imgthird;
         this.detailVisible=true;
      },
      resetForm(Form)
      {
        this.$refs[Form].resetFields();
      },
    },
    created () {
      let _this=this;
            axios({
              url:this.GLOBAL.host+'/admi/zeroExamGoods/list/0/8',
              method:'get',
              headers:{
                token:localStorage.getItem('token')
              }
            }).then(function (res) {
                _this.totalPages=res.data.totalElements;
                _this.tableData=res.data.content;
            })
    },
    data(){
      let validateReason=(rule,value,callback)=>{
        if(value==='')
        {
          callback(new Error('请输入驳回理由'));
        }
        else
        {
          callback();
        }
      }
      return{
        goodsIdNow:0,
        totalPages:null,
        editDialogVisible:false,
        detailVisible:false,
        editForm:{
          nopassreason:''
        },
        detailInfo:{
          goodsid: '',
          title: '',
          name: '',
          text:'',
          goodsprice:'',
        },
        rules:{
          nopassReason: [
            { required: true, validator:validateReason , message:'请输入驳回理由' ,trigger: 'blur' }
          ],
        },
        auditResult:{
          administratorid: sessionStorage.getItem('administratorID'),
          isexamine:0,
          nopassreason:''
        },
        tableData:[],
      }
    }
  }
</script>

<style scoped>

</style>
