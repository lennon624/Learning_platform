<template>
  <div class="one">
    <h3 class="top">课程讨论</h3>
    <el-divider></el-divider>
    <el-card class="box-card">
      <div style="margin: 20px 0;"></div>
      <el-input
        type="textarea"
        placeholder="请输入内容"
        v-model="textarea"
        maxlength="30"
        show-word-limit
      ></el-input>

      <el-button type="primary" @click="GKD2">提交</el-button>
      <el-divider></el-divider>

      <ul class="mcd-menu" style="margin: 5px">
        <li v-for="item in items" :key="item.id">
            <i class></i>
            <strong>来自{{item.fromUserId}}的评论:</strong>
            <small>{{item.comment}}</small>
        </li>
      </ul>
    </el-card>
  </div>
</template>

<script>
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import { getCommentsById, comment } from "@/api/modules/course";
export default {
  name: "bbs",
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["course"],
  data() {
    return {
      //评论区内容
      textarea: "",
      items: "",
      myuser: "",
      username: "",
      ChapterByCourseId: ""
    };
  },
  mounted() {
    this.myuser = JSON.parse(getCookie("user") || "{}");
    this.username = this.myuser.username;
    this.GKD();
  },
  methods: {
    //查询所有评论
    GKD() {
      getCommentsById({ courseId: this.course.id }) //名字
        .then(res => {
          this.items = res.data.data;
          console.log(this.items);
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },
    //添加评论内容
    GKD2() {
      comment({
        courseId: this.course.id,
        username: this.username,
        comment: this.textarea
      })
        .then(res => {
          this.GKD();
          this.textarea = null;
        })
        .catch(err => {
          this.toast(err, 2);
        });
    }
  }
};
</script>

<style scoped>
.top {
  margin-right: 1100px;
}
.one {
  border-style: solid;
  border-width: 0.1px;
  border-radius: 10px;
  border-color: #edeeef;
  background-color: white;
  margin-left: 0px;
}
.box-card {
  width: 800px;
  margin: 0 auto;
}
</style>