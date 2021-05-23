<template>
    <div id="page">
        <div class="Background">
            <div id="login">
                        <h2 style="text-align: center">用户登录</h2>
                        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                            <el-form-item label="账号" prop="accountnumber">
                                <el-input v-model="ruleForm.accountnumber"
                                          prefix-icon="el-icon-user-solid"
                                          placeholder="请输入学号"
                                          @keyup.enter.native="submitForm('ruleForm')">
                                </el-input>
                            </el-form-item>
                            <el-form-item label="密码" prop="password">
                                <el-input v-model="ruleForm.password"
                                          prefix-icon="el-icon-lock"
                                          show-password
                                          @keyup.enter.native="submitForm('ruleForm')">
                                </el-input>
                            </el-form-item>
                            <el-form-item style="margin-top: 40px">
                                <el-button type="primary"
                                           @click="submitForm('ruleForm')"
                                           style="width: 40%;">登录
                                </el-button>
                                <el-button @click="resetForm('ruleForm')"
                                           style="width: 40%;">重置
                                </el-button>
                            </el-form-item>
                        </el-form>
                        <div class="accountLink" style="text-align: right; margin-right: 20px">
                            <el-link type="primary" @click="register">注册</el-link>
                            <el-divider direction="vertical"></el-divider>
                            <el-link type="primary" @click="forgotPass">忘记密码</el-link>
                        </div>
                    </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data() {
            let validateUserName = (rule,value,callback)=>{
                if(value==='')
                {
                    callback(new Error('请输入账号'));
                }
                else
                {
                    callback();
                }
            }
            let validatePass = (rule, value, callback) => {
                if (value === '')
                {
                    callback(new Error('请输入密码'));
                }
                else
                {
                    callback();
                }
            };
            return {
                ruleForm: {
                    accountnumber: '',
                    password: '',
                },
                rules: {
                    accountnumber: [
                        { validator: validateUserName, trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                }
            };
        },

        created() {
            sessionStorage.setItem('isLogin', '0');
        },

        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    let _this = this;
                    if (valid) {
                        // 向后端发送登录信息请求验证
                        axios.post(_this.GLOBAL.host+'user/login/',
                            _this.ruleForm).then(function (resp) {
                            if (resp.data.message == 'Account does not exist') {
                                alert("该账号尚未注册!")
                            } else if (resp.data.message == 'Pw Error') {
                                alert("密码错误!")
                            } else if (resp.data.message == 'The user has been banned') {
                                alert("很抱歉，由于您已毕业/发布不当言论，导致该账号停用，如有异议，" +
                                      "请点击下方'联系我们'与管理员联系");
                            } else {
                                // 设置登录成功的标志
                                sessionStorage.setItem('accountNumber',
                                    _this.ruleForm.accountnumber);
                                sessionStorage.setItem('isLogin', '1');
                                localStorage.setItem('token',resp.data.token);

                                // 判定该用户是否为毕业班学生
                                let string = _this.ruleForm.accountnumber;
                                let sub = "20"+string.substring(2,4);   // 用户入学年份
                                let date = new Date();
                                let year = date.getFullYear() - 4;  // 毕业班入学年份
                                if (string.substring(0,2) == "15") {
                                    year--; // 建筑学院特殊处理
                                }
                                year = year.toString();
                                // console.log(year)
                                // console.log(sub);
                                if (year.localeCompare(sub) < 0) {
                                    sessionStorage.setItem('Graduated', 0);
                                    // console.log(year+"<"+sub)
                                } else if (year.localeCompare(sub) == 0) {
                                    sessionStorage.setItem('Graduated', 1);
                                    // console.log(year+"="+sub)
                                } else {
                                    sessionStorage.setItem('Graduated', 1);
                                    // console.log(year+">"+sub)
                                }
                                // console.log("Graduated="+sessionStorage.getItem('Graduated'))
                                // 登录成功马上就获取该用户未读消息个数
                                axios.get(_this.GLOBAL.host+'message/listnum/'+
                                    _this.ruleForm.accountnumber).then(res=>{
                                    _this.$store.state.UnReadMessageNum=res.data
                                    sessionStorage.setItem('UnReadMessageNum',res.data)
                                    _this.$router.push({path:'/home'});
                                })

                            }
                        });
                    } else {
                        return false;
                    }
                });
            },

            resetForm(formName) {
                this.$refs[formName].resetFields();
            },

            register() {
                this.$router.push('/register');
            },

            forgotPass() {
                this.$router.push('/resetPass');
            },

            toFZU() {
                const _this = this;
                this.$confirm('您打开的是站外链接，即将离开本页面', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    window.location.href = 'https://www.fzu.edu.cn/';
                }).catch(() => {

                });
            },

            aboutInfo() {
                this.$alert('本项目为福州大学黄于晗的毕业设计', '关于我们', {
                    confirmButtonText: '确定',
                });
            },

            contactUs() {
                const _this = this;
                this.$confirm('您打开的是站外链接，即将离开本页面', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    window.location.href = 'https://www.cnblogs.com/shadouyou/';
                }).catch(() => {

                });
            }
        }
    }
</script>

<style scoped>
    #page {
        width: 1400px;
        position: center;
        margin: 0 auto;

    }
    .Background {
        height: 750px;
        width: 1400px;
        background-image: url("../../img/login-background.jpg");
        background-size:cover;
    }
    #login {
        background: rgba(80, 80, 80, 0.36);

        border-radius:15px;
        float: right;
        margin-right: 200px;
        margin-top: 100px;
        height: 350px;
        width: 400px;
    }
    h2 {
        padding-top: 20px;
    }
    .el-form {
        padding-top: 30px;
        height: 200px;
        width: 350px;
    }
</style>
