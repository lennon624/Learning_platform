<template >
  <div>
    <div style="background-color: #f7f7f7">
      <span class="til">Dasabi</span>
      <span
        style="border-left:solid 1px #d9d9d9;margin-left: 70px;font-size: 50px;padding-left: 40px;color: grey"
      >登陆</span>
    </div>

    <div
      style="background-image: -webkit-linear-gradient(90deg, #3a4457, #F6EAB8);width: 100%;position: fixed;height: 100%;top: 90px;left: 0px"
    ></div>
    <div class="myLoginUI">
      <el-card class="box-card">
        <div class="login_header">
          <span @click="cur=0" :class="{active:cur==0}">账号登录</span>
          <el-divider direction="vertical"></el-divider>
          <span @click="cur=1" :class="{active:cur==1}">快捷登录</span>
        </div>
        <div v-show="cur==0">
          <h1 class="h1">账号登录</h1>
          <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
            autocomplete="on"
            label-position="left"
          >
            <el-form-item class="login-form-item" prop="username">
              <el-input
                ref="username"
                v-model="loginForm.username"
                placeholder="用户名/邮箱/手机号"
                name="username"
                type="text"
                tabindex="1"
                autocomplete="on"
                prefix-icon="el-icon-user"
              />
            </el-form-item>

            <el-form-item class="login-form-item" prop="password">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="loginForm.password"
                :type="passwordType"
                placeholder="请输入密码"
                name="password"
                tabindex="2"
                autocomplete="on"
                prefix-icon="el-icon-lock"
                @keyup.enter.native="handleLogin"
              />
            </el-form-item>

            <el-button
              :loading="loading"
              type="primary"
              style="width:100%;margin-bottom:20px;"
              @click.native.prevent="handleLogin"
            >登录</el-button>
          </el-form>
        </div>

        <div v-show="cur==1" class="Cbody_item">
          <h1 class="h1">快捷登录</h1>
          <el-form
            ref="phoneForm"
            :model="phoneForm"
            :rules="phoneRules"
            class="login-form"
            autocomplete="on"
            label-position="left"
          >
            <!-- 手机部分 -->
            <el-form-item class="login-form-item" prop="tel">
              <el-input
                ref="tel"
                v-model="phoneForm.tel"
                placeholder="手机号"
                name="tel"
                type="text"
                tabindex="1"
                autocomplete="on"
                prefix-icon="el-icon-mobile-phone"
              />
            </el-form-item>

            <el-form-item class="login-form-item" prop="phonecode">
              <el-input
                placeholder="请输入短信验证码"
                style="width:60%; float: left"
                ref="phonecode"
                v-model="phoneForm.phonecode"
                name="phonecode"
                type="text"
                tabindex="1"
                autocomplete="on"
                prefix-icon="el-icon-chat-dot-square"
              />
              <el-button
                type="primary"
                plain
                @click="getTelCode"
                :disabled="disabled"
                style="float: right"
              >{{btntxt}}</el-button>
            </el-form-item>

            <el-button
              :loading="loading"
              type="primary"
              style="width:100%;margin-bottom:20px;"
              @click.native.prevent="fastLogin"
            >登录</el-button>
          </el-form>
        </div>
        <span class="no_account" @click="go2register">没有账号？</span>
        <span class="forget_password" @click="go2recovery">忘记密码？</span>

        <Vcode :show="isShow" @success="success" @close="close" />
      </el-card>
    </div>
  </div>
</template>

