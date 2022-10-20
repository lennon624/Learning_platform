<template>
  <div class="one">
    <h3 class="top">视频章节</h3>
    <!-- <el-divider></el-divider> -->
    <el-card class="box-card" shadow="never">
      <el-collapse accordion style="margin-left: 30px">
        <el-collapse-item v-for="chapter in chapters" :key="chapter.id" :title="chapter.chapter">
          <!-- <el-button @click="govideo(chapter.etc)">观看视频</el-button> -->
          <el-button v-show="!show" @click="change()">观看视频</el-button>
          <el-button v-show="show" @click="change()">结束观看</el-button>
          <div v-show="show">
            <subvideo :src="chapter.etc.video_link" :key="id" style="background-color: black"></subvideo>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
import subvideo from "@/components/subvideo.vue";
import { ChapterByCourseId } from "@/api/modules/chapter";
import { getVideoSrcByChapterId } from "@/api/modules/video";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
export default {
  name: "videolist",
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["course"],
  components: {
    subvideo,
  },
  data() {
    return {
      //章节内容
      chapters: "",
      show: "",
      id: "",
    };
  },
  mounted() {
    //获取章节列表
    this.getChapterByCourseId();
  },
  methods: {
    //通过课程id获取章节列表
    getChapterByCourseId() {
      ChapterByCourseId({ courseId: this.course.id })
        .then((res) => {
          this.chapters = res.data.data;
          console.log(this.chapters);
        })
        .catch((err) => {
          this.toast(err, 2);
        });
    },

    //跳转播放界面
    govideo(row) {
      setStore("videosrc", row.video_link);
      this.navigator("/videopage");
    },
    //控制视频组件显示
    change() {
      this.show = !this.show;
      this.id = new Date();
    },
  },
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
  width: 100%;
  margin: 0 auto;
  border: none;
  border-radius: 0;
  border-top: solid 2px #edeeef;
}
</style>