<template>
  <div class="totalcourse">
    <el-row>

      <el-col :span="6" v-for="course in courses" :key="course.id">
<!--        <el-card :body-style="{ padding: '0px' }">-->
          <li class="card" @click="goCourse(course)" >


              <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"  />


            <a class="card-description" target="_blank">
              <h2>{{course.courseName}}</h2>
              <p type="text" class="button" @click="goCourse(course)">查看详情</p>
            </a>

          </li>
        <el-divider></el-divider>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCourseByCategory ,getCourseByParentCategoryId} from "@/api/modules/course";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
export default {
  components: {},
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["categoryId","subcategory"],

  data() {
    return {
      value: 3.7, //评分
      courses: "" //课程数组
    };
  },
  mounted() {
    this.getCourseByCategory1();
  },
  methods: {
    //获取课程内容
    getCourseByCategory1() {             //待修改
      if(this.categoryId !== undefined){
        console.log(this.categoryId+"sadasdasd")
      getCourseByCategory({ category_id: this.categoryId })
        .then(res => {
          console.log("wuhu " + res);

          this.courses = res.data.data;
        })
        .catch(err => {
          // this.toast(err, 2);
        });
      }else {
        //获取父类全部课程
        getCourseByParentCategoryId({ parent_category_id: this.subcategory })
        .then(res => {
          console.log("wuhu " + res);

          this.courses = res.data.data;
        })
        .catch(err => {
          this.toast(err, 2);
        });
      }
    },

    //跳转课程界面
    goCourse(row) {
  
      this.navigator("/index/" +this.subcategory+"/"+ row.id);
    }
  }
};
</script>
<style>
  /* Lazy Load Styles */
  .card-image {
    display: block;
    min-height: 20rem; /* layout hack */
    background: #fff center center no-repeat;
    background-size: cover;
    filter: blur(3px); /* blur the lowres image */
  }

  .card-image > img {
    display: block;
    width: 100%;
    opacity: 0; /* visually hide the img element */
  }

  .card-image.is-loaded {
    filter: none; /* remove the blur on fullres image */
    transition: filter 1s;
  }




  /* Layout Styles */
  html, body {
    width: 100%;
    height: 100%;
    margin: 0;
    font-size: 16px;
    font-family: sans-serif;
  }

  .card-list {
    display: block;
    margin: 1rem auto;
    padding: 0;
    font-size: 0;
    text-align: center;
    list-style: none;
  }

  .card {
    display: inline-block;
    width: 240px;
    height: 340px;
    max-width: 20rem;
    margin: 0px;
    font-size: 1rem;
    text-decoration: none;
    overflow: hidden;
    box-shadow: 0 0 3rem -1rem rgba(0,0,0,0.5);
    transition: transform 0.1s ease-in-out, box-shadow 0.1s;
  }

  .card:hover {
    transform: translateY(-0.5rem) scale(1.0125);
    box-shadow: 0 0.5em 3rem -1rem rgba(0,0,0,0.5);
  }

  .card-description {
    display: block;
    padding: 1em 0.5em;
    color: #515151;
    text-decoration: none;
  }

  .card-description > h2 {
    margin: 0 0 0.5em;
  }

  .card-description > p {
    margin: 0;
  }
</style>