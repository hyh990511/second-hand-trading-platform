<template>
  <div id="page">

    <el-main>
      <div id="issue_tag">
        <p style="padding-top: 15px">发布商品</p>
      </div>
      <br/>
      <el-container>
        <el-form :label-position="labelPosition" :model="issueForm" ref="issueForm" :rules="rules">
          <el-container >
            <el-form-item label="商品名称"  prop="name">
              <el-input v-model="issueForm.name" maxlength="30" class="el_input"></el-input>
            </el-form-item>
            <el-form-item label="标题" prop="title" >
              <el-input v-model="issueForm.title" maxlength="30" class="el_input"></el-input>
            </el-form-item>
        </el-container>

          <el-container >
            <el-form-item label="选择专区" prop="ascription" >
              <el-select v-model="issueForm.ascription" placeholder="专区" class="el_input">
                <el-option label="普通商品" value="common"></el-option>
                <el-option label="毕业专区" value="graduation" v-if="graduated == 1"></el-option>
                <el-option label="以书易书" value="books"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="类别" prop="tag" v-if="issueForm.ascription !== 'books'">
              <el-select v-model="issueForm.tag" placeholder="类别" class="el_input">
                <el-option label="书籍" value="书籍"></el-option>
                <el-option label="代步工具" value='代步工具'></el-option>
                <el-option label="生活用品" value='生活用品'></el-option>
                <el-option label="数码产品" value='数码产品'></el-option>
                <el-option label="其它" value="其它"></el-option>
              </el-select>
            </el-form-item>

              <el-form-item label="图书类别" prop="childtag" v-if="issueForm.ascription == 'books' || issueForm.tag == '书籍'">
              <el-select v-model="issueForm.childtag" placeholder="图书类别" class="el_input">
                <el-option label="人文" value="人文"></el-option>
                <el-option label="社会" value="社会"></el-option>
                <el-option label="科学" value="科学"></el-option>
                <el-option label="教材" value="教材"></el-option>
                <el-option label="考研" value="考研"></el-option>
                <el-option label="其它" value="其它"></el-option>
              </el-select>
              </el-form-item>

          </el-container>
          <br/>
          <el-container >

            <el-form-item label="描述"  prop="text" >
              <el-input type="textarea" :autosize="{ minRows: 4}" v-model="issueForm.text" class="el_input"></el-input>
            </el-form-item>
            <el-form-item label="价格" prop="goodsPrice"  style="float:left;" v-if="issueForm.ascription !== 'books'">
              <el-input v-model.number="issueForm.goodsPrice" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="运费" prop="sendPrice"  style="float:left;" v-if="issueForm.ascription !== 'books'">
              <el-input v-model.number="issueForm.sendPrice" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="意向交换书籍" prop="exchangeBook"  style="float:left;" v-if="issueForm.ascription === 'books'">
              <el-input v-model="issueForm.exchangeBook" style="width: 200px"></el-input>
            </el-form-item>
          </el-container>
        </el-form>
      </el-container>
      <br/><br/>
      <el-container>
        <el-upload
                :action="this.GLOBAL.host+'QiNiu/uploadimg'"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                multiple
                :limit='3'
                :file-list="filelist"
                :on-exceed="exceed"
                :on-success="handleSuccess"
                :auto-upload="false"
                :on-remove="remove"
                :on-change="changed"
                ref="upLoad"
        >
          <i class="el-icon-plus"></i>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
      </el-container>
      <el-button type="success" @click="submitAll('issueForm')" round v-if="issueTag=='issue'">确认发布</el-button>
      <el-button type="success" @click="submitAll('issueForm')" round v-else>确认编辑</el-button>
      <el-button type="success" @click="resetForm('issueForm')" round>重置</el-button>
    </el-main>

  </div>
</template>

