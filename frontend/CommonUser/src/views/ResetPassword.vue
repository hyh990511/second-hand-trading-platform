<template>
    <div class="register">
        <div class="stepArea">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="验证邮箱"></el-step>
                <el-step title="密码重置"></el-step>
            </el-steps>
        </div>
        <div class="formArea">
            <div class="emailInfo" v-if="active === 0">
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
                                          @keyup.enter.native=
                                                  "submitCheck('emailForm')"
                                          style="width: 122px;
                                                margin-right: 30px"></el-input>
                                <!--                                TODO：获取验证码按钮设个点击时间           -->
                                <el-button type="primary"
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
                                           style="width: 40%;">确认</el-button>
                                <el-button @click="resetForm('emailForm')"
                                           style="width: 40%;">重置</el-button>
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
                                          @keyup.enter.native=
                                                  "finishSub('passForm')"
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
                                          @keyup.enter.native=
                                                  "finishSub('passForm')"
                                          style="width: 70%;"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex" class="row-bg" justify="center">
                        <el-col :span="8">
                            <el-form-item>
                                <el-button type="primary"
                                           @click="finishSub('passForm')"
                                           style="width: 70%;">重置</el-button>
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
        name: "ResetPassword",
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
            return {
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
                        axios.post(_this.GLOBAL.host+'verify/email/1/'
                            + _this.emailForm.stuID).then(function (resp) {
                            if (resp.data == 'account is not exist') {
                                _this.$alert('账号不存在，请重新输入', '提示', {
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

            finishSub(formName) {
                this.userForm.accountnumber = this.emailForm.stuID;
                this.userForm.password = this.passForm.password;

                this.$refs[formName].validate((valid) => {
                    let _this = this;
                    if (valid) {
                        // 发送注册信息给后台创建新用户
                        axios.put(_this.GLOBAL.host+'user/resetPwd/' +
                            _this.emailForm.check+'/'+_this.authCode.hash +
                            '/'+_this.authCode.tamp,_this.userForm).then(function (resp) {
                            if (resp.data == 'failed') {
                                _this.$alert('服务器出错，请稍后再试', '警告', {
                                    confirmButtonText: '确定',
                                    type: 'error'
                                });
                            } else if (resp.data == 'success') {
                                _this.$alert('修改成功！', '提示', {
                                    confirmButtonText: '确定',
                                }).then(() => {
                                    _this.$router.push('/login');
                                }).catch(() => {

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
    .emailInfo {
        margin-top: 150px;
    }
</style>