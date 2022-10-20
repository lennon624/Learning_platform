<template>
  <div>
    <div style="background-color: #f7f7f7">
    <span class="til" >Dasabi</span>
    <span style="border-left:solid 1px #d9d9d9;margin-left: 70px;font-size: 50px;padding-left: 40px;color: grey">注册</span>
    </div>


    <div style="background-image: -webkit-linear-gradient(90deg, #3a4457, #F6EAB8);width: 100%;position: fixed;height: 100%;top: 90px;left: 0px">
    </div>
  <div class="myLoginUI">
    <el-card class="box-card">
      <h1 class="h1">注册</h1>
      <el-form
        ref="registerForm"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
        autocomplete="on"
        label-position="left"
      >
        <el-form-item class="register-form-item" prop="username">
          <el-input
            ref="username"
            v-model="registerForm.username"
            placeholder="请输入用户名"
            name="username"
            type="text"
            tabindex="1"
            autocomplete="on"
            prefix-icon="el-icon-user"
          />
        </el-form-item>

        <el-form-item class="register-form-item" prop="password">
          <el-input
            :key="passwordType"
            ref="password"
            v-model="registerForm.password"
            placeholder="请输入密码"
            name="password"
            :type="passwordType"
            tabindex="2"
            autocomplete="on"
            prefix-icon="el-icon-lock"
          />
        </el-form-item>

        <el-form-item class="register-form-item" prop="passwordAgain">
          <el-input
            :key="passwordType"
            ref="passwordAgain"
            v-model="registerForm.passwordAgain"
            placeholder="请确认密码"
            name="passwordAgain"
            :type="passwordType"
            tabindex="3"
            autocomplete="on"
            prefix-icon="el-icon-lock"
          />
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            ref="email"
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            name="email"
            type="text"
            tabindex="4"
            autocomplete="on"
            prefix-icon="el-icon-message"
          />
        </el-form-item>

        <el-form-item prop="emailCode">
          <el-input
            ref="emailCode"
            v-model="registerForm.emailCode"
            placeholder="请输入邮箱验证码"
            name="emailCode"
            type="text"
            tabindex="5"
            autocomplete="on"
            prefix-icon="el-icon-chat-dot-square"
            style="width: 67%; float: left;"
          />
          <el-button
            type="primary"
            plain
            style="float: right"
            :disabled="disabled"
            @click="getEmailCode()"
          >{{btntxt}}</el-button>
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;margin-bottom:20px;"
          @click.native.prevent="handleRegister"
        >注册</el-button>
      </el-form>
      <span class="back2login" @click="back2login">返回登录</span>

      <Vcode :show="isShow" @success="success" @close="close" />
    </el-card>
  </div>
  </div>
</template>

<script>
import Vcode from "vue-puzzle-vcode";
import { sendEmailCode, register } from "@/api/modules/user";
export default {
  name: "register",
  data() {
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

    var checkPasswordAgain = (rule, value, callback) => {
      let password = this.$refs.password.value;
      if (!value) {
        return callback(new Error("确认密码不能为空"));
      } else {
        if (value != password) {
          return callback(new Error("两次输入密码不一致"));
        }
        callback();
      }
    };

    //邮箱格式验证
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

    return {
      registerForm: {
        username: "",
        password: "",
        confirmpassword: "",
        email: "",
        emailCode: "",
        etc: {}
      },
      registerRules: {
        username: { validator: checkLength, trigger: "blur" },
        password: { validator: checkPassword, trigger: "blur" },
        passwordAgain: { validator: checkPasswordAgain, trigger: "blur" },
        email: { validator: userEmail, trigger: "blur" },
        emailCode: {
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
      type: "0" //邮件类型
    };
  },

  components: {
    Vcode
  },

  methods: {
    // 点击注册按钮弹出图片验证
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          console.log("通过了表单验证");
          this.loading = true;
          this.isShow = true; //显示验证码模态框
        }
      });
    },

    // 用户通过了验证
    success(msg) {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      this.loading = false;

      //邮箱验证码放入etc
      var etcinfo = {
        emailCode: ""
      };
      etcinfo.emailCode = this.registerForm.emailCode;
      this.registerForm.etc = JSON.stringify(etcinfo);

      //注册的请求发到后端
      register(this.registerForm)
        .then(resp => {
          this.toast(resp.data.message, 1);
          this.navigator("/login");
        })
        .catch(data => {
          this.toast(data, 2);
        });
      //
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
      this.loading = false;
    },

    // 发送邮箱验证码按钮
    getEmailCode() {
      this.$refs.registerForm.validateField("email", err => {
        if (!err) {
          this.time = 60;
          this.timer();

          // 注册邮件
          this.type = 0;

          // 向后端请求发送验证码
          sendEmailCode({ email: this.registerForm.email, type: this.type })
            .then(resp => {
              this.toast(resp.data.message, 1);
            })
            .catch(data => {
              console.log(data);
              this.toast(data, 2);
            });
        }
      });
    },

    // 定时器
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

    back2login() {
      this.navigator("/login");
    }
  }
};
</script>

<style  lang="scss">
.login_header span {
  cursor: pointer;
}
.active {
  color: #298adb;
}
.back2login {
  cursor: pointer;
  color: #298adb;
  float: right;
  padding-bottom: 30px;
  font-size: 18px;
}
.box-card {
  margin: 0 auto;
  width: 480px;
}
.myLoginUI {
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -280px 0 0 -240px;
  width: 480px;
}
</style>