<script>
import Vcode from "vue-puzzle-vcode";
import { login, sendTelCode, loginByTel } from "@/api/modules/user";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
export default {
  name: "login",
  data() {
    var checkLength = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名/邮箱/手机号不能为空"));
      }

      if (value.length >= 5) {
        callback();
      } else {
        callback(new Error("长度不能小于5"));
      }
    };

    //邮箱验证
    var userEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;

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

    //手机验证
    var checkPhone = (rule, value, callback) => {
      const phoneReg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/;
      this.btntxt = "获取验证码";
      this.time = 0;
      if (!value) {
        return callback(new Error("手机号不能为空"));
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,实际输入的数字总是识别成字符串
        // 所以在前面加了一个+实现隐式转换

        if (!Number.isInteger(+value)) {
          callback(new Error("请输入数字值"));
        } else {
          if (phoneReg.test(value)) {
            this.time = 60;
            this.btntxt = "获取验证码";
            this.disabled = false;
            callback();
          } else {
            callback(new Error("电话号码格式不正确"));
          }
        }
      }, 100);
    };

    return {
      loginForm: {
        username: "",
        password: ""
      },
      phoneForm: {
        tel: "", //手机号
        phonecode: "" //手机号验证码
      },
      loginRules: {
        username: { validator: checkLength, trigger: "blur" },
        password: { required: true, message: "密码不能为空", trigger: "blur" },
        email: { validator: userEmail, trigger: "blur" }
      },
      phoneRules: {
        tel: { validator: checkPhone, trigger: "blur" },
        phonecode: {
          required: true,
          message: "验证码不能为空",
          trigger: "blur"
        }
      },
      isShow: false, // 验证码模态框是否出现
      loading: false,
      passwordType: "password",
      redirect: undefined,
      time: 0, //验证码时间
      btntxt: "获取验证码",
      disabled: false,
      cur: 0 //默认选中第一个tab
    };
  },

  components: {
    Vcode
  },

  methods: {
    // 进入注册页面
    go2register() {
      this.navigator("/register");
    },
    // 进入忘记密码页面
    go2recovery() {
      this.navigator("/recovery");
    },

    // 点击了登录按钮
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.isShow = true; //显示验证码模态框
        }
      });
    },

    // 用户通过了验证
    success(msg) {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      this.loading = false;
      login(this.loginForm)
        .then(resp => {
          // let token = resp.data.data.token;
          // setStore("token",token)
          if (resp.data.statusCode === "200") {
            let user = resp.data.data.user;
            setCookie("user", user);
            this.toast(resp.data.message, 1);
            this.navigator("/Home");
          }
        })
        .catch(data => {
          this.toast(data, 2);
        });
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
      this.loading = false;
    },

    // 获取手机验证码
    getTelCode() {
      // 发送之前验证是否填入手机号
      this.$refs.phoneForm.validateField("tel", err => {
        if (!err) {
          this.time = 60;
          this.timer();
          //发送手机验证码
          sendTelCode({ tel: this.phoneForm.tel })
            .then(resp => {
              if (resp.data.statusCode === "200") {
                this.toast(resp.data.message, 1);
              }
            })
            .catch(data => {
              this.toast(data, 2);
            });
        } else {
          this.disabled = false;
        }
      });
    },
    timer() {
      if (this.time > 0) {
        this.time--;
        this.btntxt = this.time + "s后重新获取";
        this.disabled = true;
        setTimeout(this.timer, 1000);
      } else {
        this.time = 0;
        this.btntxt = "获取验证码";
        this.disabled = false;
      }
    },

    // 快捷登录按钮
    fastLogin() {
      // 点击登录之前验证输入
      this.$refs.phoneForm.validate(valid => {
        if (valid) {
          loginByTel({
            tel: this.phoneForm.tel,
            inputCode: this.phoneForm.phonecode
          })
            .then(resp => {
              if (resp.data.statusCode === "200") {
                let user = resp.data.data.user;
                setCookie("user", user);
                this.toast(resp.data.message, 1);
                if (resp.data.data.isNew == "1") {
                  //1 表示新用户 跳转到信息完善界面
                  this.$router.push({
                    path: "/infoRefine",
                    query: { tel: this.phoneForm.tel } //携带到下个下个界面的参数
                  });
                  console.log("跳转到信息完善界面");
                } else {
                  this.navigator("/Home");
                }
              } else {
                this.toast(resp.data.message, 2);
              }
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

<style  lang="scss" >
/*#poster {*/
/*  background-image: url("../assets/loginBack.jpeg");*/
/*  background-repeat: no-repeat;*/
/*  background-size: cover;*/
/*  height: 120%;*/
/*  width: 120%;*/
/*  background-size: auto;*/
/*  // position: absolute;*/
/*  background-position: center;*/
/*  position: fixed;*/
/*}*/

.item-a {
  grid-area: header;
}
.item-b {
  grid-area: main;
}
.item-c {
  grid-area: sidebar;
}
.item-d {
  grid-area: footer;
}

.container {
  grid-template-columns: 50px 50px 50px 50px;
  grid-template-rows: auto;
  grid-template-areas:
    "header header header header"
    "main main . sidebar"
    "footer footer footer footer";
}

.til {
  margin-left: -65%;

  color: #2d73cc;
  font-weight: bolder;
  font-size: 60px;
}
.login_header span {
  cursor: pointer;
}
.active {
  color: #298adb;
}
.no_account {
  cursor: pointer;
  color: #298adb;
  float: left;
  padding-bottom: 30px;
  font-size: 16px;
}
.forget_password {
  cursor: pointer;
  color: #298adb;
  float: right;
  padding-bottom: 30px;
  font-size: 16px;
}
.background {
  width: 100%;
  height: 100%;
  background: url("../assets/background.png");
}
.box-card {
  margin: 0 auto;
  width: 480px;
  border: solid 1px white;
  border-radius: 20px;
  background-color: rgba($color: white, $alpha: 0.1);
}
.myLoginUI {
  position: absolute;
  top: 55%;
  left: 50%;
  margin: -280px 0 0 -240px;
  width: 480px;
  // border: solid 1px white;
  // border-radius: 20px;
}

.box-card:hover {
  background-color: rgba($color: white, $alpha: 0.6);
  transition: all 1s;
}
</style>
