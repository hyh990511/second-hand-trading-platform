<template>
    <div>
        <div class="show">
            <el-row id="topBlock" :gutter="20">
                <el-col :span="8" style="margin-top: 20px">
                    <el-badge value="hot" class="item">
                        <span style="font-size: 20px">毕业专区</span>
                    </el-badge>
                    <span style="font-size: 10px">________临近毕业，学长学姐物件低价甩卖！</span>
                </el-col>
                <div class="block" id="top">
                    <el-carousel height="200px" :interval="6000" type="card">
                        <el-carousel-item v-for="item in imgList" style="border: solid 1px black">
                            <el-image :src="item"></el-image>
                        </el-carousel-item>
                    </el-carousel>
                </div>
            </el-row>
            <el-row :gutter="40">
                <!--            搜索结果板块              -->
                <el-col class="searchResult" :span="19">
                    <!--                    搜索框                    -->
                    <el-form :model="keywordForm"
                             class="searchForm"
                             style="margin: 20px 0px"
                             @submit.native.prevent>
                        <el-row>
                            <el-col>
                                <el-form-item>
                                    <el-input v-model="keywordForm.goods"
                                              placeholder="请输入商品名称"
                                              prefix-icon="el-icon-search"
                                              style="width: 50%"
                                              @keyup.enter.native=
                                                      "goSearch(keywordForm.goods)">
                                    </el-input>
                                    <el-button type="primary"
                                               @click="goSearch(keywordForm.goods)">搜索
                                    </el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                    <!--                    提示语                    -->
                    <div v-if="isSearch === 0">
                        <div class="noInfo" v-if="totalGoods === 0">很抱歉，该专区暂时没有在售商品，您可以到其它专区逛逛</div>
                    </div>
                    <div v-if="isSearch === 1">
                        <div class="prompt" v-if="totalGoods !== 0">共查询到{{totalGoods}}件商品,结果如下</div>
                        <div class="noInfo" v-if="totalGoods === 0">很抱歉，没有找到与“{{keyword}}”相关的商品，您可以尝试换个关键词搜索</div>
                    </div>
                    <div class="prompt">
                        <i class="el-icon-search" style="float: left;font-size: 30px"></i>
                        <span style="float: left;font-weight: bolder;margin-left: 10px;">搜索结果：</span>
                    </div>
                    <!--                    分割线                    -->
                    <div v-if="totalGoods !== 0"
                         style="margin: 10px 0px;
                                width: 100%;
                                height: 2px;
                                background: darkgray;
                                clear: both;">
                    </div>
                    <!--                 搜索结果展示区                -->
                    <el-row :gutter="12" v-if="totalGoods !== 0">
                        <el-col  class="col-bg" :span="6" v-for="(goods,index) in goodsList" :key="index">
                            <el-card class="card" :body-style="{ padding: '0px', height:'360px'}" @click.native="detail(goods.goodsID)">
                                <div class="cardContent">
                                    <div>
                                        <el-image class="img" :src="goods.imgfirst" lazy></el-image>
                                    </div>
                                    <div class="infoArea">
                                        <div style="width: 240px; margin: 5px">
                                            <el-row>
                                                <el-col :span="16">
                                                    <div class="price">￥{{goods.goodsPrice}}</div>
                                                </el-col>
                                                <el-col :span="8">
                                                    <div class="time">{{goods.realaseTime}}</div>
                                                </el-col>
                                            </el-row>
                                        </div>
                                        <el-row>
                                            <el-col :span="24">
                                                <div class="gTitle">{{goods.title}}</div>
                                            </el-col>
                                        </el-row>
                                        <div style="width: 245px; margin: 5px">
                                            <el-row type="flex" class="row-bg" justify="center">
                                                <el-col :span="2">
                                                    <i class="el-icon-user-solid"></i>
                                                </el-col>
                                                <el-col :span="10" style="text-align: left">
                                                    <div class="nickName">{{goods.nickname}}</div>
                                                </el-col>
                                                <el-col :span="12">
                                                    <el-rate
                                                            :colors="colors"
                                                            v-model="goods.creditRating"
                                                            disabled
                                                            score-template="{project.value}">>
                                                    </el-rate>
                                                </el-col>
                                            </el-row>
                                        </div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                    <!--                    分页组件                  -->
                    <div style="width: 100%" v-if="totalGoods !== 0 && isSearch !== 0">
                        <el-pagination
                                background
                                layout="prev, pager, next"
                                :page-size=pageSize
                                :total="totalPage"
                                @current-change="page">
                        </el-pagination>
                    </div>
                </el-col>
                <!--            热卖商品板块              -->
                <el-col class="hotGoods" :span="5">
                    <div class="prompt">
                        <i class="el-icon-s-data" style="color: #ff4400;float: left;font-size: 30px"></i>
                        <span style="float: left;font-weight: bolder;margin-left: 10px;">热卖商品</span>
                    </div>
                    <!--                    分割线                    -->
                    <div style="margin: 10px 0px;
                                width: 100%;
                                height: 1px;
                                background: orangered;
                                float: left;">
                    </div>
                    <!--               热卖商品展示区                  -->
                    <el-row>
                        <el-col  class="col-bg" :span="24" v-for="(goods,index) in hotList" :key="index">
                            <el-card class="card" :body-style="{ padding: '0px', height:'360px'}" @click.native="detail(goods.goodsID)">
                                <div class="cardContent">
                                    <div>
                                        <el-image class="img" :src="goods.imgfirst" lazy></el-image>
                                    </div>
                                    <div class="infoArea">
                                        <div style="width: 240px; margin: 5px">
                                            <el-row>
                                                <el-col :span="16">
                                                    <div class="price">￥{{goods.goodsPrice}}</div>
                                                </el-col>
                                                <el-col :span="8">
                                                    <div class="time">{{goods.realaseTime}}</div>
                                                </el-col>
                                            </el-row>
                                        </div>
                                        <el-row>
                                            <el-col :span="24">
                                                <div class="gTitle">{{goods.title}}</div>
                                            </el-col>
                                        </el-row>
                                        <div style="width: 245px; margin: 5px">
                                            <el-row type="flex" class="row-bg" justify="center">
                                                <el-col :span="2">
                                                    <i class="el-icon-user-solid"></i>
                                                </el-col>
                                                <el-col :span="10" style="text-align: left">
                                                    <div class="nickName">{{goods.nickname}}</div>
                                                </el-col>
                                                <el-col :span="12">
                                                    <el-rate
                                                            :colors="colors"
                                                            v-model="goods.creditRating"
                                                            disabled
                                                            score-template="{project.value}">>
                                                    </el-rate>
                                                </el-col>
                                            </el-row>
                                        </div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        data() {
            return {
                imgList:{
                    url1:'https://images.pexels.com/photos/267885/pexels-photo-267885.jpeg?cs=srgb&dl=pexels-267885.jpg&fm=jpg',
                    url2:'https://images.pexels.com/photos/207691/pexels-photo-207691.jpeg?cs=srgb&dl=pexels-207691.jpg&fm=jpg',
                    url3:'https://images.pexels.com/photos/34578/pexels-photo.jpg?cs=srgb&dl=iphone-34578.jpg&fm=jpg'
                },
                keywordForm: {
                    goods: null
                },
                isSearch: 0,
                totalGoods: null,
                totalPage: null,
                ascription: 'graduation',
                pageSize: 16,
                goodsList: [],
                hotList: [],
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                keyword: null,
                accountnumber: null,
            };
        },

        // 获取搜索关键字并从后端请求该商品首页列表(一页16条)
        created() {
            // 登录验证，美化完界面之后自己打开
            this.accountnumber = sessionStorage.getItem('accountNumber');
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }

            // 点击本页先随机展示属于本专区的商品
            const _this = this
            axios.post(_this.GLOBAL.host+'goods/init/'+_this.pageSize+'/'+
                 _this.ascription).then(function (resp) {
                    _this.goodsList = resp.data
                    for (var i = 0; i < _this.goodsList.length; i++) {
                        _this.goodsList[i].creditRating = parseFloat(_this.goodsList[i].creditRating);
                    }
            })

            // 获取热卖商品
            let hotSize = 4;    //热买推荐商品的个数(建议不要大于4个)
            axios.get(_this.GLOBAL.host+'goods/hot/'+_this.ascription+'/'+hotSize)
                .then(function (resp) {
                    _this.hotList = resp.data
            })
        },
        methods: {
            //商品搜索事件
            goSearch(key) {
                if (key !== null && key != '') {
                    this.isSearch = 1;
                    this.keyword = key;
                    const _this = this;
                    axios.get(_this.GLOBAL.host+'goods/search/1/'+_this.pageSize+'/'+
                        _this.keyword+'/'+ _this.ascription).then(function (resp) {
                        _this.goodsList = resp.data.content
                        _this.totalGoods = _this.goodsList.length
                        _this.totalPage = resp.data.totalElements
                    })
                }
            },

            //分页获取商品列表(一页16条)
            page(currentPage) {
                // alert(currentPage)
                const _this = this
                axios.get(_this.GLOBAL.host+'goods/search/'+currentPage+'/'+
                    _this.pageSize+'/'+_this.keyword+'/'+_this.ascription)
                    .then(function (resp) {
                        _this.goodsList = resp.data.content
                        _this.totalPage = resp.data.totalElements
                    })
            },

            //传送数据给商品详情页
            detail(goods_id) {
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
    #top{
        clear: both;
    }
    .prompt {
        text-align:left;
        margin-top: 10px;
        clear: both;
    }
    .noInfo {
        padding-top: 25%;
    }
    .show {
        margin: 30px;
        width: 1320px;
        opacity:0.9;
    }
    .col-bg {
        margin-bottom:20px;
    }
    .card {
        width: 250px;
        height: 360px;
    }
    .card:hover{
        cursor: pointer;
    }
    .cardContent {
        padding: 0px;
        margin: 0px;
        height: 360px;
    }
    .img {
        width: 250px;
        height: 250px;
    }
    .price {
        word-break:keep-all;
        white-space:nowrap;
        overflow:hidden;
        text-overflow:ellipsis;

        padding: 5px;
        text-align:left;
        font-family:SourceHanSansSC;
        font-weight:700;
        font-size:18px;
        color: #ff4400;
        font-style:normal;
        letter-spacing:0px;
        line-height:15px;
        text-decoration:none;
    }
    .time {
        font-size: 8px;
        padding: 5px 5px 0px 0px;
    }
    .gTitle {
        white-space:pre-wrap;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        word-break:break-all;
        text-align:left;
        width:95%;
        height:40px;
        padding: 0px 10px 5px 10px;
    }
    .nickName {
        width: 100px;
        word-break:keep-all;
        white-space:nowrap;
        overflow:hidden;
        text-overflow:ellipsis;
    }

</style>