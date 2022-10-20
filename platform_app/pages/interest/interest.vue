<template>
	<view class="container">
		<view class="background">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>

			<!-- 提示语 -->
			<view class="tips">
				<h1>欢迎加入学习队伍 : )</h1>
				<view class="tips-text">
					<h3>告诉我你的你的偏好吧
						<br />在下方轻按选择你的兴趣</h3>
				</view>
			</view>

			<!-- 标签选项 -->
			<view style="margin-top: 500rpx; margin-left: 5%; margin-right: 5%;" v-for="(category, index) in categoryList" :key="index">
				<categoryTag :category="category" @increase="handleSelect" @reduce="handleDeselect"></categoryTag>
			</view>

			<!-- 确认按钮 -->
			<view style="position: absolute; width: 80%; margin: 0 auto; bottom: 50rpx; left: 0; right: 0;">
				<u-button type="primary" shape="circle" @click="confirm()">我选好了</u-button>
			</view>
		</view>
		<!-- 吐司 -->
		<view>
			<u-toast ref="uToast" />
		</view>
	</view>
</template>

<script>
	import {
		getChildCategory2
	} from '../../api/modules/category.js';

	import {
		addInterest,
		removeInterest
	} from '../../api/modules/student.js';

	import categoryTag from "./categoryTag.vue";

	export default {
		data() {
			return {
				categoryList: [],
				user: {}
			}
		},

		components: {
			categoryTag
		},

		mounted() {
			// 获取用户信息
			uni.getStorage({
				key: "user",
				success: (res => {
					this.user = res.data
					console.log(this.user)
				})
			})
			
			

			// 获取兴趣列表
			getChildCategory2({studentId: this.user.etc.id})
				.then(res => {
					if (res.data.success) {
						this.categoryList = res.data.data
					}
				})
		},

		methods: {
			confirm() {
				this.toast("选择成功~", "success", "bottom")
				setTimeout(() => {
					uni.reLaunch({
						url: "../index/index"
					})
				}, 1500)
			},

			// 选中标签
			handleSelect(category) {
				console.log("选中了兴趣" + category.categoryName)
				addInterest({
					childCategoryId: category.id,
					studentId: this.user.etc.id
				}).then(res => {
					// console.log(res.data)
				})
			},

			// 取消选中标签
			handleDeselect(category) {
				console.log("取消选中了兴趣" + category.categoryName)
				removeInterest({
					childCategoryId: category.id,
					studentId: this.user.etc.id
				}).then(res => {
					// console.log(res.data)
				})
			},

			// 返回上一级
			goBack() {
				uni.navigateBack({
					delta: 1
				});
			},

			// 进入登录页
			goLogin() {
				uni.navigateTo({
					url: '../login/login'
				})
			},

			// 回到首页
			goHome() {
				uni.reLaunch({
					url: "../index/index"
				})
				// uni.switchTab({
				// 	url: "../tabBar/home/home"
				// })
			}

		}
	}
</script>

<style scoped>
	/deep/ .uni-input-input {
		color: white;
	}

	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
	}

	.container {
		color: #FFFFFF;
	}

	.background {
		background-color: #001934;
		height: 100%;
		width: 100%;
		position: absolute;
		text-align: center;
	}

	.top-bar {
		position: absolute;
		width: 100%;
	}

	.tips {
		position: absolute;
		text-align: center;
		top: 250rpx;
		width: 100%;
	}

	.tips-text {
		width: 80%;
		margin: 0 auto;
	}
</style>
