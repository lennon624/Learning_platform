<template>
  <!-- 题目讨论区模块 -->
  <div class="one">
    <h3 class="top">课程讨论</h3>
    <!-- <el-divider></el-divider> -->
    <el-card class="box-card" shadow="never">
      <div style="margin: 20px 0;"></div>
      <div class="talking">
        <!-- 文本输入和提交 -->
        <el-form>
          <el-form-item>
            <el-input
              type="textarea"
              placeholder="我有话说..."
              v-model="talikingTxt"
              @focus="inputRowsHandle(talkingInputRows)"
              @blur="inputRowsHandle(talkingInputRows)"
              :rows="talkingInputRows"
              resize="none"
              style="width:60%;margin-right:10px"
            ></el-input>
            <el-button type="primary" @click="reportHandle">发表讨论</el-button>
          </el-form-item>
        </el-form>

        <!-- 讨论区内容 -->
        <div class="talking-area">
          <div class="talking-area-title-containers">
            <!-- <div class="talking-area-title" @click="tabCur=0" :class="{activetab:tabCur==0}">本题讨论</div> -->
            <div class="talking-area-title" @click="tabCur=1" :class="{activetab:tabCur==1}">热门讨论</div>
          </div>

          <div class="talking-items">
            <!-- 没讨论时的提示语 -->
            <div
              v-if="talkingList== null"
              style="margin: 50px auto;color:grey"
            >赶紧发表本课程第一条讨论，与旁友们一起激情交流吧~</div>
            <!-- 一级讨论 -->
            <div class="talking-single" v-for="item in talkingList" :key="item.id">
              <div class="username-and-content item-username">{{item.username}}</div>
              <div class="username-and-content item-content">{{item.talkingContent}}</div>
              <div style="position:relative;line-height:30px">
                <div style="position:absolute;left:2%;color:#bdbdbd;font-size:15px;">{{item.date}}</div>
                <div style="position:absolute;right:1%;font-size:15px">
                  <span
                    class="good-and-reply-btn"
                    @click="replyBtnHandle(item)"
                    style="cursor:pointer"
                  >
                    <i v-if="currentReplyId != item.id" class="el-icon-chat-dot-square"></i>
                    <span v-if="currentReplyId != item.id">回复</span>
                    <i
                      v-if="currentReplyId == item.id"
                      class="el-icon-chat-dot-square"
                      style="color:#85c4ff;"
                    ></i>
                    <span v-if="currentReplyId == item.id" style="color:#85c4ff;">收起</span>
                  </span>
                  <span v-if="item.replyUserId == user.id">
                    |
                    <el-popconfirm
                      title="确定要删除此讨论或者讨论下的回复嘛？"
                      confirmButtonText="确定"
                      cancelButtonText="不了不了"
                      icon="el-icon-info"
                      iconColor="red"
                      @onConfirm="deleteTalkingHandle(item.id)"
                      style="cursor:pointer"
                    >
                      <span slot="reference" class="delete-talking">删除</span>
                    </el-popconfirm>
                  </span>
                </div>
              </div>
              <!-- 回复框 -->
              <div class="reply" v-if="item.id == currentReplyId">
                <el-input
                  class="reply-txt"
                  type="textarea"
                  rows="3"
                  resize="none"
                  v-model="replyTxt"
                  :placeholder="getPlaceHolder(item)"
                ></el-input>
                <el-button style="margin:10px 1px 0 0" @click="replySubmitHandle(item)">提交回复</el-button>
              </div>
              <!-- 二级讨论 -->
              <div
                class="talking-single"
                v-for="item in item.replys"
                :key="item.id"
                style="margin:30px;margin-right:1px"
              >
                <div class="username-and-content item-username">
                  {{item.username}}
                  <span style="color:#c1c5ce">回复</span>
                  @{{item.username1}}
                </div>
                <div class="username-and-content item-content">{{item.talkingContent}}</div>
                <div style="position:relative;line-height:30px">
                  <div style="position:absolute;left:2%;color:#bdbdbd;font-size:15px;">{{item.date}}</div>
                  <div style="position:absolute;right:1%;font-size:15px">
                    <span
                      class="good-and-reply-btn"
                      @click="replyBtnHandle(item)"
                      style="cursor:pointer"
                    >
                      <i v-if="currentReplyId != item.id" class="el-icon-chat-dot-square"></i>
                      <span v-if="currentReplyId != item.id">回复</span>
                      <i
                        v-if="currentReplyId == item.id"
                        class="el-icon-chat-dot-square"
                        style="color:#85c4ff;"
                      ></i>
                      <span v-if="currentReplyId == item.id" style="color:#85c4ff;">收起</span>
                    </span>
                    <span v-if="item.replyUserId == user.id">
                      |
                      <el-popconfirm
                        title="确定要删除此讨论或者讨论下的回复嘛？"
                        confirmButtonText="确定"
                        cancelButtonText="不了不了"
                        icon="el-icon-info"
                        iconColor="red"
                        @onConfirm="deleteTalkingHandle(item.id)"
                        style="cursor:pointer"
                      >
                        <span slot="reference" class="delete-talking">删除</span>
                      </el-popconfirm>
                    </span>
                  </div>
                </div>
                <!-- 回复框 -->
                <div class="reply" v-if="item.id == currentReplyId">
                  <el-input
                    class="reply-txt"
                    type="textarea"
                    rows="3"
                    resize="none"
                    v-model="replyTxt"
                    :placeholder="getPlaceHolder(item)"
                  ></el-input>
                  <el-button style="margin:10px 1px 0 0" @click="replySubmitHandle(item)">提交回复</el-button>
                </div>
              </div>
              <el-divider style="margin:20px"></el-divider>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getStore, setStore, removeStore } from "@/libs/localstorage";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import {
  reportTalking,
  getTalkings,
  replyTalking,
  deleteTalking,
} from "@/api/modules/talkings";

