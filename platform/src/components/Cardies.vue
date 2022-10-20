<template>
  <div>
    <section class="container">
      <div class="row active-with-click" v-if="this.noInterest == false">
        <div class="col-md-3 col-sm-6 col-xs-12" v-for="item in items" :key="item.id">
          <article class="material-card Indigo">
            <h2>
              <span>{{item.courseName}}</span>
              <strong>
                <i class="fa fa-fw fa-star"></i>
                {{item.teacherId}}
              </strong>
            </h2>
            <div class="mc-content">
              <div class="img-container">
                <img class="img-responsive" src="@/assets/cc.jpg" />
              </div>
              <div class="mc-description" style="height: 190px">{{item.introduction}}</div>
            </div>
            <a class="mc-btn-action" @click="changeClass($event,item)">
              <i class="fa fa-bars"></i>
            </a>
            <div class="mc-footer">
              <!-- <a class="fa fa-fw" style="width: 185px" v-show="!result" @click="changestatus">加入课程</a> -->
              <a class="fa fa-fw" style="width: 185px"  @click="goCourse(item)">查看详情</a>
            </div>
          </article>
        </div>
      </div>
      <div style="padding-bottom: 40px" v-if="this.noInterest">
        <h3>你还未选择兴趣噢 点击右上角头像→个人资料选择兴趣吧</h3>
      </div>
    </section>
  </div>
</template>

<script>
import { queryMyList } from "@/api/modules/student";
import { coursebyinterest, getpcategorybycourse } from "@/api/modules/course";
import { getCookie, setCookie, removeCookie } from "@/libs/cookie";
import $ from "jquery";
export default {
  name: "Cardies",
  data() {
    return {
      items: {
        result: ""
      },
      form: {
        studentId: "",
        courseId: ""
      },
      subcategory: "",
      id: "",
      result: false,
      studentid: "",
      myuser: "",
      list :'',
      noInterest: false,
    };
  },
  created() {
    //    self = this
    // window.change= self.change
  },

  mounted() {
    this.myuser = JSON.parse(getCookie("user") || "{}");
    this.studentid = this.myuser.etc.id;

    this.form.studentId = this.myuser.etc.id;

    this.getcbyi();
  },

  methods: {
    // //查询课程是否在选课表中
    // queryMyList1(item) {
    //   this.form.courseId = item.id;
    //   queryMyList(this.form)
    //     .then(resp => {  
    //       item.result = resp.data.data
    //       this.result = resp.data.data
    //       console.log(  this.result);
    //     })
    //     .catch(data => {
    //       this.toast(data, 2);
    //     });
    // },

    getcbyi() {
      //根据兴趣推荐课程
      coursebyinterest({ student_id: this.studentid })
        .then(resp => {
          this.items = resp.data.data;
          console.log("this.items");
          console.log(this.items);
          //  this.queryMyList1()
        })
        .catch(data => {
          this.noInterest = true;
          console.log(this.noInterest);
          // this.toast(data, 2);
        });
    },
    // //改变课程状态
    // changestatus() {
    //   this.$message({
    //     message: "加入课程成功",
    //     type: "success"
    //   });
    //   this.result = !this.result;
    // },
    //跳转兴趣课程
    goCourse(row) {
      //根据课程Id获取子类别

      getpcategorybycourse({ course_id: row.id })
        .then(res => {
          this.subcategory = res.data.data;
          this.id = this.subcategory.parent_category_info.id;
          console.log(this.subcategory.parent_category_info.id);
          this.navigator("/index/" + this.id + "/" + row.id);
        })
        .catch(err => {
          this.toast(err, 2);
        });
    },

    // 点击卡片上的按钮，动态更改卡片样式
    changeClass(e, item) {
       //查询课程状态
    // this.queryMyList1(item);
      //动态更改卡片样式
      var that = e.currentTarget;
      var card = $(that).parent(".material-card");
      var icon = $(that).children("i");
      icon.addClass("fa-spin-fast");

      if (card.hasClass("mc-active")) {
        card.removeClass("mc-active");
        window.setTimeout(function() {
          icon
            .removeClass("fa-arrow-left")
            .removeClass("fa-spin-fast")
            .addClass("fa-bars");
        }, 800);
      } else {
        card.addClass("mc-active");
        window.setTimeout(function() {
          icon
            .removeClass("fa-bars")
            .removeClass("fa-spin-fast")
            .addClass("fa-arrow-left");
        }, 800);
      }
    }
  }
};
</script>

<style scoped>
@import "../dist/material-cards.css";
@import "http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css";
@import "http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css";
@import "http://fonts.googleapis.com/css?family=Raleway:400,300,200,500,600,700";
</style>
