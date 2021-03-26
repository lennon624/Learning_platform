<template>
  <el-main style="background-color: #f5f5f5;overflow:visible">
    <div class="block" style=" height:100%; width:1150px; margin:0 auto">
      
      <el-carousel height="150px" >
        <el-carousel-item v-for="kid in kids" :key="kid.id">
          <img :src="kid.idView" class="image"  />
        </el-carousel-item>
      </el-carousel>
    </div>

    <h2 class="explore-topics-skills-title">根据类别浏览课程</h2>

    <ul class="imglist">
      <li v-for="item in items" :key="item.id" @click="gobuss(item)">
        <img :src="getImgUrl(item.imgSrc)" />
      </li>

      <!-- <li @click="goart">
        <img src="../assets/arts_and_humanities.jpeg" />
      </li>
      <li @click="gofarm">
        <img src="../assets/health.jpeg" />
      </li>
      <li @click="gocomputer">
        <img src="../assets/information_technology.jpeg" />
      </li>
      <li @click="gomath">
        <img src="../assets/math_and_logic.jpeg" />
      </li>
      <li @click="gophy">
        <img src="../assets/social_sciences.jpeg" />
      </li>-->
    </ul>
    <h2 class="explore-topics-skills-title">为您推荐的课程</h2>
    <!--        <books class="books-area"></books>-->
    <Cardies></Cardies>
  </el-main>
</template>

<script>
// import Books from '../components/Books'
import Cardies from "../components/Cardies";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
import { getparentcategory } from "@/api/modules/category";
export default {
  // components: {Books},
  components: { Cardies },

  // name: "master"
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      items: "",
      src: "../assets/arts_and_humanities.jpeg",
      //下方为广告栏
      kid:"",
      kids: [
        { id: 0, idView: require("@/assets/img/arts_and_humanities.jpeg") },
        { id: 1, idView: require("@/assets/img/business.jpeg") },
        { id: 2, idView: require("@/assets/img/health.jpeg") }
      ]
    };
  },
  mounted() {
    this.getcategory();
  },
  methods: {
    //查询父类别
    getcategory() {
      getparentcategory()
        .then(res => {
          console.log(res);
         
          this.items = res.data.data;
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },

    //获取图片地址
    getImgUrl(img) {
      return require("@/assets/" + img + ".jpeg");
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    //跳转子类别
    gobuss(item) {
      setStore("category", item.id);
      this.navigator("/index/" + item.id);
    },
  
  }
};
</script>

<style scoped>
/* 广告位 */
.image {
    width: 100%;
    height: 100%;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

/* 广告位 */

.til {
  margin-left: 5%;
  float: left;
  color: #2d73cc;
  font-weight: bolder;
  font-size: 60px;
  display: inline;
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
li {
  list-style-type: none;
}
ul.imglist {
  margin: 0 auto;
  width: 1200px;
  overflow: hidden;
}
ul.imglist li {
  float: left;
  padding: 8px 8px;
  width: 380px;
}
ul.imglist li img {
  display: block;
  width: 360px;
  height: 170px;
}
/*ul.imglist li span{ display:block; width:100%; height:30px; line-height:30px; background:#F6F6F6}*/
.books-area {
  width: 1150px;
  margin-left: auto;
  margin-right: auto;
}
</style>