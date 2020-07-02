<template>
  <div>
    <Nav></Nav>

    <!--  <el-menu>-->
    <!--      <nav></nav>-->
    <!--  </el-menu>-->
    <!--        <Nav></Nav>-->
    <div id="v-content" v-bind:style="{minHeight: Height+'px'}">
    <router-view />
    </div>
    <myfooter></myfooter>
  </div>


</template>



<script>
import Nav from "./Nav";
import myfooter from "./footer";
export default {
  name: "index",
  components: { Nav, myfooter },

  data() {
    return {
      Height: 0
    }
  },


  mounted() {
    //动态设置内容高度 让footer始终居底   header+footer的高度是100
    this.Height = document.documentElement.clientHeight - 100;
    //监听浏览器窗口变化　
    window.onresize = ()=> {this.Height = document.documentElement.clientHeight -100}
  }
  }


</script>

<style scoped>
</style>