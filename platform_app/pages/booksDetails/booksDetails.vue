<template>
	<view class="books-details" style="height: 1000rpx;">
		<u-navbar back-text="课程详情"></u-navbar>
		<!-- 1. 课程介绍 -->
		<view class="book-detail">
			<view class="detail-left">
				<!-- <image class="book-img" src="https://s1.ax1x.com/2020/05/29/tmXxvF.jpg" />
				 -->
				<image class="book-img" :src="course.imgSrc" />
			</view>
			<view class="detail-right">
				<view class="detail-right-box">
					<view class="title">{{ course.courseName }}</view>
					<!-- <view class="author">任课教师:{{ course.etc.teacher_name }}</view> -->
					<view class="tag">
						<!-- <view class="tag-title">{{ pcategory.child_category_info.categoryName }}</view>
						<view class="tag-title">{{ pcategory.parent_category_info.categoryName }}</view> -->
					</view>
					<!-- <view class="grade">
						<u-rate active-color="#FFB32F" current="5" :disabled='true' />
					</view> -->
				</view>
				<view class="btn-box">

					<!-- <view class="readBtn" v-show="result" @click="govideo">继续学习</view>
					<button v-show="result" @click="removecourse">取消选课</button> -->
					<!-- <view class="readBtn" v-show="result" @click="govideo">继续学习</view>
					 <button class="quxiao"  v-show="result" @click="removecourse">取消选课</button> -->
					<view class="readBtn" v-show="result" @click="govideo">
						继续学习
					</view>
					
					 <view class="joinBtn" v-show="result" @click="removecourse">取消选课</view>

					<view class="joinBtn" v-show="!result" @click="changestatus">添加课程</view>
				</view>
			</view>
		</view>
		<!-- 课程介绍 -->
		
		<view class="kejieshao">课程介绍</view>
		<view class="book-jieshao">
			<u-read-more :toggle="true" show-height="76" :shadow-style="jieshaStyle" color="#0086E7" close-text="展开阅读全文" font-size="26">
				<rich-text class="jiesha" :nodes="course.introduction"></rich-text>
			</u-read-more>
		</view>
		<!-- 2. 目录 -->
		<view style="font-size: 32upx;font-weight: 700;margin: 30upx auto;">课程目录</view>
		<view class="" style="margin: 30rpx auto;">
			<course-catalog :courseID="courseId"></course-catalog>
		</view>
		<!-- 3. 讨论-->
		<view class="review">
			<view class="review-head">课程讨论</view>
			<scroll-view scroll-y="true" style="height: 520rpx;">
				<talking-main ref="talkList" :courseId="courseId" notShowRptBtn="true"></talking-main>
			</scroll-view>
		</view>
		<!-- toast -->
		<view><u-toast ref="uToast" /></view>
	</view>
</template>

<script>
import { getparentcategory } from '@/api/modules/category';
import { getCourseById, getpcategorybycourse, coursebyinterest } from '@/api/modules/course.js';
import { queryMyList, addCourse2List, removeCourseFromList } from '@/api/modules/student';
import talkingMain from '@/pages/talkings/talking-main.vue';
import CourseCatalog from '@/components/course-catalog/course-catalog.vue';

export default {
	components: {
		talkingMain,
		CourseCatalog
	},
	data() {
		return {
			course: '',
			myuser: '',
			myform: {
				studentId: '',
				courseId: ''
			},
			courseId: '',
			result: false,
			pcategory: '',
			iconOFF: false, //爱心激活开关
			jieshaStyle: {
				backgroundImage: 'linear-gradient(-180deg, rgba(255, 255, 255, 0) 0%, #fff 80%)',
				paddingTop: '80rpx',
				marginTop: '-80rpx'
			}
		};
	},
	onLoad() {
		
	},
	onShow() {
		

	},
	created() {
		uni.getStorage({
			key: 'course',
			success: res => {
				this.course = res.data;
				this.courseId = this.course.id;
				console.log(this.courseId);
			}
		});
		uni.getStorage({
			key: 'user',
			success: res => {
				this.myuser = res.data;
				this.myform.studentId = this.myuser.etc.id;
				this.myform.courseId = this.courseId;
			}
		});
	},
	mounted() {
		
		
		this.getpcategorybycourse(this.course.id);
		//查询课程是否在选课表中
		this.queryMyList1();
	},
	methods: {
		//取消选课
		removecourse() {
			removeCourseFromList({
				student_id: this.myform.studentId,
				course_id: this.myform.courseId
			})
				.then(res => {
					console.log(res);
					this.toast('取消选课成功');
				})
				.catch(err => {
					this.toast(err, 'error');
				});
			this.result = !this.result;
		},

		//改变课程状态
		changestatus() {
			// this.$message({
			//   message: "加入课程成功",
			//   type: "success"
			// });

			addCourse2List({
				student_id: this.myform.studentId,
				course_id: this.myform.courseId
			})
				.then(res => {
					console.log(res);
				})
				.catch(err => {
					this.toast(err, 'error');
				});
			this.result = !this.result;
		},
		//查询课程是否在选课表中
		queryMyList1() {
			queryMyList(this.myform)
				.then(resp => {
					if (resp.data.data === 'true') 
					this.result = !this.result;
				})
				.catch(data => {
					this.toast(data, 2);
				});
		},
		//查询课程对应子类别
		getpcategorybycourse(id) {
			getpcategorybycourse({
				course_id: id
			})
				.then(res => {
					this.pcategory = res.data.data;
				})
				.catch(err => {
					this.toast(err, 2);
				});
		},

		//查询父类别
		getcategory() {
			getparentcategory()
				.then(res => {
					console.log(res);

					this.items = res.data.data;
				})
				.catch(err => {
					this.toast(err, 2);
				});
		},

		govideo() {
			uni.navigateTo({
				url: '../video/video?SRC='+'http://v.stu.126.net/mooc-video/nos/mp4/2016/09/23/1004990139_87f2555950b74ba7a77ec09dd8207465_sd.mp4'
			});
		},

		rateBtn(value) {
			console.log(value);
		},
		iconActive() {
			this.iconOFF = !this.iconOFF;
		}
	},
	
};
</script>

