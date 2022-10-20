<template>
  <div class="page-banner" style="min-height: 420px;">
    <div id="banner">
      <el-breadcrumb
              separator-class="el-icon-arrow-right"
              style="position: absolute;top:120px;left: 160px;font-size: 15px;color: white"
      >
        <el-breadcrumb-item :to="{ path: '/Home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item
                :to="{ path: '/index/'+this.form.parent_category_id}"
        >{{subcategory.child_category_info.categoryName}}</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.course.courseName}}</el-breadcrumb-item>
      </el-breadcrumb>

      <h1 class="title-s">{{this.course.courseName}}</h1>

      <!-- <h3 class="title-e">muzhu de chanhou huli</h3> -->
      <div class="jiaotou">
        <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
        <span
                style="position: relative;left: 12px;bottom: 10px;color: white"
        >{{this.course.etc.teacher_name}}</span>
      </div>
      <button class="jiaru" v-show="result" @click="goCourseDetails">继续学习</button>
      <button v-show="result" @click="removecourse">取消选课</button>
      <button class="jiaru" v-show="!result" @click="changestatus">加入课程</button>
      <div>
        <div class="tigong">
          <span>提供方</span>
        </div>
        <div class="xiao">
          <h1>{{this.course.etc.school_name}}</h1>
        </div>
      </div>
    </div>

    <!--    <div style="margin-top: 30px;margin-left: -990px">-->
    <!--        <nav>-->
    <!--            <a style="font-size: 20px">关于L</a>-->
    <!--            <a style="font-size: 20px;position:relative; left: 50px">CSS</a>-->

    <!--        </nav>-->
    <!--        <hr/>-->
    <!--    </div>-->
    <br />
    <div class="containerr" style="position: relative;left: 70%">
      <nav>
        <ul class="mcd-menu" style="border-radius: 20px">
          <span>推荐课程</span>
          <li v-for="item in items" :key="item.id">
            <a  @click="goCourse(item)" >
              <i class></i>
              <strong>{{item.courseName}}</strong>
              <small>{{item.introduction}}</small>
            </a>
          </li>
          <li>

          </li>
        </ul>
      </nav>
    </div>

    <!--Contenedor-->
    <div id="container">
      <!--Pestaña 1 activa por defecto-->
      <input id="tab-1" type="radio" name="tab-group" checked="checked" />
      <label for="tab-1">课程介绍</label>
      <!--Pestaña 2 inactiva por defecto-->
      <input id="tab-2" type="radio" name="tab-group" />
      <label for="tab-2">课程公告</label>
      <!--Pestaña 3 inactiva por defecto-->
      <!--            <input id="tab-3" type="radio" name="tab-group" />-->
      <!--            <label for="tab-3">Tab 3</label>-->
      <!--Contenido a mostrar/ocultar-->
      <div id="content">
        <!--Contenido de la Pestaña 1-->
        <div id="content-1">
          <p class="left">{{this.course.introduction}}</p>
        </div>
        <!--Contenido de la Pestaña 2-->
        <div id="content-2">
          <p class="left">{{this.course.notice}}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { getCourseById, getpcategorybycourse,coursebyinterest } from "@/api/modules/course";
  import { getStore, setStore, removeStore } from "@/libs/localstorage";
  import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
  import {
    queryMyList,
    addCourse2List,
    removeCourseFromList
  } from "@/api/modules/student";
  export default {
    name: "Course",
    components: "",
    data() {
      return {
        items: [

        ],

        form: {
          parent_category_id: "",
          student_id: "1"
        },
        course: {
          id: "",
          etc: {
            teacher_name: "",
            school_name: ""
          }
        }, //课程信息
        courseId: "", //课程id
        subcategory: {
          child_category_info: {
            id: ""
          }
        }, // 获得子类别
        schoolname: "", //学校名称
        myform: {
          studentId: "",
          courseId: ""
        },
        myuser: "",
        result: false,
        subcategory2:'',
        id2:'',
      };
    },
    mounted() {
      //判断有无课程信息
      this.isInfo();
      this.myuser = JSON.parse(getCookie("user") || "{}");
      this.myform.studentId = this.myuser.etc.id;
      this.myform.courseId = this.courseId;
      //查询课程是否在选课表中
      this.queryMyList1();
      //通过课程id查询课程信息
      this.getCourseById1();
      //通过课程id获取子类别
      this.getpcategorybyc();
      //通过兴趣获得推荐课程
      this.getcbyi();
    },
    methods: {
      getcbyi() {
        //根据兴趣推荐课程
        coursebyinterest({ student_id: this.myform.studentId })
                .then(resp => {
                  this.items = resp.data.data;
                  console.log("this.items");
                  console.log(this.items);
                  //  this.queryMyList1()
                })
                .catch(data => {
                  this.noInterest = true;
                  console.log(this.noInterest);
                  // this.toast(data, 2);
                });
      },
      //查询课程是否在选课表中
      queryMyList1() {
        queryMyList(this.myform)
                .then(resp => {
                  if (resp.data.data === "true") this.result = !this.result;
                  console.log(resp.data.data);
                  console.log(this.result);
                })
                .catch(data => {
                  this.toast(data, 2);
                });
      },
      //通过课程id获取子类别
      getpcategorybyc() {
        console.log("this.course.id: " + this.course.id);
        getpcategorybycourse({ course_id: this.course.id })
                .then(res => {
                  this.subcategory = res.data.data;
                  console.log(JSON.parse(JSON.stringify(this.subcategory)));
                })
                .catch(err => {
                  this.toast(err, 2);
                });
      },
      //跳转兴趣课程
      goCourse(row) {
        //根据课程Id获取子类别

        getpcategorybycourse({ course_id: row.id })
                .then(res => {
                  this.subcategory2 = res.data.data;
                  this.id2 = this.subcategory2.parent_category_info.id;
                  console.log(this.subcategory2.parent_category_info.id);
                  this.navigator("/index/" + this.id2 + "/" + row.id);
                })
                .catch(err => {
                  this.toast(err, 2);
                });
      },
      //取消选课
      removecourse() {
        removeCourseFromList({
          student_id: this.myform.studentId,
          course_id: this.myform.courseId
        })
                .then(res => {
                  console.log(res);
                  this.toast("取消选课成功");
                })
                .catch(err => {
                  this.toast(err, 2);
                });
        this.result = !this.result;
      },

      //改变课程状态
      changestatus() {
        this.$message({
          message: "加入课程成功",
          type: "success"
        });
        console.log(this.myform.studentId);
        addCourse2List({
          student_id: this.myform.studentId,
          course_id: this.myform.courseId
        })
                .then(res => {
                  console.log(res);
                })
                .catch(err => {
                  this.toast(err, 2);
                });
        this.result = !this.result;
      },
      //跳转课程详情界面
      goCourseDetails() {
        let study = this.course.id;

        this.navigator(
                "/index/" +
                this.form.parent_category_id +
                "/" +
                this.$route.params.course +
                "/study"
        );
      },

      //判断有无课程信息
      isInfo() {
        let info = this.$route.params.course; //获取url参数
        this.form.parent_category_id = this.$route.params.subcategory; //获取url参数
        //   if(info === null){
        //     this.navigator("/Home");  //没有课程信息直接进入返回404
        //   }
        //   else {
        this.courseId = info;
        this.course.id = this.courseId;
      },

      //通过课程ID查询课程信息
      getCourseById1() {
        getCourseById({ course_id: this.course.id })
                .then(res => {
                  console.log(res);

                  this.course = JSON.parse(JSON.stringify(res.data.data))[0];
                  console.log("this.course: ");
                  console.log(JSON.parse(JSON.stringify(this.course)));
                })
                .catch(err => {
                  this.toast(err, 2);
                });
      }
    }
  };
