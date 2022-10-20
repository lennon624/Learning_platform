<template>
	<view class="container">
		<!-- 顶栏 -->
		<u-navbar height="44" :is-back="true" back-icon-color="white" title="发表讨论" title-color="white" :background="barBackground">
			<view slot="right" @tap="reportHandle" style="color: white;font-size: 35rpx;margin-right: 20rpx;">发表</view>
		</u-navbar>
		<!-- input框 -->
		<view class="">
			<u-row gutter="16" justify="center">
				<u-col style="height: 100%;">
					<!-- <view style="width: 100%;height: 100%;"> -->
					<u-input v-model="reportTxt" type="textarea" height="1000" />
					<!-- </view> -->
				</u-col>
			</u-row>
		</view>
		<!-- 哪个课程的提示按钮 -->
		<u-button class="course-name" size="medium" shape="circle">课程：{{ courseName }}</u-button>
		<!-- toast -->
		<u-toast ref="uToast" />
	</view>
</template>

<script>
import { reportTalking } from '@/api/modules/talkings';
export default {
	components: {},
	data() {
		return {
			user:"",
			reportTxt: '',
			courseName: '',
			courseId:'',
			barBackground: {
				// backgroundColor: '#001f3f'
				// 渐变色
				backgroundImage: 'linear-gradient(45deg, #22a54c, #244f98)'
			}
		};
	},
	onLoad: function(option) {
		//option为object类型，会序列化上个页面传递的参数
		// console.log(option.courseName); //打印出上个页面传递的参数。
		// console.log(option.courseId); //打印出上个页面传递的参数。
		this.courseName = option.courseName;
		this.courseId = option.courseId;
	},
	onShow() {
		let that = this;
		uni.getStorage({
			key: "user",
			success: function(res) {
				if (res.data == null) {
				} else {
					that.userId = res.data.id
					that.user = res.data
				}
			}
		})
	},
	methods: {
		reBack() {
			uni.navigateBack({
				delta: 1
			});
		},
		//发表讨论
		reportHandle() {
			var that = this
			if (this.reportTxt != '') {
				console.log(this.user)
				reportTalking({
					userId: this.user.id,
					// courseId: this.courseId,
					courseId: this.courseId,
					talkingContent: this.reportTxt
				})
					.then(resp => {
						if (resp.data.statusCode == '200') {
							this.toast(resp.data.message,'success', 'center')
							// this.$refs.uToast.show({
							// 					title: '成功发表讨论~',
							// 					type: 'success',
							// 					url: './talking-main.vue'
							// 				})
							this.reportTxt = ''; //清空
							this.goBack();
						}
					})
					.catch(data => {
						this.toast(data, 'fail', 'center');
					});
			}
		}
	}
};
</script>

<style>
.course-name {
	position: absolute;
	right: 5%;
	bottom: 1%;
	background-color: #1b3767;
	font-size: 35rpx;
	color: white;
}
</style>
