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
            <div  style="float: left; padding-left: 40px; padding-top: 50px; height: 480px; width: 40%" >
                <div style="padding: 200px 100px 50px 100px">
                    <el-avatar :size="100" :src="userInfo.images" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"></el-avatar>
                </div>
            </div>
            <div style="float:left;margin-top: 30px;width: 1px;height: 460px; background: darkgray;"></div>
            <div style="float: left; margin-top: 80px; height: 500px; width: 55%" >
                <div class="text">
                    <p style="float: left"><i class="el-icon-collection-tag"></i> 昵称：{{ userInfo.nickname }}</p>
                </div>
                <div class="text">
                    <p style="float: left"><i class="el-icon-user"></i> 账号：{{ userInfo.accountnumber }}</p>
                </div>
                <div class="text">
                    <p style="float: left"><i class="el-icon-male"></i> 性别：{{ userInfo.gender }}</p>
                </div>
                <div style="float:left;margin: 20px 100px 3px 20px;width: 85%;height: 1px; background: darkgray;"></div>
                <div class="text" style="padding-left: 20px">
                    <p style="float: left;margin: 0; padding: 0"><i class="el-icon-s-promotion"></i> 联系方式</p>
                </div>
                <div class="text">
                    <p style="float: left">邮箱：{{ userInfo.email }}</p>
                </div>
                <div class="text">
                    <p style="float: left">QQ号：{{ userInfo.qqnumber }}</p>
                </div>
                <div style="float:left;margin: 20px 100px 3px 20px;width: 85%;height: 1px; background: darkgray;"></div>
                <div class="text" style="padding-left: 20px">
                    <p style="float: left;margin: 0; padding: 0"> 其它</p>
                </div>
                <div class="text">
                    <p style="float: left"><i class="el-icon-edit"></i> 个性签名：{{ userInfo.introduction }}</p>
                </div>
                <div class="text">
                    <p style="float: left"><i class="el-icon-star-off"></i> 信用等级：</p>
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
            <el-button type="primary" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)" @click="back2Goods">返回</el-button>
        </div>

    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data() {
            return {
                userInfo: [],
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],  // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
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
            let userId = this.$route.query.userID;
            // 后端需要新的根据用户ID返回信息的接口
            axios.get(_this.GLOBAL.host+ '/user/other/info/'+userId)
                .then(function (resp) {
                _this.userInfo = resp.data
            })
        },
        methods: {
            back2Goods() {
                let goods_id = this.$route.query.goodsID;
                this.$router.push({
                path: '/GoodsDetails',
                query:{
                    goodsID: goods_id,
                }
            })
            },
        }
    }
</script>

<style scoped>
    .text{
        width: 100%;
        float: left;
        padding-left: 80px;
    }
    .text > p{
        margin: 10px auto;
    }
</style>