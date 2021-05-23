<template>
    <div class=u_info style="padding: 10px 200px 0px 200px;">
        <div>
            <div style="width: 100%; float: left;">
                <div id="div3"
                     style="font-size: 18px;
                        float: left;
                        font-family:SourceHanSansSC;
                        font-weight:700;
                        font-size:25px;
                        color: #333333;
                        font-style:normal;
                        letter-spacing:0px;
                        line-height:20px;
                        text-decoration:none;"
                >{{ userInfo.nickname }}的个人信息</div>
            </div>
            <div  style="float: left;
                        padding-left: 40px;
                        padding-top: 50px;
                        height: 480px;
                        width: 40%" >
                <div style="padding: 200px 100px 50px 100px">
                    <el-avatar :size="100"
                               :src="userInfo.images"
                               style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12),
                                                   0 0 6px rgba(0, 0, 0, .04)">
                    </el-avatar>
                </div>

                <el-button style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04); float: top" @click="dialogVisible=true">上传头像</el-button>
            </div>

            <!--      上传头像弹出层      -->
            <el-dialog title="编辑头像"
                       :visible.sync="dialogVisible"
                       :before-close="handleClose">
                <div>
                    <el-row type="flex"
                            justify="center"
                            style="margin-bottom: 10px">
                        <el-col :span="24">
                            <div class="eleme">
                                <el-upload
                                        :action="this.GLOBAL.host +
                                                'QiNiu/uploadimg'"
                                        :class="{hide:hideUpload}"
                                        :before-upload="beforeUpload"
                                        :file-list="fileList"
                                        list-type="picture-card"
                                        :limit="limitCount"
                                        :on-preview="handlePreview"
                                        :on-success="handleSuccess"
                                        :on-change="onChange"
                                        :on-remove="onRemove"
                                        :auto-upload="false"
                                        ref="upLoad"
                                >
                                    <i class="el-icon-plus"></i>
                                    <div slot="tip" class="el-upload__tip">
                                        只能上传jpg/png/jpeg图片，且不超过500kb
                                    </div>
                                </el-upload>
                            </div>
                        </el-col>
                    </el-row>
                    <el-button style="margin-left: 10px;"
                               size="small"
                               type="success"
                               @click="submitUpload">确认设置
                    </el-button>
                    <el-button type="primary"
                               size="small"
                               @click="dialogVisible=false">取消
                    </el-button>
                </div>
            </el-dialog>

            <!--          分割线         -->
            <div style="float:left;
                        margin-top: 30px;
                        width: 1px;
                        height: 460px;
                        background: darkgray;"></div>

            <!--          个人信息展示区           -->
            <div style="float: left;
                        padding-top: 30px;
                        height: 500px;
                        width: 55%" >
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-collection-tag"></i>
                         昵称：{{ userInfo.nickname }}
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-user"></i>
                         账号：{{ userInfo.accountnumber }}
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-male"></i>
                         性别：{{ userInfo.gender }}
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-collection-tag"></i>
                         姓名：{{ userInfo.realname }}
                    </p>
                </div>
                <!--           分割线          -->
                <div style="float:left;
                            margin: 3px 100px 3px 20px;
                            width: 85%;
                            height: 1px;
                            background: darkgray;">
                </div>
                <div class="text" style="padding-left: 20px">
                    <p style="float: left;margin: 0; padding: 0">
                        <i class="el-icon-s-promotion"></i>
                         联系方式
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">邮箱：{{ userInfo.email }}</p>
                </div>
                <div class="text">
                    <p style="float: left">手机号：{{ userInfo.phone }}</p>
                </div>
                <div class="text">
                    <p style="float: left">QQ号：{{ userInfo.qqnumber }}</p>
                </div>
                <!--           分割线          -->
                <div style="float:left;
                            margin: 3px 100px 3px 20px;
                            width: 85%;
                            height: 1px;
                            background: darkgray;">
                </div>
                <div class="text" style="padding-left: 20px">
                    <p style="float: left;margin: 0; padding: 0"> 其它</p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-location-outline"></i>
                         收货地址(校内)：{{ userInfo.address }}
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-edit"></i>
                         个性签名：{{ userInfo.introduction }}
                    </p>
                </div>
                <div class="text">
                    <p style="float: left">
                        <i class="el-icon-star-off"></i> 信用等级：</p>
                    <div class="block" style="float: left; margin: 10px">
                        <el-rate
                                :colors="colors"
                                v-model="userInfo.creditrating"
                                disabled
                                show-score
                                text-color="#ff9900"
                                score-template="{value}">>
                        </el-rate>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <el-button type="primary" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)" @click="modifyPass">修改密码</el-button>
            <el-button type="primary" style="margin-left: 80px; box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)" @click="modifyInfo">修改信息</el-button>
        </div>

    </div>