<style lang="scss" scoped>
.books-details {
	padding: 0 $bleed $navBoxHeight $bleed;
}

.book-detail {
	display: flex;
	padding-top: 40upx;

	.detail-left {
		flex: 1;
		min-width: 270upx;
		max-width: 270upx;
		height: 340upx;

		.book-img {
			width: 240upx;
			height: 340upx;
			border-radius: 12upx;
			background-color: $skeletonColor;
		}
	}

	.detail-right {
		height: 340upx;
		padding: 4upx 0;

		.detail-right-box {
			height: calc(100% - 64upx);
		}

		.title {
			font-size: 36upx;
			font-weight: 700;
			overflow: hidden;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			margin-bottom: 10upx;
		}

		.author {
			font-size: 26upx;
			color: $mediumGrey;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 1;
			-webkit-box-orient: vertical;
			padding-bottom: 20upx;
		}

		.tag {
			padding-bottom: 5upx;

			.tag-title {
				background-color: #eff5fd;
				height: 48upx;
				line-height: 48upx;
				color: $mediumGrey;
				padding: 0 20upx;
				margin-right: 20upx;
				border-radius: 4upx;
				display: inline-block;
				font-size: 24upx;
			}
		}

		// .grade {
		// 	padding-top: 4upx;
		// }

		.btn-box {
			.readBtn {
				background-color: $dominantHue;
				border: 1px solid $dominantHue;
				padding: 0 30upx;
				display: inline-block;
				height: 64upx;
				line-height: 64upx;
				border-radius: 4upx;
				color: #fff;
				cursor: pointer;
				font-size: 28upx;
				margin-right: 26upx;
			}

			.readBtn:active {
			}

			.joinBtn {
				border: 1px solid $dominantHue;
				color: $dominantHue;
				padding: 0 29upx;
				height: 64upx;
				line-height: 64upx;
				display: inline-block;
				border-radius: 4upx;
				cursor: pointer;
				font-size: 28upx;
			}
		}
	}
}

.book-jieshao {
	padding: 40upx 0 30upx 0;
	overflow: hidden;

	.jiesha {
		width: 100%;
		word-wrap: break-word;
		color: $mediumGrey;
		line-height: 48upx;
		font-size: 26upx;
	}
}

.catalogue-box {
	height: 96upx;
	line-height: 96upx;
	padding: 0 20upx;
	background-color: $lightBlue;
	border-radius: 4upx;
	font-size: 28upx;
	display: flex;
	margin-bottom: 60upx;

	.catalogue-left {
		flex: 1;
		max-width: 80upx;
	}

	.catalogue-centre {
		flex: 1;
		color: $mediumGrey;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}

	.catalogue-right {
		flex: 1;
		max-width: 80upx;
		text-align: right;
	}
}

.review {
	.review-head {
		font-size: 32upx;
		font-weight: 700;
		margin-bottom: 30upx;
	}

	.review-input {
		$review: 84upx;
		height: $review;
		border-radius: 8upx;
		border: 1px solid $dominantHue;
		margin-bottom: 60upx;

		input {
			height: $review - 2upx;
			line-height: $review - 2upx;
			padding: 0 20upx;
			float: left;
			width: calc(100% - 112upx);
			font-size: 28upx;
			box-sizing: border-box;
		}

		.review-btn {
			float: right;
			height: $review - 2upx;
			line-height: $review - 2upx;
			width: 112upx;
			background-color: $dominantHue;
			text-align: center;

			.send {
				color: #fff;
				font-size: 48upx;
			}
		}
	}
}

.review-list {
	.review-box {
		$imgWidth: 96upx;
		padding-bottom: 40upx;

		.user-message {
			$userH: 76upx;
			display: flex;
			height: $userH;

			.message-img {
				flex: 1;
				height: $userH;
				min-width: $imgWidth;
				max-width: $imgWidth;

				.user-img {
					width: $userH;
					height: $userH;
					border-radius: 50%;
					background-color: $skeletonColor;
				}
			}

			.message-name {
				flex: 1;

				.head {
					font-size: 28upx;
					height: 44upx;
				}

				.star {
					height: 38upx;
					padding-top: 2upx;
				}
			}

			.message-praise {
				flex: 1;
				line-height: $userH - 20upx;
				min-width: 40upx;
				max-width: 40upx;
				text-align: right;
				padding: 10upx 0;

				.icon-active {
					.love {
						color: $redAll;
					}
				}

				.love {
					font-size: 34upx;
					color: #a3b0c0;
				}
			}
		}

		.con-tent {
			padding: 14upx 0 0 $imgWidth;

			.text {
				word-wrap: break-word;
				font-size: 26upx;
				line-height: 40upx;
				color: $mediumGrey;
			}

			.time {
				padding: 10upx 0 0 0;
				color: $lightGray;
				font-size: 24upx;
			}
		}
	}
	
	.quxiao{
		font-size: 28rpx;
		width: 170rpx;
		display: flex;
		float: right;
	}
	
	.kejieshao{
		font-size: 32upx;
		font-weight: 700;
		margin-bottom: -20upx;
		margin-top: 10rpx;
	}
}
</style>
