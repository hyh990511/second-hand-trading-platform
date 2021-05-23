<template>
    <div id="page1" v-if = "$route.meta.keepAlive">
        <el-container>
            <el-header style="padding: 0px">
                <el-menu router
                         :default-active="activeIndex"
                         class="el-menu-demo"
                         mode="horizontal"
                         @select="handleSelect"
                         background-color="#545c64"
                         text-color="#fff"
                         active-text-color="#ffd04b"
                         style="width: 100%; float: left">
                    <el-row>
                        <el-col :span="3">
                            <el-menu-item index="/home">校园二手交易平台</el-menu-item>
                        </el-col>
                        <el-col :span="2" :offset="1">
                            <el-menu-item index="/c_goods">普通商品</el-menu-item>
                        </el-col>
                        <el-col :span="2">
                            <el-menu-item index="/b_goods" >以书易书</el-menu-item>
                        </el-col>
                        <el-col :span="2">
                            <el-menu-item index="/g_goods" >毕业专区</el-menu-item>
                        </el-col>
                        <el-col :span="4" :offset="9" v-if="isLogin == '1'">
                            <el-row>
                                <el-col :span="9">
                                    <div style="padding-top: 16px;">
                                        <el-badge :value="$store.state.UnReadMessageNum" class="item" v-if="$store.state.UnReadMessageNum > 0">
                                            <el-link icon="el-icon-bell" type="primary" @click="toMessage">消息</el-link>
                                        </el-badge>
                                        <el-link icon="el-icon-bell" type="primary" @click="toMessage" v-else>消息</el-link>
                                    </div>
                                </el-col>
                                <el-col :span="10">
                                    <el-submenu index="3">
                                        <template slot="title">
                                            <el-link type="primary">我的</el-link>
                                        </template>
                                        <el-menu-item index="/u_info">个人信息</el-menu-item>
                                        <el-menu-item index="/order">我的订单</el-menu-item>
                                        <el-menu-item index="/listIssues">我的发布</el-menu-item>
                                        <el-menu-item @click.native="logout">退出登录</el-menu-item>
                                    </el-submenu>
                                </el-col>
                                <el-col :span="5">
                                    <div  style="float: left; margin-top: 5px" >
                                        <el-avatar :size="50" :src="$store.state.UserImages" @error="errorHandler"></el-avatar>
                                    </div>
                                </el-col>
                            </el-row>
                        </el-col>
                        <el-col :span="2" :offset="12" v-else>
                            <div style="padding-top: 16px;">
                                <el-row>
                                    <el-col :span="10">
                                        <el-link type="primary" @click="toLogin">登录</el-link>
                                    </el-col>
                                    <el-col :span="10">
                                        <el-link type="primary" @click="toRegist">注册</el-link>
                                    </el-col>
                                </el-row>
                            </div>
                        </el-col>
                    </el-row>
                </el-menu>
            </el-header>
            <el-main>
                <router-view v-if="isRouterAlive"></router-view>
            </el-main>
            <el-footer style="height: 120px">
                <div class="introduce" style="padding-top: 10px; padding-left: 30px">
                    <div style="font-size: 18px; width: 100%">校园二手交易平台——毕业设计</div>
                    <div style="padding-top: 20px">
                        <el-row :gutter="20">
                            <el-col :span="30">
                                <el-link type="info" class="introduce" @click="aboutInfo">关于我们</el-link>
                            </el-col>
                            <el-col :span="30">
                                <el-link type="info" class="introduce" @click="contactUs">联系我们</el-link>
                            </el-col>
                            <el-col :span="30">
                                <el-link type="info" class="introduce" @click="toFZU">@福州大学</el-link>
                            </el-col>
                        </el-row>
                    </div>
                </div>
                <div style="padding-top: 80px; position: center">©2021-2021 福州大学黄于晗  版权所有</div>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import ListIssues from "./ListIssues";
    import store from "../store/store.js";
    import axios from "axios";
    export default {
        data() {
            return {
                activeIndex: '/home',
                isLogin: '0',
                formInline: {
                    goods: '',
                },
                isRouterAlive: true,
            };
        },
        watch: {
            UnReadMessageNum(newVal,oldVal){
                console.log('newVal:'+newVal+',oldVal'+oldVal)
            }
        },
        provide(){ //提供
            return {
                reload: this.reload
            }
        },
        created() {
            this.isLogin = sessionStorage.getItem('isLogin');
            let account = sessionStorage.getItem('accountNumber')
            if (this.isLogin == '1') {
                let _this = this;
                axios.get(_this.GLOBAL.host+'/user/info/'+account)
                    .then(function (resp) {
                        let userInfo = resp.data;
                        sessionStorage.setItem('Images',userInfo.images);
                        _this.$store.state.UserImages = userInfo.images;
                    })
            }
        },
        methods: {
            errorHandler() {
                return true;
            },
            logout() {
                sessionStorage.setItem('isLogin','0');
                sessionStorage.setItem('accountNumber', null);
                localStorage.setItem('token', "Bearer " + "error");
                this.isLogin = '0';
                this.activeIndex = '/home';
                this.$router.push({
                    path: '/refresh',
                    query:{
                        destination: '/',
                    }
                })
            },
            toLogin(){
                this.$router.push('/login');
            },
            toRegist() {
                this.$router.push('/register');
            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            },
            onSubmit(goods) {
                this.activeIndex = null;
                if(goods !== ""){
                    this.$router.push({
                        path: '/refresh',
                        query:{
                            destination: '/c_goods',
                            param: goods
                        }
                    })
                }
            },
            //TODO:消息页面
            toMessage() {
                this.activeIndex = null;
                this.$router.push('/listMessages');
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
                this.$alert('本项目为福州大学黄于晗毕设项目', '关于我们', {
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
            },
            reload(){
                this.isRouterAlive = false
                this.$nextTick( function () {
                    this.isRouterAlive = true
                })
            },
        },

    }
</script>

<style scoped>
    #page1 {
        width: 1400px;
        position: center;
        margin: 0 auto;
    }
    .el-header,.el-footer {
        background-color: #545c64;
        color: #333;
        text-align: center;
        /*line-height: 60px;*/
        height: 60px;
    }
    .el-main {
        margin: 0;
        padding: 0;
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        /*line-height: 700px;*/
        min-height: 80vh;
    }
    .myMain {
        background-color: #E9EEF3;
        color: #333;
        min-height: 80vh;
    }
    div:focus {
        outline:none;
    }
    .introduce {
        float: left;
        font-family:SourceHanSansSC;
        font-weight:700;
        font-size:14px;
        color:#909399;
        font-style:normal;
        letter-spacing:0px;
        line-height:20px;
        text-decoration:none;
    }

</style>
