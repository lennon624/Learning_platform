<template >
  <div>
    <div style="background-color:#f8f8f8;">
      <span
        class="particletext bubbles"
        style="font-size: 30px;margin-left: -80%;"
      >{{this.etc.school_name}}</span>
      <a style="border-left:1px;padding-left: 11px">{{this.course.courseName}}</a>
    </div>
    <el-divider></el-divider>

    <el-container>
      <el-aside style="background-color: #f8f8f8" width="160px">
        <el-menu
          class="categories"
          default-active="0"
          style="margin-top: 70px;text-align:left"
          active-text-color="#1E90FF"
        >
          <el-menu-item class="seta" index="1" @click="showWhat = 'discover'">
            <i class="el-icon-s-finance"></i>
            <span slot="title">课程介绍</span>
          </el-menu-item>
          <el-menu-item index="2" class="seta" @click="showWhat = 'videolist'">
            <i class="el-icon-s-custom"></i>
            <span slot="title">视频章节</span>
          </el-menu-item>
          <el-menu-item index="3" class="seta" @click="showWhat = 'homework'">
            <i class="el-icon-s-goods"></i>
            <span slot="title">作业</span>
          </el-menu-item>
          <el-menu-item index="4" class="seta" @click="showWhat = 'Talking'">
            <i class="el-icon-s-platform"></i>
            <span slot="title">讨论区</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!--        <el-main>-->
      <!--            <div class="vid" v-bind:style="{width: myWidth}">-->

      <!--                <subvideo src='' style="background-color: black"></subvideo>-->
      <!--            </div>-->
      <!--        </el-main>-->

      <el-main style="background-color:#f8f8f8;margin-top: -30px;">
        <!-- 动态切换显隐，组件 -->
        <component :is="showWhat" :course="course"></component>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { getCourseById, getpcategorybycourse } from "@/api/modules/course";
import subvideo from "@/components/subvideo.vue";
import discover from "@/components/learn/discover.vue";
import homework from "@/components/learn/homework.vue";
import videolist from "@/components/learn/videolist.vue";
import bbs from "@/components/learn/bbs.vue";
import Talking from "@/components/learn/Talking.vue";

export default {
  beforeCreate() {
    document.querySelector("body").setAttribute("style", "background:#f8f8f8");
  },
  components: {
    subvideo,
    discover,
    homework,
    bbs,
    videolist,
    Talking,
  },
  data() {
    return {
      //默认显示组件discover，若字符串为B则显示组件B,name为component声明
      showWhat: "discover",
      myWidth: window.innerWidth - 500 + "px",
      //对应课程
      course: "",
      etc: "",
      ID: "",
    };
  },
  mounted() {
    this.ID = this.$route.params.course; //获取课程ID参数
    console.log(this.ID);
    //通过课程id查询课程信息
    this.getCourseById1();
  },

  methods: {
    //通过课程ID查询课程信息
    getCourseById1() {
      getCourseById({ course_id: this.ID })
        .then((res) => {
          this.course = JSON.parse(JSON.stringify(res.data.data))[0];
          this.etc = JSON.parse(JSON.stringify(this.course.etc));
        })
        .catch((err) => {
          this.toast(err, 2);
        });
    },
  },
};
</script>

<style  scoped>
.el-menu-vertical:not(.el-menu--collapse) {
  width: 250px;
}

.categories {
  position: fixed;
  margin-left: 50%;
  left: -700px;
  top: 100px;
  width: 150px;
  border-style: solid;
  border-width: 0.1px;
  border-radius: 20px;
  border-color: #edeeef;
}
.seta {
  font-weight: bolder;
  font-size: large;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.bodyContainer {
  height: 100vh;
}

.el-header {
  height: 50%;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 30px;
  /*background:url('~@/assets/head.jpg') repeat;   */
}

.el-aside {
  background-color: #2a3542;
}

.el-main {
  background-color: white;
}
.toggle-button {
  background-color: #a72fc527;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
.el-avatar {
  margin-right: 15px;
}
.back {
  background-color: #0d47a1;
}
</style>