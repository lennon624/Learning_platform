<template>
  <div class="one">
    <h3 class="top">课程介绍</h3>
    <!-- <el-divider></el-divider> -->
    <el-card class="box-card" shadow="never">{{this.course.introduction}}</el-card>
  </div>
</template>

<script>
export default {
  name: "discover",
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["course"],
  methods: {
    goBack() {
      console.log("go back");
    }
  }
};
</script>

<style scoped>
.top {
  margin-right: 1100px;
}
.zheng {
  width: 700px;
  height: 800px;
  font-size: 20px;
  border: 0;
  margin-right: 450px;
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
  width: 100%;
  margin: 0 auto;
  border: none;
  border-radius: 0;
  border-top:solid 2px #edeeef;
}
</style>