<script>
  import axios from 'axios'
  export default {
    beforeRouteLeave(to,from,next){
      sessionStorage.setItem('issueTag','issue')
      next()
    },
    data() {
      let validatePrice=(rule, value, callback)=>{
        if (value==''||!value) {
          callback(new Error('价格不能为空！'));
        }
        else callback();
      }
      return {
        accountnumber: null,
        graduated: null,
        num:0,
        dialogImageUrl: '',
        dialogVisible: false,
        qiniuUrl:'https://upload-z2.qiniup.com',
        issueTag:sessionStorage.getItem('issueTag'),
        filelist:[],
        uploadFinish:false,
        imgchanged:false,
        labelPosition: 'left',
        //输入表单信息
        issueForm: {
          exchangeBook:'',
          sendPrice:'',
          goodsid:'',
          name: '',
          title: '',
          text: '',
          ascription:'',
          goodsPrice:'',
          tag:'',
          childtag:'',
          //这是用户上传的图片，直接放这里等下一起传给后端，反正对表单输入没影响，空就是没有
          imgfirst:null,
          imgsecond:null,
          imgthird:null
        },
        rules:{
          exchangeBook: [
            {required: true, message: '请输要交换的书籍名', trigger: 'blur' }
          ],
          name: [
            {required: true, message: '请输入商品名称', trigger: 'blur' }
          ],
          title:[
            {required: true, message: '请输入发布标题', trigger: 'blur' }
          ],
          childtag:[
            {required: true, message: '请选择类别', trigger: 'blur' }
          ],
          describe:[
            {required: true, message: '请输入商品描述', trigger: 'blur' }
          ],
          ascription:[
            {required: true, message: '请选择商品分区', trigger: 'blur' }
          ],
          tag:[
            {required: true, message: '请输入商品类别', trigger: 'blur' }
          ],
          goodsPrice:[
            { required:true,message: '价格不能为空', trigger: 'blur' },
            {type: 'number', message: '价格必须为数字'}
          ],
        },
      };
    },
    watch:{
      uploadFinish(val,oldVal)
      {
        const _this=this;
        if(val===true)
        {
          //为了解决命名大小写问题，这里先用一个傻办法
          this.issueForm['goodsid']=this.issueForm['goodsID']
          this.issueForm['goodsprice']=this.issueForm['goodsPrice']
          this.issueForm['sendprice']=this.issueForm['sendPrice']
          this.issueForm['exchangebook']=this.issueForm['exchangeBook']
          this.issueForm['releasetime']=this.issueForm['realaseTime']
          this.issueForm['isexamine']=this.issueForm['isExamine']
          this.issueForm['nopassreason']=this.issueForm['nopassReason']
          this.issueForm['examinetime']=this.issueForm['examineTime']
          if(this.issueTag === 'issue'){
            axios.post(this.GLOBAL.host+'goods/issue/'+sessionStorage.getItem('accountNumber'),this.issueForm).then(()=>{
              _this.$message({
                message:'发布成功!',
                type:'success'
              })
              _this.$router.push('/listIssues')
            })


          }else{
            console.log(this.issueForm)
            axios.put(this.GLOBAL.host+'goods/issue/alter',this.issueForm).then((res)=>{

              _this.$message({
                message:'修改成功!',
                type:'success'
              })
              _this.$router.push('/listIssues')
            })

          }
        }
      }


    },
    methods: {
    remove(file,filelist){
      this.num--
      if(this.issueForm['imgfirst'] === file.url)
        this.issueForm['imgfirst']=null
      else if(this.issueForm['imgsecond'] === file.url)
        this.issueForm['imgsecond']=null
      else if(this.issueForm['imgthird'] === file.url)
        this.issueForm['imgthird']=null
      this.filelist=filelist
      this.imgchanged=true
    },
      changed(file,filelist){
        this.imgchanged=true
        this.filelist=filelist
      },
    exceed(){
      alert('图片数目已达到三张');
    },

      //提交表单和图片链接
      handleSuccess(response, file, fileList)
      {
        if(this.issueForm['imgfirst']==null)
          this.issueForm['imgfirst']=response.message
        else if(this.issueForm['imgsecond']==null)
          this.issueForm['imgsecond']=response.message
        else if(this.issueForm['imgthird']==null)
          this.issueForm['imgthird']=response.message
        this.num+=1;
        if(this.num === fileList.length)
        {
          this.uploadFinish=true;
        }

      },
      submitAll(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.filelist.length === 0)
              this.$message({
                message:'至少需要上传一张图片',
                type:'warning'
              })

            else if(this.imgchanged === false)
            {
              this.uploadFinish =true;
            }
            else
            {
              //TODO:向后台发送整个表单的信息和图片的链接
              this.$refs.upLoad.submit()
            }

          } else {
            this.$message({
              message:'发布失败!请按规则填好信息',
              type:'error'
            })
            return false;
          }
        });


      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },

      resetForm(formName) {
        //双清
        this.num=0;//这里代表已经选择的图片数
        this.$refs.upLoad.clearFiles();
        this.$refs[formName].resetFields();
      },
    },
    created(){
      let isLogin = sessionStorage.getItem('isLogin');
      this.graduated = sessionStorage.getItem('Graduated');
      if (isLogin !== '1') {
        this.$router.push('/login');
      }
      if(this.issueTag == 'issue') { }
      else
      {
        const _this =this
        axios.get(this.GLOBAL.host+'goods/info/'+this.issueTag).then((res)=>{
          _this.issueForm=res.data
          if(_this.issueForm.imgfirst){
            _this.filelist.push({name:'imgfirst',url:_this.issueForm.imgfirst})
            _this.num++
          }
          if(_this.issueForm.imgsecond) {
            _this.filelist.push({name:'imgsecond',url:_this.issueForm.imgsecond})
            _this.num++
          }
          if(_this.issueForm.imgthird){
            _this.filelist.push({name:'imgthird',url:_this.issueForm.imgthird})
            _this.num++
          }
        })
      }
    }

  }
</script>

<style scoped>
  #issue_tag{
    background-color: #545c64;
    text-align: center;
    width: 15%;
    height: 60px;
    color:white;
    border-radius: 30px;
  }
  .el_input{
    width: 300px;
  }
.el-form-item{
  float:left;
  margin-right: 60px;
}
</style>
