<template>
    <div id="order_content">
        <div>
            <!--订单表-->
            <el-table
                    ref="singleTable"
                    :data="tableData"
                    tooltip-effect="dark"
                    :default-sort = "{prop: 'purchase_date', order: 'ascending'}"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
                    @row-click="goodsdetail">
                <el-table-column
                        v-for="(item,index) in tableData"
                        v-if="index === 0"
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
                        prop="goodsname"
                        label="商品名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="tradingstatus"
                        label="交易状态"
                        width="120"
                        sortable>
                </el-table-column>
                <el-table-column
                        prop="releasetime"
                        label="购买日期"
                        width="200"
                        sortable>
                </el-table-column>
                <el-table-column
                        prop="goodsprice"
                        label="价格（rmb）">
                </el-table-column>
                <el-table-column
                        prop="sendway"
                        label="送货方式"
                        :formatter="sendwayFormat">
                </el-table-column>
                <el-table-column
                        prop="sendprice"
                        label="运费(rmb)">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="300">
                    <template slot-scope="scope">
                        <el-button @click.native.stop ="confirm(scope.row)" type="text" size="small" icon="el-icon-check" v-if="scope.row.tradingstatus==='待收货'"><span style="color: #2ed573">确认收货</span></el-button>
                        <el-button @click.native.stop ="cancel(scope.row)" type="text" size="small" icon="el-icon-s-release" v-if="scope.row.tradingstatus==='待收货'"><span style="color: #a4b0be">取消订单</span></el-button>
                        <el-button @click.native.stop ="appeal(scope.row)" type="text" size="small" icon="icon-tousu iconfont" v-if="scope.row.tradingstatus==='已收货'"><span style="color: #ff6b81">订单申诉</span></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <el-pagination
                    background
                    layout="prev, pager, next"
                    :page-size=pageSize
                    :total="total"
                    :current-page="curPage+1"
                    @current-change="page">
            </el-pagination>

            <!--确认订单评分表单-->
            <el-dialog title="此次交易评分" :visible.sync="dialogFormVisible">
                <el-rate
                        v-model="value1"
                        :colors="colors"
                        show-text
                >
                </el-rate>

                <!--取消订单-->
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="score">确 定</el-button>
                </div>
            </el-dialog>

            <el-dialog title="订单申诉" :visible.sync="appealFormVisible" :append-to-body="true">
                <el-form :model="appealForm" :rules="rules" ref="appealForm">
                    <el-form-item label="原因：" prop="appealreason">
                        <el-input ref="appeal_input" type="textarea" :rows="4" v-model="appealForm.appealreason" placeholder="请填写申诉原因" ></el-input>
                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="appealCancel">取 消</el-button>
                    <el-button type="primary" @click="appealSubmit('appealForm')">确 定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "Order",
        data() {
            return {
                appealForm:{
                    appealreason: '',
                    orderid: null,  //订单id,
                },
                rules: {
                    appealreason: [
                        { required: true, message: '申诉原因不能为空', trigger: 'blur' }
                    ]
                },

                value1: 3,
                dialogFormVisible: false,
                appealFormVisible: false,
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                currentRow: null,

                tableData: [
                ],
                multipleSelection: [],
                //userId: '221701207',
                userId: null,
                pageSize: 5,
                total: null,
                curPage: 0,
            }
        },
        methods: {
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            confirm(currentRow){
                this.dialogFormVisible = true;
                this.currentRow = currentRow;
            },
            score(){
                this.dialogFormVisible = false;
                const that = this;
                axios.post(this.GLOBAL.host+'user/order/affirm/'+
                    this.currentRow.orderid+'/'+this.value1).then(function(resp) {
                    if(resp.data === 'success'){
                        that.$message({
                            type: 'success',
                            message: '确认订单成功!'
                        });
                        // location. reload()
                        that.$router.push({
                            path: '/refresh',
                            query:{
                                destination: '/order',
                                param: that.curPage,
                            }
                        })

                    }else{
                        that.$message({
                            type: 'warning',
                            message: '确认订单失败!请稍后再试！'
                        });
                    }
                })
            },
            cancel(currentRow){
                // console.log(currentRow);
                const that = this;
                this.$confirm('确认要取消订单吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    axios.post(this.GLOBAL.host+'user/order/cancel/' +
                        currentRow.orderid).then(function(resp) {
                        if(resp.data == 'success'){
                            that.$message({
                                type: 'success',
                                message: '取消订单成功!'
                            });
                            // location. reload()
                            that.$router.push({
                                path: '/refresh',
                                query:{
                                    destination: '/order',
                                     param: that.curPage,
                                }
                            })

                        }else{
                            that.$message({
                                type: 'warning',
                                message: '取消订单失败!'
                            });
                        }
                    })
                }).catch(() => {
                    that.$message({
                        type: 'info',
                        message: '取消操作'
                    });
                });

                // this.$alert('是否确认取消订单', '注意！', {
                //     confirmButtonText: '确定',
                //     callback: action => {
                //         this.$message({
                //             type: 'warning',
                //             message: `抱歉！我们正在马不停蹄地开发！`
                //         });
                //     }
                // });
            },
            appeal(currentRow) {
                this.appealForm.appealreason = '';
                this.appealForm.orderid = currentRow.orderid;
                this.appealFormVisible = true;
                this.$nextTick(() => { // 2. 弹框显示DOM更新完成后 获取refs.ref1 设置焦点
                    this.$refs['appeal_input'].focus(); // 设置焦点
                })
            },
            appealCancel(){
                this.appealFormVisible = false;
            },
            appealSubmit(formName){
                const that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // 订单申诉交互交互
                        axios.post(this.GLOBAL.host+'user/order/appeal',
                            this.appealForm).then(function (resp) {
                            if(resp.data == 'success'){
                                that.$message({
                                    type: 'success',
                                    message: '申诉订单成功!'
                                });
                            }else{
                                that.$message({
                                    type: 'warning',
                                    message: '申诉订单失败!'
                                });
                            }
                            // location. reload()
                            that.$router.push({
                                path: '/refresh',
                                query:{
                                    destination: '/order',
                                    param: that.curPage,
                                }
                            })
                        })

                        that.appealFormVisible = false;
                    } else {
                        return false;
                    }
                });
            },
            // comment(currentRow){
            //     // console.log(currentRow);
            //     this.$alert('该功能暂未开放，敬请期待', '抱歉！', {
            //         confirmButtonText: '确定',
            //         callback: action => {
            //             this.$message({
            //                 type: 'warning',
            //                 message: `抱歉！我们正在马不停蹄地开发！`
            //             });
            //         }
            //     });
            // },
            // stateFormat(row,column){
            //     if (row.iscomment === 1) {
            //         return '已评价'
            //     } else  {
            //         return '未评价'
            //     }
            // },
            page(currentPage){
                //alert(currentPage);
                this.curPage = currentPage - 1;
                const that = this;
                axios.get(this.GLOBAL.host+'user/order/'+this.userId+'/' +
                    (currentPage - 1)+'/'+this.pageSize).then(function(resp) {
                    //console.log(resp);
                    that.tableData = resp.data.content;
                    that.total = resp.data.totalElements;
                })
            },
            goodsdetail(row, event, column){
              //console.log(row.goodsid);
                this.$router.push({
                    path: '/GoodsDetails',
                    query:{
                        goodsID: row.goodsid,
                    }
                })
            },
            sendwayFormat(row, column){
                if(row.sendway == 0){
                    return "自提"
                }else{
                    return "卖家送"
                }
            }
        },
        created(){
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
            this.userId = sessionStorage.getItem('accountNumber');
             if(this.$route.query.param !== undefined){
                this.curPage = this.$route.query.param;
            }
            const that = this;
            axios.get(this.GLOBAL.host + 'user/order/'+this.userId +
                '/' + this.curPage + '/' + this.pageSize).then(function(resp) {
                that.tableData = resp.data.content;
                that.total = resp.data.totalElements;
            })
        }
    }
</script>

<style scoped>
    @import "https://at.alicdn.com/t/font_1791888_e6s3aotu86b.css";
    #order_content{
        width: 1200px;
        margin: 0 auto;
        line-height: 50px;
    }
</style>
