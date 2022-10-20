<template>
  <div class="resume">
    <div class="base">
      <div class="profile">
        <div class="photo">
          <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
        </div>
        <div class="info">
          <h4 class="name">{{username}}</h4>
        </div>
      </div>
      <div>
        <h3>个人信息</h3>
        <div>
          <i>用户名：</i>
          <span>{{this.user.username}}</span>
        </div>
        <div>
          <i>手机：</i>
          <span>{{this.user.tel}}</span>
        </div>
        <div>
          <i></i>邮箱：
          <span>{{this.user.email}}</span>
        </div>
        <!--                <div > <i ></i><span>codepen.io/xichen</span></div>-->
      </div>
    </div>

    <div
      style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);width: 50%;margin-left: 12%;
                    height: 470px;margin-top: 3%"
    >
      <div class="block">
        <br />

        <el-tag>选择兴趣</el-tag>
        <br />
        <br />
        <br />
        <el-checkbox-group v-model="list">
          <el-checkbox
            v-for="item in items"
            :label="item.id"
            :key="item.id"
            :checked="item.etc"
            @change="checked=>handleChange(checked, item)"
          >{{item.categoryName}}</el-checkbox>
        </el-checkbox-group>
        <br />
      </div>
    </div>
  </div>
</template>

<script>
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import { getChildCategory2 } from "@/api/modules/category";
import {
  selectInterests,
  removeInterest,
  addInterest
} from "@/api/modules/student";
export default {
  name: "profile",
  mounted() {
    this.user = JSON.parse(getCookie("user") || "{}");
    this.studentId = this.user.etc.id;

    //获取所有兴趣选项
    this.getChildCategory3();
  },
  data() {
    return {
      username: "",
      studentId: "",
      user: "",
      items: "",
      list: []
    };
  },
  methods: {
    //获取所有兴趣选项
    getChildCategory3() {
      getChildCategory2({ studentId: this.studentId })
        .then(resp => {
          this.items = resp.data.data;
        })
        .catch(data => {
          this.toast(data, 2);
        });
    },

    // 复选框改变
    handleChange(checked, item) {
      if (checked == true) {
        addInterest({ studentId: this.studentId, childCategoryId: item.id })
          .then(resp => {
            this.toast(resp.data.message, 1);
          })
          .catch(data => {
            console.log(data);
          });
      } else if (checked == false) {
        removeInterest({ studentId: this.studentId, childCategoryId: item.id })
          .then(resp => {
            this.toast(resp.data.message, 1);
          })
          .catch(data => {
            console.log(data);
          });
      }
    }
  }
};
</script>

<style scoped>
@import "../../dist/profi.css";
</style>
