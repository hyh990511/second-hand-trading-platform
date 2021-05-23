<template>
    <div id="content" >
        <div>
            <div id="header">
                <div id="header_left">
                    <div>
                        <el-tooltip class="item" effect="dark" content="标题" placement="bottom">
                            <span>{{goodsData.title}}</span>
                        </el-tooltip>
                    </div>
                </div>
                <div id="header_right">
                    <div class="el-icon-user">
                        <el-tooltip class="item" effect="dark" content="卖家昵称" placement="bottom">
                            <span>{{goodsData.nickname}}</span>
                        </el-tooltip>
                    </div>
                    <div class="el-icon-time">
                        <el-tooltip class="item" effect="dark" content="发布时间" placement="bottom">
                            <span>发布于{{goodsData.realaseTime}}</span>
                        </el-tooltip>
                    </div>
                </div>
            </div>
            <div>
                <el-tooltip class="item" effect="dark" content="商品名称" placement="bottom">
                    <span>{{goodsData.name}}</span>
                </el-tooltip>
            </div>
            <div id="credit">
                <div>
                    <el-tooltip class="item" effect="dark" content="信用评级" placement="bottom">
                        <el-rate
                                v-model="creditRating"
                                disabled
                                show-score
                                text-color="#ff9900"
                                score-template="{value}">
                        </el-rate>
                    </el-tooltip>
                </div>
            </div>
            <div id="goods_text">
                <el-tooltip class="item" effect="dark" content="商品描述" placement="bottom">
                    <span>{{goodsData.text}}</span>
                </el-tooltip>
            </div>
            <div id="goods_img">
                <el-image v-for="url in urls" :key="url" :src="url" lazy></el-image>
            </div>
            <div v-if="goodsData.ascription != 'books'" class="goods_price">
                <i class="icon-price2 iconfont"></i>
                <el-tooltip class="item" effect="dark" content="价格" placement="bottom">
                    <span style="color: #ff4757">{{goodsData.goodsPrice}}</span>
                </el-tooltip>
                rmb
            </div>
            <div v-if="goodsData.ascription == 'books'" class="goods_price"><i class="el-icon-notebook-2"></i>想要交换的书：<span style="color: #ff4757">{{goodsData.exchangeBook}}</span></div>
            <div id="img_bottom">
                <div>
                    <i class="icon-fenlei-copy iconfont"></i>
                    <el-tooltip class="item" effect="dark" content="类别" placement="bottom">
                        <span v-if="goodsData.ascription != 'books'">{{goodsData.tag}}</span>
                        <span v-else>{{goodsData.childtag}}</span>
                    </el-tooltip>
                </div>
                <div>
                    <span v-show="goodsData.issale==1 ? true: false" style="color: #F56C6C">已售出!</span>
                    <el-button type="warning" icon="el-icon-shopping-cart-2" round :disabled="goodsData.isSale != 0 || goodsData.isExamine != 1"
                               @click="purchase">购买/交换</el-button>
                    <el-button type="warning" icon="el-icon-warning-outline" round :disabled="goodsData.isSale != 0 || goodsData.isExamine != 1"
                               @click="goodsReport(goodsId)">举报</el-button>
                </div>
            </div>
            <!--评论区-->
            <div id="comment">
                <!--发表评论表单-->
                <div id="comment_input">
                    <el-form :model="commentForm" :rules="rules" ref="commentForm" label-width="100px" class="form-inline">
                        <el-row >
                            <el-col :span="2">
                                <el-tag
                                        :key="tag.name"
                                        :type="tag.type"
                                        closable
                                        @close="replyLz">
                                    {{tag.name}}
                                </el-tag>
                            </el-col>
                            <el-col :span="16">
                                <el-form-item prop="text">
                                    <el-input ref="comment_textarea" type="textarea" :rows="4" v-model="commentForm.text"
                                              show-word-limit maxlength="200" placeholder="发表你的评论">
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item>
                                    <el-button type="primary" :disabled="goodsData.isSale != 0 || goodsData.isExamine != 1" @click="commentSubmit('commentForm')">发起评论</el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
                <!--已存在的评论-->
                <div id="comment_exist">
                    <div id="no_comment" v-show="comment.length == 0">
                        <span>暂无评论</span>
                    </div>
                    <div v-for="item in comment" class="comment_content">
                        <div class="comment_info">
                            <div id="comment_nickname" @click="info(item.userID)">
                                <el-avatar :src=item.images></el-avatar>
                                {{item.nickname}}
                            </div>
                            <div id="comment_time" style="float: right">
                                {{item.releaseTime}}
                            </div>
                        </div>
                        <div class="comment_text" >
                            <span>{{item.text}}</span>
                        </div>
                        <div class="comment_operation">
                            <el-tooltip class="item" effect="dark" content="回复" placement="top-start">
                                <el-button type="info" icon="el-icon-s-comment" content="回复" @click="reply(item.nickname,item.userID)" circle></el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" content="举报" placement="top-start">
                                <el-button type="warning" icon="el-icon-warning" @click="report(item.commentID)" circle></el-button>
                            </el-tooltip>
                        </div>
                    </div>
                    <!--评论分页-->
                    <el-pagination
                            background
                            layout="prev, pager, next"
                            :page-size=pageSize
                            :total="total"
                            :current-page="curPage+1"
                            @current-change="page">
                    </el-pagination>
                    <!--购买表单-->
                    <el-dialog title="提交订单"  :visible.sync="purchaseFormVisible" :append-to-body="true">
                        <el-form :model="purchaseForm" :rules="rules" ref="purchaseForm">
                            <el-form-item label="送货方式" prop="sendway">
                                <el-select v-model="purchaseForm.sendway" placeholder="请选择送货方式"  @change="selectChange">
                                    <el-option label="自取" value=0></el-option>
                                    <el-option label="卖家送" value=1></el-option>
                                </el-select>
                            </el-form-item>
                            <el-row :gutter="20">
                                <el-col :span="20">
                                    运费：<span style="color: #ff4757">{{sendPrice}}rmb</span>（注：选择自提则不需运费）
                                </el-col>
                            </el-row>
                            <el-row :gutter="20" v-if="goodsData.ascription != 'books'">
                                <el-col :span="20">
                                    商品价格：<span style="color: #ff4757">{{goodsData.goodsPrice}}rmb</span>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20" v-if="goodsData.ascription == 'books'">
                                <el-col :span="20">
                                    想要交换的书：<span style="color: #ff4757">{{goodsData.exchangeBook}}</span>
                                </el-col>
                            </el-row>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="purchaseFormVisible = false">取 消</el-button>
                            <el-button type="primary" @click="purchaseSubmit('purchaseForm')">确 定</el-button>
                        </div>
                    </el-dialog>
                    <!--举报商品表单-->
                    <el-dialog title="举报商品" :visible.sync="goodsReportFormVisible" :append-to-body="true">
                        <el-form :model="goodsReportForm" :rules="reportRules" ref="goodsReportForm">
                            <el-form-item label="原因：" prop="text">
                                <el-input ref="goods_report_input" type="textarea" :rows="4" v-model="goodsReportForm.text" placeholder="请填写举报原因" ></el-input>
                            </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="goodsReportCancel">取 消</el-button>
                            <el-button type="primary" @click="goodsReportSubmit('goodsReportForm')">确 定</el-button>
                        </div>
                    </el-dialog>
                    <!--举报评论表单-->
                    <el-dialog title="举报评论" :visible.sync="reportFormVisible" :append-to-body="true">
                        <el-form :model="reportForm" :rules="reportRules" ref="reportForm">
                            <el-form-item label="原因：" prop="text">
                                <el-input ref="report_input" type="textarea" :rows="4" v-model="reportForm.text" placeholder="请填写举报原因" ></el-input>
                            </el-form-item>

                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="reportCancel">取 消</el-button>
                            <el-button type="primary" @click="reportSubmit('reportForm')">确 定</el-button>
                        </div>
                    </el-dialog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "GoodsDetails",
        data() {
            return {
                tag: { name: '回复楼主：', type: 'success' },

                pageSize: 10,
                total: null,
                curPage: 0,

                sendPrice: 0,   //运费

                purchaseFormVisible:false,
                purchaseForm:{
                    sendway: null,
                    goodsid: null,  //商品id,
                },

                //商品举报
                goodsReportFormVisible: false,
                goodsReportForm: {
                  text: '',
                  goodsid: this.goodsId,
                },
                //评论举报
                reportFormVisible: false,
                reportForm:{
                    text:'',
                    commentid: null,
                },

                commentForm: {
                    text: '',
                    goodsid: null,
                },
                comment:[
                    // {
                    //     userID:1,
                    //     comment_id: 1,
                    //     date: "2020-05-23 21:52",
                    //     nickname: "浪里小白龙",
                    //     text: "我感觉这个商品物超所值，爱了爱了，就是价格有点伤不起哈，等下次攒够了钱再说！",
                    //     goodsid: 3,
                    // },
                    // {
                    //     userID:2,
                    //     comment_id: 2,
                    //     date: "2020-05-22 11:32",
                    //     nickname: "三目童子",
                    //     text: "都别抢，让我来！",
                    //     goodsid: 3,
                    // },
                    // {
                    //     userID:3,
                    //     comment_id: 3,
                    //     date: "2020-05-22 13:34",
                    //     nickname: "东海三太子",
                    //     text: "回复三目童子：在你买之前，我已经买了！",
                    //     goodsid: 3,
                    // },
                ],
                rules: {
                    text: [
                        { required: true, message: '评论不能为空', trigger: 'blur' }
                    ],
                    sendway:[
                        { required: true, message: '送货方式必选', trigger: 'change' }
                    ],
                },
                reportRules:{
                    text: [
                        { required: true, message: '举报原因不能为空', trigger: 'blur' }
                    ],
                },
                creditRating: null, //信用级别
                report_who: null,   //被举报者的账号
                reply_who: null,    //被回复的人，回复楼主则为null
                urls: [],       //商品图片链接
                goodsData:{},   //商品相关信息
                userId: null,   //登录用户ID
                goodsId: null,  //商品ID
            }
        },
        methods:{
            purchaseSubmit(formName){
                const that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //提交购买数据交互
                        axios.post(this.GLOBAL.host+'goods/buy/'+that.userId,
                            that.purchaseForm).then(function (resp) {
                            if(resp.data == 'success'){
                                that.$alert('购买成功', '提示', {
                                    confirmButtonText: '确定'
                                });
                                //_this.$router.push('/u_info'); 等待刷新页面
                            } else if (resp.data == 'failed') {
                                that.$alert('购买失败，不可以购买自己发布的商品', '提示', {
                                    confirmButtonText: '确定'
                                });
                            } else {
                                that.$alert('购买失败，请稍后再试', '提示', {
                                    confirmButtonText: '确定'
                                });
                            }
                            //刷新
                            that.$router.push({
                                path: '/refresh',
                                query:{
                                    destination: '/GoodsDetails?goodsID='+that.goodsId,
                                }
                            })
                        })

                        that.purchaseFormVisible = false;

                    } else {
                        return false;
                    }
                });
            },
            purchaseCancel(){
                // this.purchaseForm.sendway = '';
                this.purchaseFormVisible = false;
            },
            purchase(){
                this.purchaseForm.sendway = null;
                this.purchaseForm.goodsid = this.goodsId;
                this.purchaseFormVisible = true;
            },
            commentSubmit(formName) {
                const that = this;
                this.commentForm.goodsid = this.goodsId;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //alert('submit!'+that.commentForm);
                        if(this.reply_who != null){
                            this.commentForm.text = this.tag.name+':\n'+this.commentForm.text;
                        }
                        // console.log(that.commentForm);
                        // 提交评论数据交互
                        //评论楼主（卖家）
                        if(this.reply_who == null){
                            axios.post(this.GLOBAL.host+'user/comment/deliver/'
                                + that.userId, that.commentForm).then(function (resp) {
                                if(resp.data === 'success'){
                                    that.$message({
                                        type: 'success',
                                        message: `评论成功！`
                                    });
                                    //刷新
                                    that.$router.push({
                                        path: '/refresh',
                                        query:{
                                            destination: '/GoodsDetails?goodsID='+that.goodsId,
                                        }
                                    })
                                }else if(resp.data === 'Unqualified'){
                                    that.$message({
                                        type: 'warning',
                                        message: `非法评论！`
                                    });
                                    that.commentForm.text = '';
                                }else if(resp.data === 'error'){
                                    that.$message({
                                        type: 'warning',
                                        message: `评论失败，请稍后再试！`
                                    });
                                }
                            })
                        }else{  //评论层主（回复评论区已有评论）
                            axios.post(this.GLOBAL.host+'user/comment/reply/' +
                                that.userId+'/'+that.reply_who,that.commentForm)
                                .then(function (resp) {
                                if(resp.data === 'success'){
                                    that.$message({
                                        type: 'success',
                                        message: `回复成功！`
                                    });
                                    //刷新
                                    that.$router.push({
                                        path: '/refresh',
                                        query:{
                                            destination: '/GoodsDetails?goodsID='+that.goodsId,
                                        }
                                    })
                                }else if(resp.data === 'Unqualified'){
                                    that.$message({
                                        type: 'warning',
                                        message: `非法回复！`
                                    });
                                    that.commentForm.text = '';
                                }else if(resp.data === 'error'){
                                    that.$message({
                                        type: 'warning',
                                        message: `回复失败，请稍后再试！`
                                    });
                                }
                            })
                        }
                    } else {
                        return false;
                    }
                });
            },
            reply(nickname,userID){
                //alert(userID);
                //this.commentForm.text = '@'+nickname+':\n';
                this.tag.name = '回复'+nickname+'：';
                this.reply_who = userID;
                this.$refs['comment_textarea'].focus();
            },
            replyLz(){
              this.tag.name = '回复楼主：';
              this.reply_who = null;
              //this.commentForm.text = '';
            },
            goodsReport(g_id) {
                const that = this;
                that.goodsReportForm.text = '';
                that.goodsReportFormVisible = true;
                that.goodsReportForm.goodsid = g_id;
                that.$nextTick(() => { // 2. 弹框显示DOM更新完成后 获取refs.ref1 设置焦点
                    that.$refs['goods_report_input'].focus(); // 设置焦点
                })
            },

            goodsReportSubmit(formName) {
                const that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // console.log(this.reportForm.commentId);
                        // console.log(this.reportForm.text);
                        //举报评论交互
                        axios.post(this.GLOBAL.host+'user/goods/inform/' +
                            that.userId, that.goodsReportForm).then(function (resp) {
                            if(resp.data === 'success'){
                                that.$message({
                                    type: 'success',
                                    message: `举报成功！`
                                });
                                //刷新
                                that.$router.push({
                                    path: '/refresh',
                                    query:{
                                        destination: '/GoodsDetails?goodsID='+that.goodsId,
                                    }
                                })
                            }
                        })

                        that.goodsReportFormVisible = false;
                    } else {
                        return false;
                    }
                });
            },

            goodsReportCancel() {
                this.goodsReportFormVisible = false;
            },

            report(comment_id) {
                const that = this;
                that.reportForm.text = '';
                that.reportForm.commentid = comment_id;
                that.reportFormVisible = true;
                that.$nextTick(() => { // 2. 弹框显示DOM更新完成后 获取refs.ref1 设置焦点
                    that.$refs['report_input'].focus(); // 设置焦点
                })
            },
            reportSubmit(formName) {
                const that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // console.log(this.reportForm.commentId);
                        // console.log(this.reportForm.text);
                        //举报评论交互
                        axios.post(this.GLOBAL.host+'user/comment/inform/' +
                            that.userId, that.reportForm).then(function (resp) {
                            if(resp.data === 'success'){
                                that.$message({
                                    type: 'success',
                                    message: `举报成功！`
                                });
                                //刷新
                                that.$router.push({
                                    path: '/refresh',
                                    query:{
                                        destination: '/GoodsDetails?goodsID='+that.goodsId,
                                    }
                                })
                            }
                        })

                        that.reportFormVisible = false;
                    } else {
                        return false;
                    }
                });
            },
            reportCancel(){
                this.reportFormVisible = false;
            },
            info(userID){
                //console.log(userID);
                //跳转查看信息页
                this.$router.push({
                    path: '/show_info',
                    query:{
                        userID: userID,
                        goodsID: this.goodsId,
                    }
                })
            },
            page(currentPage){
                // alert(currentPage);
                this.curPage = currentPage - 1;
                const that = this;
                axios.get(this.GLOBAL.host+'user/comment/list/'+this.goodsId+'/'+(this.curPage+1)+'/'+this.pageSize).then(function(resp){
                    //console.log(resp);
                    that.comment = resp.data.content;
                })
            },
            selectChange(value){
                //console.log(value);
                if(value == '0'){
                    this.sendPrice = 0;
                }else{
                    this.sendPrice = this.goodsData.sendPrice;
                }
            },
        },
        created(){
            const that = this;
            this.goodsId = this.$route.query.goodsID;
            this.userId = sessionStorage.getItem('accountNumber');
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
            if( this.goodsId !== null){
                //获取商品信息
                axios.get(this.GLOBAL.host+'goods/info/'+this.goodsId)
                    .then(function(resp) {
                        console.log(resp)
                    that.goodsData = resp.data;
                    that.creditRating = parseFloat(that.goodsData.creditRating);
                    if(resp.data.imgfirst !== null){
                        that.urls.push(resp.data.imgfirst);
                    }
                    if(resp.data.imgsecond !== null){
                        that.urls.push(resp.data.imgsecond);
                    }
                    if(resp.data.imgthird){
                        that.urls.push(resp.data.imgthird);
                    }
                })

                //获取评论区信息
                axios.get(this.GLOBAL.host+'user/comment/list/'+this.goodsId+
                    '/'+(this.curPage+1)+'/'+this.pageSize).then(function(resp) {
                    that.comment = resp.data.content;
                    that.total = resp.data.totalElements;
                })
            }
        }
    }
