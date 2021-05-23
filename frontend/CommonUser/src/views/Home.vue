<template>
  <div class="home">
    <div class="notice">
      <el-tooltip id="issue" effect="light" content="点击发布新商品" placement="top-start">
        <el-button @click="gotoIssue">+</el-button>
      </el-tooltip>
      <el-card class="notice-card">
        <el-table
                :data="noticeData"
                :row-style="{height:'70px'}"
                style="width: 100%;font-weight:bold">
          <el-table-column
                  prop="text"
                  label="公告"
                  :show-overflow-tooltip="true"
                  width="180">
          </el-table-column>
          <el-table-column
                  prop="releasetime"
                  label="日期"
                  width="120">
          </el-table-column>
        </el-table>
        <div class="page">
          <el-pagination
                  :hide-on-single-page="false"
                  :total="total"
                  :page-size="pageSize"
                  layout="prev, pager, next"
                  @current-change="page">
          </el-pagination>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
  .home{
    margin: 0;
    padding: 0;
  }
  .notice{
    background-image: url("../../img/background-02.jpg");
    background-size:100% 100%;
    height: 750px;
  }
  .notice-card{
    position: relative;
    left: 70%;
    top: 100px;
    width: 350px;
    height: 500px;
    Opacity: 0.5;
    border-radius: 50px;
    box-shadow: -10px 10px 5px #888888;
  }
  .page{
    padding: 30px;
  }

  #issue {
    position: absolute;
    height: 60px;
    width: 60px;
    border-radius: 50%;
    align-content: center;
    font-size: xx-large;
    /*margin-right: 60px;*/
    /*bottom: 25%;*/
    /*right: 15%;*/
    top: 20%;
    left: 20%;
  }
</style>

<script>
  import axios from 'axios'
  export default {
    data() {
      return {
        noticeData: [
        ],
        value: false,
        total: null,
        pageSize: 5,
      }
    },
    created() {
        sessionStorage.setItem('issueTag','issue');
        const that = this;
        axios.get(this.GLOBAL.host+'notice/list/0/'+this.pageSize)
                .then(function(resp) {
            that.noticeData = resp.data.content;
            that.total = resp.data.totalElements;
        })
    },
    methods:{
      page(currentPage){
        //alert(currentPage);
        const that = this;
        axios.get(this.GLOBAL.host+'notice/list/'+(currentPage - 1)
                + '/' + this.pageSize).then(function(resp) {
          that.noticeData = resp.data.content;
          that.total = resp.data.totalElements;
        })
      },
      gotoIssue()
      {
        this.$router.push('/issueGoods')
      },
    }
  }
</script>