</template>

<script>
    import axios from 'axios'
    import store from "../store/store.js";
    export default {
        data() {
            return {
                dialogVisible: false,
                limitCount:1,
                hideUpload: false,
                fileList: [],

                userInfo: [],
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'], // 等同于{ 2: '#99A9BF',
                        // 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
                accountnumber: null,
            };
        },
        created() {
            this.accountnumber = sessionStorage.getItem('accountNumber');
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
            const _this = this;
            axios.get(_this.GLOBAL.host+'/user/info/'+_this.accountnumber)
                .then(function (resp) {
                    // console.log(_this.GLOBAL.host+'/user/info/'+_this.accountnumber)
                    // console.log(resp.data);
                    _this.userInfo = resp.data;
            })
        },

        methods: {
            modifyPass() {
                this.$router.push('/m_pass');
            },
            modifyInfo() {
                this.$router.push('/m_info');
            },

            // 修改头像相关
            // 控制弹出层关闭
            handleClose(){
                this.dialogVisible = false;
            },

            submitUpload(file) {
                this.$refs.upLoad.submit();
            },

            handlePreview(file) {
                console.log(file);
            },

            // 上传前限制
            beforeUpload(file) {
                console.log(file);
                let Msg = file.name.substring(file.name.lastIndexOf('.') + 1);
                const isJPG = (Msg === 'jpg' || Msg === 'JPG');
                const isPNG = (Msg === 'png' || Msg === 'PNG');
                const isJPEG = (Msg === 'jpeg');
                const isLt2M = file.size / 1024 < 500;

                if (!isPNG && !isJPG && !isJPEG) {
                    this.$message.error('图片类型必须是jpeg,jpg,png中的一种');
                    return false;
                } else if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 500KB!');
                    return false;
                } else {
                    return true;
                }
            },
            // 上传列表有改变时
            onChange(file,fileList) {
                this.hideUpload = fileList.length >= this.limitCount;
            },
            // 移除图片后的变化
            onRemove(file,fileList) {
                this.hideUpload = fileList.length >= this.limitCount;
            },
            // 上传成功后的设置
            handleSuccess(response, file, fileList) {
                // 头像信息传给后端
                let _this = this;
                let temp = this.userInfo.images;
                this.userInfo.images = response.message;
                axios.put(_this.GLOBAL.host+'user/setting1', _this.userInfo)
                    .then(function (resp) {
                        if (resp.data == 'success') {
                            _this.$alert('修改成功', '提示', {
                                confirmButtonText: '确定'
                            });
                            sessionStorage.setItem('Images', response.message);
                            _this.$store.state.UserImages = response.message;
                            _this.dialogVisible = false;
                        } else {
                            this.userInfo.images = temp;
                            _this.$alert('修改失败,请重试', '提示', {
                                confirmButtonText: '确定'
                            });
                            _this.fileList = null;
                            _this.dialogVisible = false;
                        }
                })
            },

            showTest() {
                alert(this.userInfo.images);
            }
        },
    }
</script>

<style>
    /*    个人信息文本样式    */
    .text{
        width: 100%;
        float: left;
        padding-left: 80px;
    }
    .text > p{
        margin: 10px auto;
    }

    /*     隐藏图片上传框     */
    .hide .el-upload--picture-card {
        display: none;
    }

</style>