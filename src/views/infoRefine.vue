<template>
  <div class="infoRefine-container">
    <div class="imgContainer">
      <img src="../assets/infoRefineImg.jpg" alt />
    </div>
    <div class="inputForm">
      <h2>个人信息完善</h2>
      <el-form ref="refineForm" label-width="80px" :model="refineForm" :rules="refineRules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="refineForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="refineForm.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            ref="email"
            v-model="refineForm.email"
            name="email"
            type="text"
            autocomplete="on"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="tel">
          <el-input v-model="refineForm.tel" disabled></el-input>
        </el-form-item>
        <el-form-item label="兴趣选择:" prop="tel">
          <el-checkbox-group v-model="checkboxGroup" size="big">
            <el-checkbox label="商业" border></el-checkbox>
            <el-checkbox label="艺术与人文" border></el-checkbox>
            <el-checkbox label="农业" border></el-checkbox>
            <el-checkbox label="物理学" border></el-checkbox>
            <el-checkbox label="数学与逻辑" border></el-checkbox>
            <el-checkbox label="计算机科学" border></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <!-- <div>{{checkboxGroup}}</div> -->
      <el-button class="btn" type="primary" round @click="updateInfo">进入平台</el-button>
    </div>
  </div>
</template>

<script>
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import Vcode from "vue-puzzle-vcode";
import { refineInfo } from "@/api/modules/user";

export default {
  name: "infoRefine",
  data() {
    //用户名验证
    var checkLength = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名不能为空"));
      }

      if (value.length >= 5) {
        callback();
      } else {
        callback(new Error("长度不能小于5"));
      }
    };
    //密码验证
    var checkPassword = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("密码不能为空"));
      }

      if (value.length >= 5) {
        callback();
      } else {
        callback(new Error("长度不能小于6位"));
      }
    };
    //邮箱验证
    var userEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
      // if (!value) {
      //   return callback();
      // }
      if (!value) {
        return callback(new Error("邮箱不能为空"));
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback();
        } else {
          callback(new Error("请输入正确的邮箱格式"));
        }
      }, 100);
    };

    return {
      refineForm: {
        username: "",
        password: "",
        email: "",
        tel: this.$route.query.tel //接收上一个界面传来的参数
      },
      checkboxGroup: [],
      refineRules: {
        username: { validator: checkLength, trigger: "blur" },
        password: { validator: checkPassword, trigger: "blur" },
        email: { validator: userEmail, trigger: "blur" }
      }
    };
  },
  methods: {
    updateInfo() {
      this.$refs.refineForm.validate(valid => {
        if (valid) {
              // console.log(JSON.stringify(this.checkboxGroup));
              // console.log(typeof(JSON.stringify(this.checkboxGroup)));
          refineInfo({
            username: this.refineForm.username,
            password: this.refineForm.password,
            email: this.refineForm.email,
            tel: this.refineForm.tel,
            interests: JSON.stringify(this.checkboxGroup)
          })
            .then(resp => {
              let user = JSON.parse(getCookie("user") || "{}");
              user.username = this.refineForm.username;
              user.email = this.refineForm.email;
              setCookie("user", JSON.stringify(user));
              this.toast(resp.data.message, 1);
              this.navigator("/Home");
            })
            .catch(data => {
              this.toast(data, 2);
            });
        }
      });
    }
  }
};
</script>

<style lang="scss" >
html {
  // background-image: url("../assets/loginBack.jpeg");
  background-repeat: no-repeat;
  background-size: auto;
  // background-color: blue;
}
h2 {
  color: #2897ff;
}

.infoRefine-container {
  margin: 50px auto;
  height: 650px;
  width: 1000px;
  border: solid 1px none;
  border-radius: 20px;
  box-shadow: #d5dadf 6px 9px 33px 5px;
  background-color: rgba($color: white, $alpha: 0.7);
}

.imgContainer {
  float: left;
  height: 650px;
  width: 400px;
  background-color: rgba($color: white, $alpha: 0.1);
}

.imgContainer img {
  border: solid 1px none;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  height: 100%;
  width: 100%;
}

.inputForm {
  float: left;
  height: 650px;
  width: 470px;
  padding: 0px 65px;
}

.el-checkbox.is-bordered {
  margin: 10px;
}

.btn {
  margin: auto 220px;
}
</style>


