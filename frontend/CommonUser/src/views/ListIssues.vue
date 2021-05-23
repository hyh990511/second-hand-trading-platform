<template>
    <div id="listIssue" style="height: 780px;width:1400px;">
        <div id="list" style="width: 1200px;margin:0 auto;line-height: 50px;">
            <!--订单表-->
            <el-table ref="singleTable" :data="tableData">
                <el-table-column
                        prop="img"
                        label="图片"
                        width="120">
                    　　<template scope="scope">
                    　　　　<el-image :src="scope.row.imgfirst">
                    <div slot="placeholder" class="image-slot">
                        加载中<span class="dot">...</span>
                    </div>
                </el-image>
                    　　</template>
                </el-table-column>

                <el-table-column
                        prop="name"
                        label="商品名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="审核状态"
                        width="120"
                        sortable>
                    <template slot-scope="scope">
                        <span v-if="scope.row.isexamine == 0">{{'未审核'}}</span>
                        <span v-else-if="scope.row.isexamine == 1">{{'审核通过'}}</span>
                        <span v-else>{{'审核失败'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="realasetime"
                        label="发布日期"
                        width="120"
                        sortable>
                </el-table-column>
                <el-table-column
                        prop="goodsprice"
                        width="120"
                        label="价格（rmb）">
                </el-table-column>
                <el-table-column
                        width="120"
                        label="商品状态">
                    <template slot-scope="scope">
                        <span v-if="scope.row.issale == 0 && scope.row.isexamine == 1">{{'在售'}}</span>
                        <span v-else-if="scope.row.issale == 1">{{'下架'}}</span>
                        <span v-else-if="scope.row.issale == 2 && scope.row.isexamine == 1">{{'交易中'}}</span>
                        <span v-else>{{'待审核'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="300">
                    <template slot-scope="scope">
                        <el-button  type="text" size="small" v-if="scope.row.issale == 0" @click="pullDown(scope.row.goodsid)">下架</el-button>
                        <el-button  type="text" size="small" icon="el-icon-edit" @click="editGoods(scope.row.goodsid)">编辑</el-button>

                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <el-pagination
                    background
                    layout="prev, pager, next"
                    :page-size=pageSize
                    :total="total"
                    @current-change="page">
            </el-pagination>


        </div>
        <el-tooltip id="issue" effect="light" content="点击发布新商品" placement="top-start">
            <el-button @click="gotoIssue" style="z-index: 999;background-color: khaki">
                <i class="el-icon-plus" style="position: absolute; left: 23%;top:20%;"></i>
            </el-button>
        </el-tooltip>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "ListIssues",

        data() {
            return {
                tableData: [{
                    goodsid:1,
                    name:'阿斯普',
                    title:'aaa',
                    text:'asdasd',
                    isexamine:0,
                    //这里是releasetime，但是传来的数据是realase，将错就错
                    realasetime:'2020',
                    goodsprice:10.0,
                    issale:0,
                    imgfirst:'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1590664356302&di=1467aecb2cebbe9dbff1d55bc8958c78&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg',
                }],
                pageSize: 5,
                total: null,
            }
        },
        methods: {
            confirm(currentRow) {
                this.dialogFormVisible = true;
                this.currentRow = currentRow;
            },
            page(currentPage) {
                const that = this;
                axios.get(this.GLOBAL.host+'goods/issue/list/'+ (this.accountnumber)
                    +'/'+(currentPage-1)+'/5').then(function (resp) {
                    that.tableData = resp.data.content;
                })
            },
            pullDown(id){
                //todo:根据id下架商品
                axios.put(this.GLOBAL.host+'goods/issue/cancel/'+id).then(()=>{
                    this.$message({
                        message:'操作成功',
                        type:'success'
                    })
                    setTimeout(function () {
                        window.location.reload()
                    },800)

                })
            },
            editGoods(id){
                sessionStorage.setItem('issueTag',id);
                    this.$router.push('/issueGoods')
            },
            gotoIssue()
            {
                this.$router.push('/issueGoods')
            },

        },
        created() {
            this.accountnumber = sessionStorage.getItem('accountNumber');
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
            const that=this;
            axios.get(this.GLOBAL.host+'goods/issue/list/' +
                (this.accountnumber) + '/0/8').then(function (resp) {
                that.tableData = resp.data.content;
                that.total = resp.data.totalElements;
            })
        }
    }
</script>

<style scoped>
    #goodList{
    }
    #issue {
        position: fixed;
        height: 60px;
        width: 60px;
        border-radius: 50%;
        align-content: center;
        font-size: xx-large;
        margin-right: 60px;
        bottom: 15%;
        right: 45%;
    }
</style>
