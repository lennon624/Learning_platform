<template>
	<view class="container">
		<view class="background">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>

			<!-- 导航栏 -->
			<view class="top-bar">
				<u-row gutter="16" justify="center">
					<u-col span="2">
						<view style="margin-left: 25rpx;">
							<u-icon name="close" size="40" @click="goHome()"></u-icon>
						</view>
					</u-col>
					<u-col span="8">
						<view style="font-size: 40rpx; text-align: center;">
							<text>Dasabi</text>
						</view>
					</u-col>
					<u-col span="2">
						<view style="font-size: 35rpx; text-align: center; margin-right: 25rpx;" @click="goLogin()">
							<text>登录</text>
						</view>
					</u-col>
				</u-row>
			</view>

			<!-- 提示语 -->
			<view class="tips">
				<h1>真不错</h1>
				<view class="tips-text">
					<h3>完成最后一步，加入我们，开启全新的学习旅途。现在完善你的个人信息吧！</h3>
				</view>
			</view>

			<!-- 输入框 -->
			<view style="position: absolute; top: 480rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="text" v-model="registerForm.username" border placeholder="你的用户名..." />
			</view>
			<view style="position: absolute; top: 580rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="password" v-model="registerForm.password" border placeholder="你的密码..." />
			</view>
			<view style="position: absolute; top: 680rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="password" v-model="registerForm.passwordAgain" border placeholder="重复一次密码..." />
			</view>

			<!-- 确认按钮 -->
			<view style="position: absolute; width: 80%; margin: 0 auto; bottom: 50rpx; left: 0; right: 0;">
				<u-button type="primary" shape="circle" @click="confirm()">马上开始</u-button>
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
		register
	} from '../../api/modules/user.js';
	export default {

		data() {
			return {
				registerForm: {
					username: "",
					password: "",
					passwordAgain: "",
					email: "",
					emailCode: ""
				},
				etc: {
					emailCode: ""
				}
			}

		},

		// 获取上个页面传递的参数
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.registerForm.email = option.email;
			this.etc.emailCode = option.code;
			this.registerForm.etc = JSON.stringify(this.etc)
		},

		methods: {
			// 确认注册
			confirm() {
				// 判断输入合法性
				if (this.registerForm.username == "" || this.registerForm.username == null) {
					// 用户名为空
					this.toast("用户名不能为空噢~", "warning", "bottom");
				} else {
					if (this.registerForm.password == "" || this.registerForm.passwordAgain == null) {
						// 密码为空
						this.toast("密码不能为空噢~", "warning", "bottom");
					} else {
						if (this.registerForm.password != this.registerForm.passwordAgain) {
							// 密码不一致
							this.toast("两次密码不一致噢~", "warning", "bottom");
						} else {
							// 输入合法
							register(this.registerForm)
								.then(res => {
									if (res.data.success) {
										this.toast(res.data.message + "~", "success", "bottom");
										uni.setStorageSync('user', res.data.data);
										setTimeout(() => {
											uni.navigateTo({
												url: "../interest/interest",
												animationType: "pop-in",
												animationDuration: 200
											})
										}, 1500)
									}

								})
								.catch(data => {
									this.toast(data, "error", "bottom");
								})
						}
					}
				}


			},

			// 重新发送验证码
			resend() {

				hello({
						text: "hello"
					})
					.then(res => {
						this.toast(res.data.message, "success", "bottom");
					})
					.catch(res => {

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
				uni.switchTab({
					url: '../tabBar/home/home'
				})
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