</script>

<style scoped>
    @import "https://at.alicdn.com/t/font_1791888_fewuhh9hrgf.css";
    @import "https://at.alicdn.com/t/font_1791888_43sjcxv7o7i.css";
    #content{
        width: 1200px;
        background-color: #ffffff;
        margin: 0 auto;
    }
    /*#no_goods{*/
    /*    margin: 0 auto;*/
    /*}*/
    #header{
        width: 1200px;
        height: 50px;
    }
    #header_left{
        width: 400px;
        height: 50px;
        padding: 0 50px;
        text-align:left;
        float: left;
    }
    #header_left>div{
        line-height: 50px;
    }
    #header_left>div>span{
        font-weight: bold;
    }
    #header_right{
        width: 300px;
        height: 50px;
        text-align: center;
        float: right;
    }
    #header_right>div:nth-child(1){
        width: 120px;
        float: left;
        line-height: 50px;
    }
    #header_right>div:nth-child(2){
        width: 170px;
        font-size: 4px;
        float: left;
        line-height: 50px;
    }
    #credit{
        clear: both;
        height: 50px;
        margin-top: 30px;
    }
    #credit>div:nth-child(1){
        padding-left: 50px;
        height: 50px;
        float: left;
    }
    #credit>div:nth-child(2){
        width: 300px;
        line-height: 50px;
        float: right;
    }
    #goods_text{
        text-align:left;
        text-indent: 2em;
        padding: 0 50px;
        line-height: 30px;
    }
    #goods_text>span{
        font: 18px/1 Tahoma,Helvetica,Arial,"\5b8b\4f53",sans-serif;
    }
    #goods_img{
        clear: both;
        height: 300px;
        width: 500px;
        margin: 0 auto;
        line-height: 20px;
        overflow-y:scroll;
        box-shadow: -10px 10px 5px #888888;
        border: 20px solid #f1f2f6;
    }
    .goods_price{
        margin-top: 50px;
        line-height: 1px;
    }
    #img_bottom{
        margin-top: 50px;
    }
    #img_bottom>div:nth-child(1){
        width: 300px;
        height: 50px;
        float: left;
        line-height: 50px;
    }
    #img_bottom>div:nth-child(2){
        width: 300px;
        height: 50px;
        float: right;
        line-height: 50px;
    }
    #comment{
        position: -webkit-sticky;
        /*position: -moz-sticky;*/
        /*position: -ms-sticky;*/
        position: sticky;
        top: 0;
        z-index: 999;
        clear: both;
        border: 10px solid #f1f2f6;
        height: 600px;
        overflow-y:scroll;
    }
    #comment_input{
        position: -webkit-sticky; /* Safari */
        position: sticky;
        top: 0;
    }
    #no_comment{
        font: 18px/1 Tahoma,Helvetica,Arial,"\5b8b\4f53",sans-serif;
    }
    #comment_exist{
        /*font: 18px/1 Tahoma,Helvetica,Arial,"\5b8b\4f53",sans-serif;*/
    }
    .comment_content{
        border: solid 1px #747d8c;
        border-radius:10px;
        overflow:hidden;
        width: 90%;
        margin: 0 auto 20px;
        background-color: #ced6e0;
    }
    .comment_content:hover{
        transform:scale3d(1.1,1.1,1.2);
        transition:all 0.8s;
        box-shadow: 10px 10px 10px #034663;
        z-index:2;
    }
    .comment_info{
        font:12px Small;
        font-weight: bold;
    }
    #comment_nickname{
        cursor:pointer;
        float: left;
    }
    #comment_time{
        float: right;
    }
    .comment_text{
        font: 16px/1 Tahoma,Helvetica,Arial,"\5b8b\4f53",sans-serif;;
        clear: both;
        text-align:left;
        text-indent: 2em;
    }
    .comment_operation{
        float: right;
    }
</style>