export default {
  props: {
    questionId: {
      type: String,
      default: "",
    },
    isMyCollection: {
      //用于控制在收藏夹模块收藏题目的时候，关闭讨论区刷新题目
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      courseId: "",
      talkingList: [],
      user: "", //存储cookie的user
      drawer1: false, //控制讨论区抽屉
      innerDrawer1: false, //控制讨论区的多层嵌套
      //讨论区的搜索表单
      formInline: {
        searchContent: "",
      },
      talikingTxt: "", //讨论区输入内容
      talkingInputRows: 2,
      replyBtnTxt: "回复", //
      replyTxt: "",
      currentReplyId: 0, //0为没有聚焦在任何一条评论上
      tabCur: 1, //切换讨论区tab(全部/单个题目)
    };
  },
  watch: {
    tabCur(val) {
      var myQuestionId;
      if (this.tabCur == 1) {
        myQuestionId = "";
      } else {
        myQuestionId = this.questionId;
        // console.log();
      }
      this.getTalkings1(myQuestionId);
    },
  },
  created() {
    this.$nextTick(() => {
      // 开启右键
      document.oncontextmenu = new Function("event.returnValue=true");
      // 开启选择
      document.onselectstart = new Function("event.returnValue=true");
    });
  },
  mounted() {
    //user cookie
    let user = JSON.parse(getCookie("user") || "{}");
    this.user = user;
    console.log(user);
    //获得课程id
    this.courseId = this.$route.params.course; //获取课程ID参数
    //获得讨论列表
    this.getTalkings1(this.courseId);
  },
  methods: {
    //获得讨论列表(全部或者单个题目的，watch的tabCur已经过滤)
    getTalkings1(courseId) {
      getTalkings({ courseId: courseId })
        .then((resp) => {
          if (resp.data.statusCode == "200") {
            this.talkingList = resp.data.data;
            console.log(this.talkingList);
          }
        })
        .catch((data) => {
          this.toast(data, 2);
        });
    },
    inputRowsHandle(talkingInputRows) {
      if (talkingInputRows == 2 || this.talikingTxt != "") {
        this.talkingInputRows = 4;
      } else {
        this.talkingInputRows = 2;
      }
    },
    goodBtnHandle() {},
    //点击讨论区的题目收藏此题
    collectHandle(questionId) {
      // console.log(questionId);
      collectQuestionInTalking({
        userId: this.user.id,
        questionId: questionId,
      })
        .then((resp) => {
          if (resp.data.statusCode == "200") {
            this.notify(resp.data.message, 1);
          }
        })
        .catch((data) => {
          this.notify(data, 3);
        });
    },
    //点击回复
    replyBtnHandle(item) {
      if (this.currentReplyId == item.id) {
        this.currentReplyId = 0;
      } else {
        this.currentReplyId = item.id;
      }
    },
    getPlaceHolder(item) {
      return "回复" + item.username;
    },
    //发表讨论
    reportHandle() {
      if (this.talikingTxt != "") {
        reportTalking({
          userId: this.user.id,
          courseId: this.courseId,
          talkingContent: this.talikingTxt,
        })
          .then((resp) => {
            if (resp.data.statusCode == "200") {
              this.notify(resp.data.message, 1);
              this.getTalkings1(this.courseId); //刷新
              this.talikingTxt = ""; //清空
              this.talkingInputRows = 2; //变成两行
            }
          })
          .catch((data) => {
            this.toast(data, 2);
          });
      }
    },
    //提交回复评论
    replySubmitHandle(item) {
      if (this.replyTxt != "") {
        if (item.talkingIdParent == undefined) {
          //回复自己
          var talkingIdParent = item.id;
        } else {
          var talkingIdParent = item.talkingIdParent;
        }
        replyTalking({
          userId: this.user.id,
          replyUserId: item.replyUserId, //回复的用户id，即将变成被回复的用户id
          talkingIdParent: talkingIdParent,
          replyTxt: this.replyTxt,
        })
          .then((resp) => {
            // console.log(resp);
            if (resp.data.statusCode == "200") {
              this.notify(resp.data.message, 1);
              this.getTalkings1(this.courseId); //刷新讨论
              (this.replyTxt = ""), //清空
                (this.currentReplyId = 0); //关闭回复框
            }
          })
          .catch((data) => {
            this.toast(data, 2);
          });
      }
    },
    //删除讨论（可能是子讨论也可能是根级讨论 需要区分
    deleteTalkingHandle(talkingId) {
      deleteTalking({ talkingId: talkingId })
        .then((resp) => {
          // console.log(resp);
          if (resp.data.statusCode == "200") {
            this.notify(resp.data.message, 1);
            this.getTalkings1(this.courseId); //刷新讨论
            this.replyTxt = "", //清空
            this.currentReplyId = 0; //关闭回复框
          }
        })
        .catch((data) => {
          this.toast(data, 2);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.top {
  margin-right: 1100px;
}
.one {
  width: 100%;
  border-style: solid;
  border-width: 0.1px;
  border-radius: 10px;
  border-color: #edeeef;
  background-color: white;
  margin-left: 0px;
}
.box-card {
  height: 100%;
  width: 100%;
  margin: 0 auto;
  border: none;
  border-radius: 0;
  border-top: solid 2px #edeeef;
}

// drawer滚动条 法一
.el-drawer__body {
  overflow: auto;
  /* overflow-x: auto; */
}

/*2.隐藏滚动条，太丑了*/
.el-drawer__container ::-webkit-scrollbar {
  display: none;
}

// // drawer滚动条 法二
// .el-drawer.rtl {
//   overflow: scroll;
// }

.talking-btn {
  position: absolute;
  right: 0px;
  width: 59px;
  font-size: 22px;
  border-top-left-radius: 25px;
  border-bottom-left-radius: 25px;
  top: 20%;
  height: 110px;
  line-height: 30px;
}

.talking-area {
  margin: 20px auto;
  width: 100%;
  //   height: 2000px;
  //   border: solid 1px red;
}

.talking-area-title-containers {
  margin: auto;
  width: 90%;
  text-align: left;
}

.talking-area-title {
  cursor: pointer;
  display: inline-block;
  position: relative;
  padding-bottom: 10px;
  margin-bottom: -3px;
  font-size: 20px;
  margin-right: 10px;
  color: #bfbfbf;
  z-index: 10;
  // transition: 2s ease-out;
}

.activetab {
  color: #8fc6f9;
  border-bottom: 3px solid #8fc6f9;
}

.talking-items {
  width: 90%;
  height: 450px;
  overflow: scroll;
  margin: auto;
  border: solid 1px #e8e8e8;
  border-top: 3px solid #e8e8e8;
}

.talking-single {
  position: relative;
  margin: 10px;
  padding-bottom: 10px;
  //   border-bottom: solid #e8e8e8 1px;
}

.talking-single:hover {
  // // border: solid 0px #dcdfe7;
  // border-radius: 10px;
  // box-shadow: rgba(0,0,0,0.2) 0px 10px 10px;
  // transition: 0.2s;
}

.question {
  cursor: pointer;
  width: 70%;
  margin-left: 15%;
  margin-bottom: 20px;
  color: yellowgreen;
  font-size: 15px;
  border: yellowgreen 1px dotted;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}

.question:hover {
  box-shadow: rgba(yellowgreen, 0.2) -4px 4px 3px;
  border-style: solid;
  transition: 0.3s;
}

.username-and-content {
  text-align: left;
  padding: 0 10px;
}
.item-username {
  color: #85c4ff;
}

.el-divider--horizontal {
  margin: 33px 0 0 0;
}

.reply {
  width: 98%;
  margin: 30px auto;
}

.good-and-reply-btn:hover {
  color: #85c4ff;
  transition: 400ms;
}

.delete-talking:hover {
  color: red;
  transition: 400ms;
}
</style>