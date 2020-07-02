<template>
  <div class="one">
    <el-tabs type="border-card">
      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 个人设置
        </span>
        <el-form
          :model="dynamicValidateForm"
          :rules="settingRules"
          ref="dynamicValidateForm"
          label-width="100px"
          class="demo-dynamic"
        >
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="dynamicValidateForm.email"></el-input>
          </el-form-item>
          <el-form-item prop="tel" label="手机号">
            <el-input v-model="dynamicValidateForm.tel"></el-input>
          </el-form-item>
          <!-- <el-form-item prop="school" label="学校">
            <el-input v-model="dynamicValidateForm.school"></el-input>
          </el-form-item>-->
          <!-- <el-form-item prop="describe" label="个人介绍">
            <el-input v-model="dynamicValidateForm.describe"></el-input>
          </el-form-item>-->

          <el-form-item>
            <el-button type="primary" @click="submitForm('dynamicValidateForm')">提交</el-button>

            <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <!-- <el-tab-pane>
            <span slot="label"><i class="el-icon-date"></i> 手机设置</span>
            我的行程
            
        </el-tab-pane>
        <el-tab-pane>
            <span slot="label"><i class="el-icon-date"></i> 资料设置</span>
            我的行程
      </el-tab-pane>-->
    </el-tabs>
  </div>
</template>

<script>
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import { editSetting } from "@/api/modules/user";

export default {
  name: "edit",
  data() {
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
      dynamicValidateForm: {
        domains: [
          {
            value: ""
          }
        ],
        id: "",
        email: "",
        tel: ""
        // school:"",
        // describe:""
      },
      settingRules: {
        email: { validator: userEmail, trigger: "blur" },
        tel: { validator: checkPhone, trigger: "blur" }
      }
    };
  },
  mounted() {
    let user = JSON.parse(getCookie("user") || "{}");
    this.dynamicValidateForm.email = user.email;
    this.dynamicValidateForm.tel = user.tel;
    this.dynamicValidateForm.id = user.id;
    console.log(user);
    
    // console.log(typeof(user.id));
    
    // console.log(user);

    //    this.dynamicValidateForm.school = user.school
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //   console.log(cookie);
          this.$confirm("是否确认提交?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              // console.log(this.dynamicValidateForm.id);
              editSetting({
                id: this.dynamicValidateForm.id,
                email: this.dynamicValidateForm.email,
                tel: this.dynamicValidateForm.tel
              })
                .then(resp => {
                  if (resp.data.statusCode === "200") {
                    this.toast(resp.data.message, 1);
                  } else {
                    this.toast(resp.data.message, 2);
                  }
                })
                .catch(data => {
                  this.toast(data, 2);
                });

            //   this.$message({
            //     type: "success",
            //     message: "个人信息更新成功!"
            //   });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消提交"
              });
            });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removeDomain(item) {
      var index = this.dynamicValidateForm.domains.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.domains.splice(index, 1);
      }
    },
    addDomain() {
      this.dynamicValidateForm.domains.push({
        value: "",
        key: Date.now()
      });
    }
  }
};
</script>

<style scoped>
.one {
  border-style: solid;
  border-width: 0.1px;
  border-radius: 10px;
  border-color: #edeeef;
  background-color: white;
  height: 800px;
  width: 70%;
  margin: 0 auto;
  margin-top: 3%;
}
</style>