</script>

<style >
  @import "../../dist/list.css";
  .el-header,
  .el-footer {
    background-color: white;
    color: #333;
    text-align: center;
    line-height: 50px;
  }

  .el-aside {
    background-color: #d3dce6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    background-color: #e9eef3;
    color: #333;
    text-align: center;
    line-height: 30px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }

  .explore-topics-skills-title {
    font-size: 24px;
    font-weight: 300;
    margin-bottom: 20px;
    line-height: 32px;
    color: #4a4a4a;
    margin-left: -70%;
  }
  #banner {
    margin-top: 2px;
    background-image: linear-gradient(90deg, rgb(1, 46, 97), rgb(2, 71, 148));
    background-size: cover;
    height: 400px;
  }

  .el-breadcrumb__inner {
    color: white !important;
  }
  .el-breadcrumb__inner is-link {
    color: white !important;
  }

  .title-s {
    position: absolute;
    left: 185px;
    top: 170px;
    color: white;
    letter-spacing: 5px;
  }
  .title-e {
    position: absolute;
    left: 155px;
    top: 185px;
    color: white;
    font-weight: lighter;
  }
  .jiaru {
    margin-top: 20%;
    margin-left: -70%;
    width: 130px;
    height: 60px;
    font-size: 20px;
  }
  .jiaotou {
    position: absolute;
    top: 35%;
    left: 11%;
  }
  .tigong {
    color: white;
    position: absolute;
    left: 55%;
    bottom: 80%;
  }
  .xiao {
    color: white;
    position: absolute;
    left: 55%;
    bottom: 69%;
  }
  a:hover {
    color: red;
  }

  * {
    font-family: Arial, sans;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
  }

  #container {
    position: absolute;
    top: 66%;
    left: 2%;
    width: 70%; /* Ancho del contenedor */
  }

  #container input {
    height: 2.5em;
    visibility: hidden;
  }

  #container label {
    background: #f9f9f9; /* Fondo de las pestañas */
    border-radius: 0.25em 0.25em 0 0;
    color: #888; /* Color del texto de las pestañas */
    cursor: pointer;
    display: block;
    float: left;
    font-size: 1em; /* Tamaño del texto de las pestañas */
    height: 2.5em;
    line-height: 2.5em;
    margin-right: 0.25em;
    padding: 0 1.5em;
    text-align: center;
  }

  #container input:hover + label {
    background: #ddd; /* Fondo de las pestañas al pasar el cursor por encima */
    color: #666; /* Color del texto de las pestañas al pasar el cursor por encima */
  }

  #container input:checked + label {
    background: #f1f1f1; /* Fondo de las pestañas al presionar */
    color: #444; /* Color de las pestañas al presionar */
    position: relative;
    z-index: 6;
    /*
          -webkit-transition: .1s;
          -moz-transition: .1s;
          -o-transition: .1s;
          -ms-transition: .1s;
          */
  }

  #content {
    background: #f1f1f1; /* Fondo del contenido */
    border-radius: 0 0.25em 0.25em 0.25em;
    min-height: 20em; /* Alto del contenido */
    position: relative;
    width: 100%;
    z-index: 5;
  }

  #content div {
    opacity: 0;
    padding: 1.5em;
    position: absolute;
    z-index: -100;
    /*
          transition: all linear 0.1s;
          */
  }

  #content-1 p {
    clear: both;
    margin-bottom: 1em;
  }
  #content-1 p.left img {
    float: left;
    margin-right: 1em;
  }
  #content-1 p.last {
    margin-bottom: 0;
  }

  #content-2 p {
    float: left;
  }
  #content-2 p.column-right {
    margin-left: 3%;
  }
  #content-2 p img {
    display: block;
    margin: 0 auto 1em auto;
  }
  #content-3 p,
  #content-3 ul {
    margin-bottom: 1em;
  }
  #content-3 ul {
    margin-left: 2em;
  }

  #container input#tab-1:checked ~ #content #content-1,
  #container input#tab-2:checked ~ #content #content-2,
  #container input#tab-3:checked ~ #content #content-3 {
    opacity: 1;
    z-index: 100;
  }

  input.visible {
    visibility: visible !important;
  }
</style>
