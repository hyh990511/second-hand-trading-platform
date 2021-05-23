<template>
    <div class="register">
        <div class="stepArea">
            <el-steps :active="active" finish-status="success"  align-center>
                <el-step title="验证邮箱"></el-step>
                <el-step title="密码设置"></el-step>
                <el-step title="信息填写"></el-step>
            </el-steps>
        </div>
        <div class="formArea">
            <div class="emailInfo" v-if="active === 0">
                <div style="text-align: center;
                            margin: 80px 0px">
                    <span>我们将通过学校邮箱来验证您是否为在校生</span>
                </div>
                <el-form :model="emailForm"
                         status-icon :rules="emailRules"
                         ref="emailForm"
                         label-width="100px">
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="12">
                            <el-form-item label="邮箱" prop="stuID">
                                <el-input v-model="emailForm.stuID"
                                          prefix-icon="el-icon-message"
                                          placeholder="请输入学号"
                                          maxlength="10"
                                          show-word-limit
                                          style="width:80%">
                                    <template slot="append">@fzu.edu.cn</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="12">
                            <el-form-item label="验证码" prop="check">
                                <el-input v-model="emailForm.check"
                                          maxlength="6"
                                          placeholder="请输入验证码"
                                          @keyup.enter.native="submitCheck('emailForm')"
                                          style="width: 122px;
                                          margin-right: 30px">
                                </el-input>
                                <el-button type="primary"
                                           native-type="button"
                                           :disabled="isDisable"
                                           @click="submitEmail('emailForm')">{{content}}
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="12">
                            <el-form-item>
                                <el-button type="primary"
                                           @click="submitCheck('emailForm')"
                                           style="width: 40%;">注册
                                </el-button>
                                <el-button @click="resetForm('emailForm')"
                                           style="width: 40%;">重置
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="passInfo" v-if="active === 1">
                <div style="text-align: center;
                            margin: 80px 0px">
                    <span>请输在下方设置您的的登陆密码</span>
                </div>
                <el-form :model="passForm"
                         status-icon :rules="passRules"
                         ref="passForm"
                         label-width="100px">
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="10">
                            <el-form-item label="密码" prop="password">
                                <el-input type="password"
                                          v-model="passForm.password"
                                          auto-complete="off"
                                          @keyup.enter.native="toSetInfo('passForm')"
                                          style="width: 70%;">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="10">
                            <el-form-item label="确认密码" prop="checkPass">
                                <el-input type="password"
                                          v-model="passForm.checkPass"
                                          auto-complete="off"
                                          @keyup.enter.native="toSetInfo('passForm')"
                                          style="width: 70%;"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="8">
                            <el-form-item>
<!--                                <el-button type="primary" @click="finishSub('passForm')" style="width: 40%;">下一步</el-button>-->
                                <el-button type="primary"
                                           @click="toSetInfo('passForm')"
                                           style="width: 70%;">下一步
                                </el-button>
