<template>
  <div class="one">
    <h3 class="top">作业</h3>
    <!-- <el-divider></el-divider> -->
    <el-card class="box-card" shadow="never">
      <div v-for="o in this.chapters" :key="o.id">
        {{o.chapter}}
        <uploadHomework :chapter="o"></uploadHomework>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ChapterByCourseId } from "@/api/modules/chapter";
import uploadHomework from "@/components/uploadHomework.vue";
export default {
  name: "homework",
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["course"],
  components: {
    uploadHomework
  },
  data() {
    return {
      chapters: {},
      formData: {
        field101: null
      },
      rules: {},
      field101Action: "https://jsonplaceholder.typicode.com/posts/",
      field101fileList: []
    };
  },

  mounted() {
    console.log(this.course);
    //获取章节列表
    this.getChapterByCourseId();
  },

  methods: {
    //通过课程id获取章节列表
    getChapterByCourseId() {
      ChapterByCourseId({ courseId: this.course.id })
        .then(res => {
          this.chapters = res.data.data;
          console.log(this.chapters);
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },

    submitForm() {
      this.$refs["elForm"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
    },
    field101BeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2;
      if (!isRightSize) {
        this.$message.error("文件大小超过 2MB");
      }
      return isRightSize;
    },
    submitUpload() {
      this.$refs["field101"].submit();
    }
  }
};
</script>

<style scoped>
.top {
  margin-right: 1100px;
}
.text {
  font-size: 14px;
}

.item {
  /*padding: 11px 0;*/
  margin-top: 50px;
  margin-right: 600px;
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