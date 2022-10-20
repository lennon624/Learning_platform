<template>
  <el-header>
    <div>
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
      >
        <div>
          <div class="til" @click="gohome">Dasabi</div>

          <div style="display: inline;float:left;margin-left: 50px;">
            <el-menu
              default-active="null"
              class="el-menu-demo"
              mode="horizontal"
              @select="handleSelect"
            >
              <el-submenu index="0">
                <template slot="title">探索</template>
                <!-- 一级菜单 -->
                <template v-for="kid in kids">
                  <el-submenu :key="kid.id" :index="kid.id" @click="gocategory(kid)">
                    <template slot="title">{{kid.categoryName}}</template>
                    <!-- 二级菜单 -->
                    <el-menu-item
                      v-for="subkid in kid.etc.children"
                      @click="gosubcategory(subkid,kid)"
                      :key="subkid.id"
                      :index="subkid.id"
                    >{{subkid.categoryName}}</el-menu-item>
                  </el-submenu>
                </template>
              </el-submenu>
            </el-menu>
          </div>

          <div style="display: inline;float:left;margin-left: 14%">
            <el-input v-model="searchcourse" style="width: 400px" placeholder="查找喜欢的课程">
              <el-button
                style="background-color: #2d73cc;color: white"
                slot="append"
                icon="el-icon-search"
                @click="search()"
              ></el-button>
            </el-input>
          </div>
        </div>
        
    
<div style="margin-left: 600px">
      <div style="float:left;width: 2px;height: 45px; background: darkgray;margin-left: 20%"></div>
      <div style="color: white;float: left">11</div>
      <div style="margin-right: 0%;float: left">
        <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
      </div>
<div >
      <el-submenu index="1" style="float: left;margin-right:0%;">
        <template slot="title">{{username}}</template>
        <el-menu-item index="2-1" @click="gomyclass">我的课程</el-menu-item>
        <el-menu-item index="2-2" @click="goprofi">个人资料</el-menu-item>
        <el-menu-item index="2-3" @click="goedit">设置</el-menu-item>
        <el-menu-item index="2-4" @click="signout">退出</el-menu-item>
      </el-submenu>
</div>
</div>
    </el-menu>
    </div>
  </el-header>
</template>

<script>
import { getCourseById, getCourseByName } from "@/api/modules/course";
import { getStore, setStore, removeStore } from "@/libs/localstorage";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import { getparentcategory, getchildcategory } from "@/api/modules/category";
export default {
  name: "Nav",
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      searchcourse: "",
      kids: "",
      subkids: "",
      username: "",
      results: ""
    };
  },
  mounted() {
    this.getcategory();
    let user = JSON.parse(getCookie("user") || "{}");
    this.username = user.username;
  },
  methods: {
    //通过课程名查询课程
    search() {
      setStore("result", this.searchcourse);

      this.navigator("/searchcourse?coursename="+this.searchcourse);
    },
    gohome() {
      this.$router.push("/home");
    },
    goedit() {
      this.$router.push("/edit");
    },
    gomyclass(){

      this.$router.push('/myclass');

    },

    goprofi() {
      this.$router.push("/profile");
    },
    //退出登录
    signout() {
      let user = getCookie("user");
      removeCookie("user", user);
      removeStore("user");
      this.navigator("/login");
    },
    //探索大类点击
    gocategory(kid) {
      this.navigator("/index/" + kid.id);
    },
    //探索小类点击
    gosubcategory(subkid, kid) {
      setStore("chiildcategoryID", subkid.id);
      this.navigator("/index/" + kid.id + "?" + subkid.id);
    },

    gocategory(kid) {
      console.log(kid.etc.children);
    },
    //查询父类别
    getcategory() {
      getparentcategory()
        .then(res => {
          this.kids = res.data.data;
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },

    handleSelect(key, keyPath, kid) {
      console.log(key, keyPath, kid);
    }
  }
};
</script>

<style scoped>
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
  line-height: 460px;
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
  width: 360px;
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
