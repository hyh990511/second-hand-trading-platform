<template>
    <div style="padding: 100px 0px 0px 450px; width: 400px">
        <el-form :model="resetForm" status-icon :rules="resetFormRules" ref="resetForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="旧密码：" prop="password">
                <el-input type="password"
                          v-model="resetForm.password"
                          auto-complete="off"
                          @keyup.enter.native="toAmend('resetForm')">
                </el-input>
            </el-form-item>
            <el-form-item label="新密码：" prop="newPwd">
                <el-input type="password"
                          v-model="resetForm.newPwd"
                          auto-complete="off"
                          @keyup.enter.native="toAmend('resetForm')">
                </el-input>
            </el-form-item>
            <el-form-item label="确认密码：" prop="checkNewPwd">
                <el-input type="password"
                          v-model="resetForm.checkNewPwd"
                          auto-complete="off"
                          @keyup.enter.native="toAmend('resetForm')"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click.native.prevent="toAmend('resetForm')">确认修改</el-button>
                <el-button @click="cancel">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data() {
            let validatePass = (rule, value, callback) => {
                if (value == null) {
                    callback(new Error("请输入新密码"));
                } else if (value.length < 6 || value.length > 18) {
                    callback(new Error("密码长度为6-18位"));
                } else {
                    callback();
                }
            };
            let validatePass2 = (rule, value, callback) => {
                if (value == null) {
                    callback(new Error("请再次输入密码"));
                } else if (value !== this.resetForm.newPwd) {
                    callback(new Error("两次输入密码不一致!"));
                } else {
                    callback();
                }
            };
            return {
                //用于校验用户输入的表单
                resetForm: {
                    //传给后台所需要的参数
                    password: "",
                    newPwd: "",
                    checkNewPwd: "",
                },
                //用于提交给后台的表单
                user: {
                    accountnumber: "",
                    password: "",
                },
                resetFormRules: {
                    password: [
                        { required: true, message: "请输入旧密码", trigger: 'blur' }
                    ],
                    newPwd: [
                        { required: true, validator: validatePass, trigger: 'blur' }
                    ],
                    checkNewPwd: [
                        { required: true, validator: validatePass2, trigger: "blur" }
                    ]
                }
            };
        },
        created() {
            this.user.accountnumber = sessionStorage.getItem('accountNumber');
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
        },
        methods: {
            toAmend(formName) {
                // 设置user信息以便发送给后台
                this.user.accountnumber = sessionStorage.getItem('accountNumber');
                this.user.password = this.resetForm.newPwd;


                this.$refs[formName].validate((valid) => {
                    const _this = this;
                    if (valid) {
                        axios.put(_this.GLOBAL.host+'user/settingPw/'+
                            _this.resetForm.password, _this.user).then(function (resp) {
                            if(resp.data == 'success'){
                                _this.$alert('修改成功，您需要重新登陆', '提示', {
                                    confirmButtonText: '确定',
                                });
                                // console.log('修改成功，我要开始跳了')
                                sessionStorage.setItem('isLogin', '0');
                                sessionStorage.setItem('accountNumber', null);
                                _this.$router.push('/login');
                            } else {
                                _this.$alert('原密码错误！', '提示', {
                                    confirmButtonText: '确定',
                                });
                            }
                        })
                    } else {
                        _this.$alert('修改失败', '提示', {
                            confirmButtonText: '确定',
                        });
                        return false;
                    }
                });
            },

            cancel() {
                const _this = this;
                this.$confirm('您确定放弃密码修改吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.$router.push('/u_info');
                }).catch(() => {

                });
            }
        },
    };
</script>

<style scoped>
</style>