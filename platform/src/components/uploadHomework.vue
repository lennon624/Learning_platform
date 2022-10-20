<template>
  <el-upload
    class="upload-demo"
    ref="upload"
    action="#"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :auto-upload="false"
    :http-request="upload"
    :on-success="handleSuccess"
  >
    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
    <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</el-button>
  </el-upload>
</template>
<script>
import { upload } from "@/api/modules/student.js";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
export default {
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["chapter"],
  data() {
    return {
      uploadData: {
        fileName: ""
      },
      studentId: "",
    };
  },

  mounted() {
    let user = JSON.parse(getCookie("user") || '{}');
    this.studentId = user.etc.id;
  },
  methods: {
    upload(param) {
      const formData = new FormData();
      // 获取文件名
      var fileName = param.file.name;
      // 添加参数
      formData.append("file", param.file);
      formData.append("fileName", fileName);
      formData.append("chapterId", this.chapter.id);
      formData.append("studentId", this.studentId);
      // 向后台发送文件
      upload(formData)
        .then(res => {
          if (res.data.success) {
            console.log("上传成功");
            param.onSuccess();
            this.toast(res.data.message, 1);
          }
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleSuccess(res, file) {}
  }
};
</script>

<style>
  input[type="file"] {
    display: none;
  }
</style>

