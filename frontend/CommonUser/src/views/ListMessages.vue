<template>

    <div class="infinite-list-wrapper" style="overflow:auto;width: 1400px;height:780px;margin: auto">
        <div style="margin-bottom: 20px">
            <el-tooltip effect="light" content="清空消息列表" placement="bottom">
            <el-button @click="dialogVisible=true" style="padding: 0;float: right;margin-right:100px;background-color: #E9EEF3;border: none">
                <i class="el-icon-delete" style="font-size: 25px;color: red" ></i>
            </el-button>
            </el-tooltip>
        </div>
        <ul
                class="list"
                v-infinite-scroll="load"
                infinite-scroll-disabled="disabled"
        >
            <el-card class="box-card" v-for="(item,index) in messageList">
                <div slot="header" class="clearfix">
                    <i class="el-icon-info" style="font-size: 20px;float: left;color: goldenrod;margin-right: 10px"></i>
                    <span v-if="item.isread == 0" style="font-size: 15px;float: left">消息未读</span>
                    <span v-else style="font-size: 15px;float: left">消息已读</span>
                    <i class="el-icon-setting" style="font-size: 20px;position:relative;right: 5%" v-if="item.type == 0">  系统通知</i>
                    <i class="el-icon-chat-round" style="font-size: 20px;position:relative;right: 5%" v-else>  用户消息</i>
                    <el-tooltip effect="light" content="确认已读" placement="top-start">
                        <el-button style="float: right; padding: 3px 0" type="text" @click="setNum(item.messageid)" v-if="item.isread == 0">
                            <i class="el-icon-check" style="font-size: 25px"></i></el-button>
                    </el-tooltip>
                    <el-tooltip effect="light" content="删除该消息" placement="top-start">
                        <el-button style="float: right; padding: 3px 0" type="text" @click="deleteMessage(item.messageid)" v-if="item.isread == 1">
                            <i class="el-icon-close" style="font-size: 25px;color:red;"></i></el-button>
                    </el-tooltip>
                </div>
                <div>
                    <p>{{item.message}}</p>
                </div>
            </el-card>
        </ul>
        <p v-if="loading">加载中...</p>
        <p v-if="noMore">没有更多了</p>
        <el-dialog
                title="警告"
                :visible.sync="dialogVisible"
                width="30%"
                >
            <i class="el-icon-warning-outline" style="color: red;font-size: 20px"></i>
            <span>该操作会删除所有消息，包括未读的消息</span>
            <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="deleteAll">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    import store from "../store/store";
    import axios from 'axios'
    export default {
        name: "ListMessages",
        created() {
            let isLogin = sessionStorage.getItem('isLogin');
            if (isLogin !== '1') {
                this.$router.push('/login');
            }
            const _this=this
            axios.get(_this.GLOBAL.host+'message/listnum/'+
                sessionStorage.getItem('accountNumber')).then(res=>{
                this.$store.state.UnReadMessageNum=res.data
            })
            axios.get(this.GLOBAL.host+'message/list/'+
                sessionStorage.getItem('accountNumber')+'/0/8').then(res=>{
                _this.messageList=res.data.content
                _this.total=res.data.totalElements
                _this.pagenow++
                _this.numNow+=_this.messageList.length
            })
        },
        data(){
            return{
                total:0,
                numNow:0,
                pagenow:0,
                loading:false,
                messageList:[],
                dialogVisible:false,
            }
        },
        computed: {
            noMore () {
                return this.numNow >= this.total
            },
            disabled () {
                return this.loading || this.noMore
            }
        },
        methods:{
            //改变未读消息个数
            setNum(mid){
                store.commit('decrease')
                axios.put(this.GLOBAL.host + '/message/read/' + mid).then( res =>{
                })
                window.location.reload()
            },
            deleteMessage(mid){
              //删除id对应的消息
                axios.delete(this.GLOBAL.host + '/message/delete/'+mid)
                //好捞啊，又是这个,局部刷新得要弄成组件
                window.location.reload()
            },
            //获取新数据
            load(){
                this.loading=true
                const _this=this
                setTimeout(() => {
                    this.loading = false;
                    axios({
                        url:this.GLOBAL.host+'message/list/'+sessionStorage.getItem('accountNumber')+'/'+this.pagenow+'/8',
                        method:'get',
                        headers:{
                            token:localStorage.getItem('token')
                        }

                    }).then(res=>{
                        _this.pagenow++
                        _this.messageList.push.apply(_this.messageList,res.data.content)
                        _this.numNow+=res.data.content.length
                    })
                }, 1500)
            },
            deleteAll()
            {
                if(this.messageList.length===0)
                {
                    this.$message({
                        message:'没有消息可以删除',
                        type:'warning'
                    })
                }else {
                    const _this=this
                    axios.delete(this.GLOBAL.host+'message/deleteAllByAccount/'+sessionStorage.getItem('accountNumber')).then(res=>{
                        _this.dialogVisible=false
                        _this.$message({
                            message:'操作成功',
                            type:'success'
                        })
                        setTimeout(function () {
                            window.location.reload()
                        },800)

                    })
                }

            }

        }
    }
</script>

<style scoped>
    .el-card{
        border-radius: 30px;
        width: 50%;
        margin: 20px 0;
        position: relative;
        left: 25%;
    }

</style>
