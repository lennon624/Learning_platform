<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="width:80%;margin: 0 auto">
    <el-tab-pane label="全部课程" name="first">
      <el-row>
        <el-col :span="6" v-for="course in courses" :key="course.id">
          <!--        <el-card :body-style="{ padding: '0px' }">-->
          <li class="card" @click="goCourse(course)">
            <img
              src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
            />

            <a class="card-description" target="_blank">
              <h2>{{course.courseName}}</h2>
              <p type="text" class="button" @click="goCourse(course)">查看详情</p>
            </a>
          </li>
          <el-divider></el-divider>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="正在学习" name="second">
      <el-row>
        <el-col :span="6" v-for="course in courses" :key="course.id">
          <!--        <el-card :body-style="{ padding: '0px' }">-->
          <li class="card" @click="goCourse(course)">
            <img
              src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
            />

            <a class="card-description" target="_blank">
              <h2>{{course.courseName}}</h2>
              <p type="text" class="button" @click="goCourse(course)">查看详情</p>
            </a>
          </li>
          <el-divider></el-divider>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="已结束" name="third">已结束</el-tab-pane>
  </el-tabs>
</template>

<script>
import {
  getCourseById,
  getpcategorybycourse,
  getMyCourseList
} from "@/api/modules/course";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import {
  queryMyList,
  addCourse2List,
  removeCourseFromList
} from "@/api/modules/student";
export default {
  name: "myclass",
  components: "",
  data() {
    return {
      activeName: "first",
      courses: "", //课程数组
      user1: "",
      studentId: ""
    };
  },
  mounted() {
    this.user1 = JSON.parse(getCookie("user") || "{}");
    this.studentId = this.user1.etc.id;
    this.getAll();
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    //根据学生Id查询学生选课
    getAll() {
      getMyCourseList({ studentId: this.studentId })
        .then(res => {
          this.courses = res.data.data;
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },
    //跳转课程界面
    goCourse(row) {
      console.log(row);
      //根据课程Id获取子类别
      getpcategorybycourse({ course_id: row.id })
        .then(res => {
          this.subcategory = res.data.data;
          this.parent = this.subcategory.parent_category_info.id;
          this.navigator("/index/" + this.parent + "/" + row.id);
        })
        .catch(err => {
          this.toast(err, 2);
        });
    }
  }
};
</script>



<style scoped>
</style>
