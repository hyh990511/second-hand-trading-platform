<template>
    <div style="padding: 40px 350px 0px 350px;">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="昵称" prop="nickname">
                <el-input class=m_info v-model="ruleForm.nickname" style="width: 30%"></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
                <el-input class=m_info
                          v-model="ruleForm.realname"
                          maxlength="15"
                          show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="ruleForm.gender" style="float: left; padding-top: 13px">
                    <el-radio label="男"></el-radio>
                    <el-radio label="女"></el-radio>
                    <el-radio label="保密"></el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone">
                <el-input class=m_info v-model="ruleForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="常用邮箱" prop="email">
                <el-input class=m_info
                          v-model="ruleForm.email"
                          maxlength="40"
                          show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item label="QQ" prop="QQ">
                <el-input class=m_info
                          v-model="ruleForm.qqnumber"
                          maxlength="10"
                          show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item label="收货地址" prop="address">
                <el-input class=m_info
                          v-model="ruleForm.address"
                          maxlength="50"
                          show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item label="个性签名" prop="signature">
                <el-input class=m_info
                          type="textarea"
                          v-model="ruleForm.introduction"
                          maxlength="40"
                          show-word-limit>
                </el-input>
            </el-form-item>
            <el-row>
                <el-col>
                    <span>账号、昵称、性别、邮箱、QQ号、个性签名将对外展示</span>
                </el-col>
            </el-row>
            <el-form-item style="padding-left: 80px;margin-top: 10px; float: left">
                <el-button type="primary" @click="submitForm('ruleForm')">确认修改</el-button>
                <el-button @click="back">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data() {
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
                user: {},
                ruleForm: {
                    // accountNumber: 'xxxxx',
                    // nickname: 'xxx',
                    // gender: '100000',
                    // realname: '100000',
                    // email: '100000',
                    // phone: '100000',
                    // qqnumber: '100000',
                    // address: '100000',
                    // introduction: '100000',
                },
                accountnumber: null,
                rules: {
                    nickname: [
                        { required: true, message: '请输入昵称', trigger: 'blur' },
                        { min: 1, max: 8, message: '长度在 1 到 8 个字符', trigger: 'blur' }
                    ],
                    realname: [
                        { required: false }
                    ],
                    gender: [
                        { required: true, message: '请选性别', trigger: 'change' }
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
                        { required: true, message: '请填写校内收货地址', trigger: 'blur' }
                    ],
                    introduction: [
                        { required: false },
                        { min: 0, max: 30, message: '内容请勿超过30个字符', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    const _this = this;
                    if (valid) {
                        axios.put(_this.GLOBAL.host+'user/setting1', _this.ruleForm)
                            .then(function (resp) {
                            if(resp.data == 'success'){
                                _this.$alert('修改成功', '提示', {
                                    confirmButtonText: '确定'
                                });
                                _this.$router.push('/u_info');
                            } else {
                                _this.$alert('修改失败', '提示', {
                                    confirmButtonText: '确定'
                                });
                            }
                        })
                    } else {
                        _this.$alert('修改失败', '提示', {
                            confirmButtonText: '确定'
                        });
                        return false;
                    }
                });
            },

            back() {
                const _this = this;
                this.$confirm('您确定放弃信息修改吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.$router.push('/u_info');
                }).catch(() => {

                });
            },
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
                _this.ruleForm = resp.data;
            })
        },
    }
</script>

<style scoped>
    .m_info {
        float: left;
        width: 70%;
    }
</style>