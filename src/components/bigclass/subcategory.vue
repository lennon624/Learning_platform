<template>
  <section>
    <div class="page-banner" style="min-height: 220px;">
      <div id="banner2" :style="IMG">
        <el-breadcrumb
          separator-class="el-icon-arrow-right"
          style="position: absolute;top:100px;left: 98px;font-size: 15px;"
        >
          <el-breadcrumb-item :to="{ path: '/Home' }" class="bbb">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{this.parent.categoryName}}</el-breadcrumb-item>
        </el-breadcrumb>
        <div style="position: relative;left: 90px;top: 150px">
          <el-row :gutter="20" justify="start">
            <div v-for="child in childs" :key="child.id">
              <el-button
                style="margin-right: 20px"
                @click="showCourse(child.id)"
              >{{child.categoryName}}</el-button>
            </div>
          </el-row>
        </div>

        <h1 class="title-s">{{this.parent.categoryName}}</h1>
      </div>
    </div>
    <h2 class="explore-topics-skills-title" style="font-weight: bold;">选择喜欢的课程</h2>
    <totalcourse :categoryId="chiildcategoryID" :key="chiildcategoryID" :subcategory="form.parent_category_id"></totalcourse>
    <router-view />
  </section>
</template>

<script>
import Cardies from "@/components/Cardies";
import totalcourse from "@/components/course/totalcourse";
import {
  getchildcategory,
  getParentCategoryById
} from "@/api/modules/category";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
export default {
  name: "subcategory",
  components: { Cardies, totalcourse },
  data() {
    return {

      form: {
        parent_category_id: ""
      },
      parent:"",
      //子类别
      childs: "",
      chiildcategoryID: "",
      A: "arts_and_humanities",
      //父类图片
      IMG : "",
    };
  },
  watch: {
 '$route' (to, from) {
this.chiildcategoryID = this.$route.query.chiildcategoryID;
 console.log(this.$route.query)
 }
},
  mounted() {
    this.form.parent_category_id = this.$route.params.subcategory; //获取url参数
    this.chiildcategoryID = this.$route.query.chiildcategoryID;  //获取子类ID

    let T = getStore("chiildcategoryID");
    if(T != null){

      this.chiildcategoryID = T;
      removeStore("chiildcategoryID")

    }
    //初始化页面数据
     this.getParentCategoryById();
  },

  methods: {
    //获取父类别信息
    getParentCategoryById() {
      getParentCategoryById(this.form)
        .then(res => {
          console.log(res);
         //储存当前父类信息
         this.parent = res.data.data;
          //储存子类信息
         this.childs = this.parent.etc.children;
         //初始化图片数据
         this.IMG = {"background-image":"url(" + require("@/assets/img/"+this.parent.imgSrc+".jpeg") + ")"};
       console.log(this.parent.imgSrc)
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },

    //展示子类课程
    showCourse(id) {
      this.chiildcategoryID = id;
      console.log(this.chiildcategoryID);
    }
  }
};
</script>

<style scoped>
.el-breadcrumb/deep/ .el-breadcrumb__inner{
    color: white;
  }
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
#banner2 {
  margin-top: 2px;

  background-size: cover;
  height: 220px;
}

.el-breadcrumb__inner {
  color: white !important;
}
.el-breadcrumb__inner is-link {
  color: white !important;
}

.title-s {
  position: absolute;
  left: 100px;
  top: 115px;
  color: white;
}
.bzoom {
  margin-bottom: 20px;
  display: inline;
  float: left;
}
.boom {
  border-radius: 14px;
}

.el-row {
  margin-bottom: 20px;
  display: flex;
}
.el-col {
  border-radius: 14px;
}

</style>