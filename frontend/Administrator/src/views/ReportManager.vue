<template>
  <div>
    <el-tag style="margin-bottom: 30px;">评论举报</el-tag>

    <el-table
            :data="commentReport"
            border
    >
      <el-table-column
              fixed
              prop="informid"
              label="举报ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="objectid"
              label="评论ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="userid"
              label="用户ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="reason"
              label="原因"
              width="120">
      </el-table-column>
      <el-table-column
              label="状态"
              width="120">
        <template slot-scope="scope">
          {{"未处理"}}
        </template>
      </el-table-column>
      <el-table-column
              label="操作"
              width="300">
        <template slot-scope="scope">
          <el-button @click="showComment(scope.row.objectid)" type="text" size="small">查看评论</el-button>
          <el-button @click="doNothing(scope.row)" type="text" size="small">不作操作</el-button>
          <el-button @click="deduct(scope.row)" type="text" size="small">扣信用分</el-button>
          <el-button @click="banUser(scope.row)" type="text" size="small">封禁该用户</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
            background
            :page-size='8'
            layout="prev, pager, next"
            :total="totalComments"
            @current-change="pagec"
            style="margin-top: 30px;"
    >
    </el-pagination>


    <el-tag style="margin-bottom: 30px;margin-top: 30px;">商品举报</el-tag>
    <el-table
            :data="productReport"
            border
    >
      <el-table-column
              fixed
              prop="informid"
              label="举报ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="objectid"
              label="商品ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="userid"
              label="用户ID"
              width="120">
      </el-table-column>
      <el-table-column
              prop="reason"
              label="原因"
              width="120">
      </el-table-column>
      <el-table-column
              label="处理状态"
              width="120">
        <template slot-scope="scope">
          {{"未处理"}}
        </template>
      </el-table-column>
      <el-table-column
             label="操作"
             width="300">
       <template slot-scope="scope">
         <el-button @click="showProduct(scope.row.objectid)" type="text" size="small">查看商品</el-button>
         <el-button @click="doNothing(scope.row)" type="text" size="small">不作操作</el-button>
         <el-button @click="deduct(scope.row)" type="text" size="small">扣信用分</el-button>
         <el-button @click="banUser(scope.row)" type="text" size="small">封禁评论用户</el-button>
       </template>
     </el-table-column>
    </el-table>
    <el-pagination
            background
            :page-size='8'
            layout="prev, pager, next"
            :total="totalProducts"
            @current-change="pagep"
            style="margin-top: 30px;"
    >
    </el-pagination>

    <el-dialog title="详情" :visible.sync="detailVisible">
      <template v-if="toShow == 'product'">
        <p>商品名:{{Detail.name}}</p>
        <p>商品标题:{{Detail.title}}</p>
        <p>商品描述:{{Detail.text}}</p>
        <p>商品图片:</p>
        <el-image v-if="Detail.imgfirst != null" :src="Detail.imgfirst"></el-image>
        <el-image v-if="Detail.imgsecond != null" :src="Detail.imgsecond"></el-image>
        <el-image v-if="Detail.imgthird != null" :src="Detail.imgthird"></el-image>
      </template>
      <template v-if="toShow == 'comment'">
        <p>评论内容:{{Detail.text}}</p>
      </template>
    </el-dialog>

    <el-dialog title="扣分反馈" :visible.sync="editDialogVisible" @close="resetForm('Message')">
      <el-form :model="Message" :rules="rules" ref="Message">
        <el-form-item label="请输入反馈信息" prop="message">
          <el-input v-model="Message.message"></el-input>
        </el-form-item>
        <el-button @click="editDialogVisible=false">取消</el-button>
        <el-button @click="doDeduct(Message.message)">确定</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import axios from 'axios'
  export default {
    name: 'ReportManager',
    created() {
      //todo:初始化页面获取数据填充
      let _this=this;
      // list/{type}/{page}/{size}/{state}
      axios({
        url:this.GLOBAL.host+'inform/list/0/0/8/0',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then((res)=>{
          _this.productReport=res.data.content
          _this.totalProducts=res.data.totalElements
      })

      axios({
        url:this.GLOBAL.host+'inform/list/1/0/8/0',
        method:'get',
        headers:{
          token:localStorage.getItem('token')
        }
      }).then((res)=>{
        _this.commentReport=res.data.content
        _this.totalComments=res.data.totalElements
      })
    },
    data() {
      return{
        commentReport:[
        ],
        productReport:[
        ],
        rules:{
          message: [
            { required: true , message:'请输入发聩信息' ,trigger: 'blur' }
          ],
        },
        totalComments:null,
        totalProducts:null,
        detailVisible:false,
        editDialogVisible:false,
        Detail:{
        },
        toShow:'',
        informid:null,
        Message:{
          message:'',
          type:0,
          isread:0,
          receiverid:null,
        }
      }

    },
    methods:{
      doNothing(row){
        //todo:直接修改举报状态为已处理
        axios.put(this.GLOBAL.host+'inform/legal/'+row.informid).then((res)=>{
          this.$message({
            message:'操作成功',
            type:'success'
          })
          setTimeout(function () {
            window.location.reload()
          },800)
        })
      },
      showComment(commentId){
        const _this=this;
        //todo:根据id获取评论信息填入Detail,然后显示对话框
        axios.get(this.GLOBAL.host+'admi/commentinfo/'+commentId).then(res=>{
          _this.Detail=res.data
        })
        this.toShow='comment';
        this.detailVisible=true;
      },
      showProduct(goodsID){
        //todo:根据id获取商品信息填入Detail,然后显示对话框
        const _this=this;
        axios.get(this.GLOBAL.host+'admi/goodinfo/'+goodsID).then((res)=>{
          console.log(res)
          _this.Detail=res.data
        })
        this.toShow='product';
        this.detailVisible=true;
      },
      deduct(row){

        this.Message.receiverid=row.userid;
        this.informid=row.informid;
        this.editDialogVisible=true;

      },
      doDeduct(message){
        //todo:扣信用分
        this.Message.message=message
        axios.put(this.GLOBAL.host+'inform/deduct/'+this.informid,this.Message).then((res)=>{
              if(res.data == 'success')
              {
                this.$message({
                  message:'操作成功',
                  type:'success'
                })
                setTimeout(function () {
                  window.location.reload()
                },800)
              }
              else{
                this.$message({
                  message:'操作失败',
                  type:'warning'
                })
              }
        })
      },
      banUser(row){
        //todo:封禁评论用户
        let _this=this;
        axios.put(this.GLOBAL.host+'inform/banUser/'+row.informid).then((res)=>{
          if(res.data == 'success')
          {
            this.$message({
              message:'封禁成功',
              type:'success'
            })
            setTimeout(function () {
              window.location.reload()
            },800)
          }else{
            this.$message({
              message:'操作失败,该用户有未完成订单',
              type:'warning'
            })
          }
        })
      },
      pagec(currentPage)
      {
        //todo:改变举报评论列表的页面数据
        let _this=this;
        axios({
          url:this.GLOBAL.host+'inform/list/1/'+(currentPage-1)+'/8/0',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then((res)=>{
          _this.commentReport=res.data.content
        })

      },
      pagep(currentPage){
        //todo:改变举报商品列表的页面数据
        let _this=this;
        axios({
          url:this.GLOBAL.host+'inform/list/0/'+(currentPage-1)+'/8/0',
          method:'get',
          headers:{
            token:localStorage.getItem('token')
          }
        }).then((res)=>{
          _this.productReport=res.data.content
        })

      },
      resetForm(Form)
      {
        this.$refs[Form].resetFields();
      },

    }

  }
</script>

<style scoped>

</style>
