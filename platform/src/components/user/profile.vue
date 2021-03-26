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
      <!--            <div class="about">-->
      <!--                <h3>个人介绍</h3>水电费水电费水电费收到。<br/>水电费水电费水电费的说法-->
      <!--            </div>-->
      <div>
        <h3>个人信息</h3>
        <div>
          <i>手机：</i>
          <span>900-000-000</span>
        </div>
        <div>
          <i></i>学校：
          <span>黑心大学</span>
        </div>
        <div>
          <i></i>邮箱：
          <span>231231@gmail.com</span>
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
        <el-checkbox-group v-model="list" >
          <el-checkbox v-for="item in items" :label="item.id" :key="item.id">{{item.categoryName}}</el-checkbox>
        </el-checkbox-group>
        <br />
       

        <el-button type="success" plain style="margin-top: 30%" @click="song">确认选择</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import { getChildCategory2 } from "@/api/modules/category";
import { selectInterests } from "@/api/modules/student";
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
      items:"",
      list:[],
    };
  },
  methods: {
    //获取所有兴趣选项
    getChildCategory3() {
      getChildCategory2()
        .then(resp => {
            console.log(resp)
            this.items = resp.data.data;
        })
        .catch(data => {
          this.toast(data, 2);
        });
    },
    song(){
      console.log(this.list)
       let id = [];
        this.list.forEach(item =>{
          id.push(item)
        
        })
        
      selectInterests({ids:id.join(',') , studentId: this.studentId})
        .then(resp => {
            console.log(resp)
        
  
      this.$message({
        message: "添加成功",
        type: "success"
      });
     
 
        })
        .catch(data => {
          this.toast(data, 2);
        });

    },
  }
};
</script>

<style scoped>
@import "../../dist/profi.css";
</style>