<!--                                <el-button @click="pre('passForm')" style="width: 40%;">上一步</el-button>-->
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="userInfo" v-if="active === 2" >
                <el-form :model="userForm"
                         :rules="userRules"
                         ref="userForm"
                         label-width="100px"
                         style="margin-left: 50px">
                    <el-row >
                        <el-col :span="10">
                            <el-form-item label="昵称" prop="nickname">
                                <el-input class=m_info
                                          v-model="userForm.nickname"
                                          style="width: 100%">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item label="姓名" prop="name">
                                <el-input class=m_info
                                          v-model="userForm.realname"
                                          maxlength="15"
                                          show-word-limit>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg">
                        <el-col :span="10">
                            <el-form-item label="性别" prop="gender">
                                <el-radio-group v-model="userForm.gender"
                                                style="float: left;
                                                padding-top: 13px">
                                    <el-radio label="男"></el-radio>
                                    <el-radio label="女"></el-radio>
                                    <el-radio label="保密"></el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg">
                        <el-col :span="10">
                            <el-form-item label="手机号码" prop="phone">
                                <el-input class=m_info
                                          v-model="userForm.phone">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg">
                        <el-col :span="10">
                            <el-form-item label="常用邮箱" prop="email">
                                <el-input class=m_info
                                          maxlength="40"
                                          show-word-limit
                                          v-model="userForm.email">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item label="QQ" prop="QQ">
                                <el-input class=m_info
                                          maxlength="10"
                                          show-word-limit
                                          v-model="userForm.qqnumber">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" >
                        <el-col :span="15">
                            <el-form-item label="收货地址" prop="address">
                                <el-input class=m_info
                                          maxlength="50"
                                          show-word-limit
                                          v-model="userForm.address">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg">
                        <el-col :span="18">
                            <el-form-item label="个性签名" prop="signature">
                                <el-input class=m_info
                                          type="textarea"
                                          maxlength="40"
                                          show-word-limit
                                          v-model="userForm.introduction">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="8">
                            <el-form-item>
                                <el-button @click="pre"
                                           style="width: 40%;">上一步
                                </el-button>
                                <el-button type="primary"
                                           @click="finishSub('userForm')">确认
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "Register",
        data() {
            let Pass = (rule, value, callback) => {
                if (value == null) {
                    callback(new Error("请输密码"));
                } else if (value.length < 6 || value.length > 18) {
                    callback(new Error("密码长度为6-18位"));
                } else {
                    callback();
                }
            };
            let Rewrite = (rule, value, callback) => {
                if (value == null) {
                    callback(new Error("请再次输入密码"));
                } else if (value !== this.passForm.password) {
                    callback(new Error("两次输入密码不一致!"));
                } else {
                    callback();
                }
            };
            let checkPhone = (rule, value, callback) => {
                if (value == null) {
                    callback(new Error("请输入手机号"));
                } else {
                    const reg = /^((13[0-9])|(14[5-9])|(15[0-3])|(15[5-9])|(166)|(17[0-8])|(18[0-9])|(19[8-9]))\d{8}$/;
                    if (reg.test(value)) {
                        callback()
                    } else {
                        return callback(new Error('请输入正确的手机号'));
                    }
                }
            };
            return {
                form:[],

                // 倒计时按钮相关
                content: '获取验证码',
                totalTime: 60,  // 除了显示在按钮上，没有实际用处
                isDisable: false,

                authCode: [],   // 存放接口返回的加密验证码

                active: 0,
                emailForm: {
                    stuID: '',
                    check: ''
                },
                userForm: {
                    accountnumber: null,
                    password: null,
                    nickname: null,
                    gender: null,
                    realname: null,
                    email: null,
                    phone: null,
                    qqnumber: null,
                    address: null,
                    introduction: null,
                    creditrating: 3.0,
                    images: 'https://cube.elemecdn.com/3/7c' +
                        '/3ea6beec64369c2642b92c6726f1epng.png',
                },
                passForm: {
                    password: null,
                    checkPass: null,
                },
                emailRules: {
                    stuID: [
                        { required: true, message: '请输入学校邮箱', trigger: 'blur' }
                    ],
                    check: [
                        { required: false }
                    ]
                },
                userRules: {
                    nickname: [
                        { required: true, message: '请输入昵称', trigger: 'blur' },
                        { min: 1, max: 8, message: '长度在1到8个字符',
                            trigger: 'blur' }
                    ],
                    realname: [
                        { required: false }
                    ],
                    gender: [
                        { required: true, message: '请选性别', trigger: 'blur' }
                    ],
                    phone: [
                        // { required: true, message: '请填写联系方式', trigger: 'blur' },
                        { required: true, validator: checkPhone, trigger: 'blur' },
                    ],
                    email: [
                        { required: false }
                    ],
                    qqnumber: [
                        { required: false }
                    ],
                    address: [
                        { required: true, message: '请填写校内收货地址',
                            trigger: 'blur' }
                    ],
                    introduction: [
                        { required: false }
                    ]
                },
                passRules: {
                    password: [
                        { required: true, validator: Pass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { required: true, validator: Rewrite, trigger: "blur"}
                    ]
                },
            }
        },
        methods: {
            pre(formName) {
                this.active--
            },

            submitEmail(formName) {
                // 按钮倒计时事件
                if (this.isDisable) return ;
                this.isDisable = true;
                this.content = this.totalTime + 's后重新获取';
                let clock = window.setInterval(() => {
                    this.totalTime--;
                    this.content = this.totalTime + 's后重新获取';
                    if (this.totalTime < 0) {
                        window.clearInterval(clock);
                        this.content = '重新获取验证码';
                        this.totalTime = 60;
                        this.isDisable = false;  //这里重新开启
                    }
                },1000) // 每次一秒

                this.$refs[formName].validate((valid) => {
                    let _this = this;
                    if (valid) {
                        // 发送邮箱给后台以发送验证码
                        axios.post(_this.GLOBAL.host+'verify/email/0/'
                            + _this.emailForm.stuID).then(function (resp) {
                            if (resp.data == 'account is occupied') {
                                _this.$alert('该邮箱已被注册，请使用其他邮箱进行注册', '提示', {
                                    confirmButtonText: '确定',
                                });
                            } else if (resp.data == 'error') {
                                _this.$alert('获取验证码失败', '提示', {
                                    confirmButtonText: '确定',
                                });
                            } else {
                                _this.authCode = resp.data;
                                _this.$alert('获取成功，请勿关闭页面，并将发送至“'
                                    +_this.emailForm.stuID+'@fzu.edu.cn”' +
                                    '的验证码填入框中', '提示', {
                                    confirmButtonText: '确定',
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },

            submitCheck(formName) {
                if(this.emailForm.check != '') {
                    this.$refs[formName].validate((valid) => {
                        let _this = this;
                        if (valid) {
                            axios.post(_this.GLOBAL.host+'verify/code/'
                                + _this.emailForm.check, _this.authCode)
                                .then(function (resp) {
                                    if (resp.data == 'success') {
                                        _this.active++;
                                    } else if (resp.data == 'failed') {
                                        _this.$alert('验证码错误，请重新填写！', '提示', {
                                            confirmButtonText: '确定',
                                            type: 'warning',
                                        });
                                    } else {
                                        _this.$alert('发生未知错误，请稍后再试', '提示', {
                                            confirmButtonText: '确定',
                                            type: 'error',
                                        });
                                    }
                            })
                        } else {
                            return false;
                        }
                    })
                } else {
                    this.$alert('验证码不能为空', '提示', {
                        confirmButtonText: '确定',
                    });
                }
            },

            toSetInfo(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.active++
                    } else {
                        return false;
                    }
                });
            },

            finishSub(formName) {
                this.userForm.accountnumber = this.emailForm.stuID;
                this.userForm.password = this.passForm.password;

                this.form.push(this.userForm);
                this.form.push(this.authCode);

                this.$refs[formName].validate((valid) => {
                    let _this = this;
                    if (valid) {
                        // 发送注册信息给后台创建新用户
                        axios.post(_this.GLOBAL.host+'user/register/'+
                            _this.emailForm.check+ '/'+_this.authCode.hash+'/' +
                            _this.authCode.tamp,_this.userForm).then(function (resp) {
                            if (resp.data == 'failed') {   // 后台错误
                                _this.$alert('服务器出错，请稍后再试！', '警告', {
                                    confirmButtonText: '确定',
                                    type: 'warning'
                                });
                            } else if (resp.data == 'success') {
                                _this.$alert('注册成功！您的帐号为：'
                                    + _this.userForm.accountnumber, '提示', {
                                    confirmButtonText: '确定',
                                }).then(() => {
                                    _this.$router.push('/login');
                                }).catch(() => {

                                });
                            } else {
                                _this.$alert('出现未知错误，请稍后再试！', '警告', {
                                    confirmButtonText: '确定',
                                    type: 'error'
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                })
            },

            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    .register {
        width: 1200px;
        margin: 0 auto;
    }
    .stepArea {
        margin: 20px 0px;
    }
    .formArea {
        width: 1000px;
        margin: 50px auto;
    }
</style>