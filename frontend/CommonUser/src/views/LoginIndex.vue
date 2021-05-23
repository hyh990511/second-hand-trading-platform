<template>
    <div id="page2" v-if = "!$route.meta.keepAlive">
        <el-container>
            <el-header>
                <div class="sweet">校园二手交易平台</div>
            </el-header>
            <div class="myMain">
                <router-view></router-view>
            </div>
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
    export default {
        methods: {
            toFZU() {
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
                this.$alert('本项目由福州大学黄于晗毕业设计所开发', '关于我们', {
                    confirmButtonText: '确定',
                });
            },

            contactUs() {
                this.$confirm('您打开的是站外链接，即将离开本页面', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    window.location.href = 'https://www.cnblogs.com/shadouyou/';
                }).catch(() => {

                });
            },
        },

    }
</script>

<style scoped>
    #page2 {
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
    .sweet {
        margin-top: 10px;
        text-align: left;
        font-family: 黑体;
        font-size: 25px;
        color: white;
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
