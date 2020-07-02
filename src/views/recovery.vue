<template>
  <div>
    <div style="background-color: #f7f7f7">
    <span class="til" >Dasabi</span>
    <span style="border-left:solid 1px #d9d9d9;margin-left: 70px;font-size: 50px;padding-left: 40px;color: grey">找回密码</span>
    </div>


    <div style="background-image: -webkit-linear-gradient(90deg, #3a4457, #F6EAB8);width: 100%;position: fixed;height: 100%;top: 90px;left: 0px">
    </div>

  <div class="myLoginUI">
    <el-card class="box-card">
      <div v-show="cur==0">
        <h1 class="h1">找回密码</h1>
        <el-form
          ref="recoveryForm"
          :model="recoveryForm"
          :rules="recoveryRules"
          class="login-form"
          autocomplete="on"
          label-position="left"
        >
          <el-form-item prop="email">
            <el-input
              ref="email"
              v-model="recoveryForm.email"
              placeholder="请输入邮箱"
              name="email"
              type="text"
              tabindex="1"
              autocomplete="on"
              prefix-icon="el-icon-message"
            />
          </el-form-item>

          <el-form-item prop="emailCode">
            <el-input
              ref="password"
              v-model="recoveryForm.emailCode"
              type="text"
              placeholder="请输入邮箱验证码"
              name="emailCode"
              tabindex="2"
              autocomplete="on"
              prefix-icon="el-icon-chat-dot-square"
              style="width: 60%; float: left;"
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
            @click.native.prevent="submitEmailCode"
          >确定</el-button>
        </el-form>
      </div>

      <div v-show="cur==1" class="Cbody_item">
        <h1 class="h1">重置密码</h1>
        <el-form
          ref="resetForm"
          :model="resetForm"
          :rules="resetRules"
          class="login-form"
          autocomplete="on"
          label-position="left"
        >
          <el-form-item prop="newPassword">
            <el-input
              :key="passwordType"
              ref="newPassword"
              v-model="resetForm.newPassword"
              placeholder="请输入新密码"
              name="newPassword"
              :type="passwordType"
              tabindex="1"
              autocomplete="on"
              prefix-icon="el-icon-lock"
            />
          </el-form-item>

          <el-form-item prop="newPasswordAgain">
            <el-input
              :key="passwordType"
              placeholder="请确认新密码"
              ref="newPasswordAgain"
              v-model="resetForm.newPasswordAgain"
              name="newPasswordAgain"
              :type="passwordType"
              tabindex="2"
              autocomplete="on"
              prefix-icon="el-icon-lock"
            />
          </el-form-item>

          <el-button
            :loading="loading"
            type="primary"
            style="width:100%;margin-bottom:20px;"
            @click.native.prevent="handleReset"
          >确认重置</el-button>
        </el-form>
      </div>
      <span class="back2login" @click="back2login">返回登录</span>

      <Vcode :show="isShow" @success="success" @close="close" />
    </el-card>
  </div>
  </div>
</template>

<script>
import Vcode from "vue-puzzle-vcode";
import {
  sendEmailCode,
  checkEmailCode,
  resetPassword
} from "@/api/modules/user";
export default {
  name: "login",
  data() {
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
      let password = this.$refs.newPassword.value;
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
      recoveryForm: {
        email: "",
        emailCode: ""
      },
      resetForm: {
        newPassword: "",
        newPasswordAgain: ""
      },
      recoveryRules: {
        email: { validator: userEmail, trigger: "blur" },
        emailCode: {
          required: true,
          message: "验证码不能为空",
          trigger: "blur"
        }
      },
      resetRules: {
        newPassword: { validator: checkPassword, trigger: "blur" },
        newPasswordAgain: { validator: checkPasswordAgain, trigger: "blur" }
      },
      isShow: false, // 验证码模态框是否出现
      loading: false,
      passwordType: "password",
      redirect: undefined,
      time: 0, //验证码时间
      btntxt: "获取验证码",
      disabled: false,
      type: "0", //邮件类型
      cur: 0,
      user_id: ""
    };
  },

  components: {
    Vcode
  },

  methods: {
    // 提交邮箱验证码
    submitEmailCode() {
      checkEmailCode({
        email: this.recoveryForm.email,
        email_code: this.recoveryForm.emailCode
      })
        .then(resp => {
          if (resp.data.success) {
            // 验证成功
            this.toast(resp.data.message, 1);
            this.user_id = resp.data.data.id;
            this.cur = 1;
          }
        })
        .catch(data => {
          this.toast(data, 2);
        });
    },

    // 点击确认重置按钮弹出图片验证码
    handleReset() {
      this.$refs.resetForm.validate(valid => {
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

      //重置密码的请求发到后端
      resetPassword({
        user_id: this.user_id,
        newPassword: this.resetForm.newPassword
      })
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
      this.$refs.recoveryForm.validateField("email", err => {
        if (!err) {
          this.time = 60;
          this.timer();

          // 注册邮件
          this.type = 1;

          // 向后端请求发送验证码
          sendEmailCode({ email: this.recoveryForm.email, type: this.type })
            .then(resp => {
              this.toast(resp.data.message, 1);
            })
            .catch(data => {
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
