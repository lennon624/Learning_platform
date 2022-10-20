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
				<h1>重置密码</h1>
				<view class="tips-text">
					<h3>在下方输入你注册时的邮箱来接收重置密码邮件。</h3>
				</view>
			</view>

			<!-- 输入框 -->
			<view style="position: absolute; top: 480rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="text" v-model="email" border placeholder="你的邮箱..." />
			</view>

			<!-- 确认按钮 -->
			<view style="position: absolute; width: 80%; margin: 0 auto; bottom: 50rpx; left: 0; right: 0;">
				<u-button type="primary" shape="circle" @click="confirm()">确认</u-button>
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
		sendEmailCode
	} from '../../api/modules/user.js';
	export default {


		data() {
			return {
				email: ""
			}

		},
		methods: {
			// 确认按钮
			confirm() {
				// 检查输入合法性
				if (this.email == null || this.email == "") {
					this.toast("邮箱不能为空噢", "warning", "bottom")
				} else {
					console.log(this.email)
					const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
					if (!mailReg.test(this.email)) {
						this.toast("邮箱格式不正确，再检查下吧~", "warning", "bottom")
					} else {
						// 发送验证码
						sendEmailCode({
								email: this.email,
								type: "1"
							})
							.then(res => {
								if (res.data.success) {
									this.toast("邮件发送成功，请注意查收~", "success", "bottom")
									setTimeout(() => {
										this.navigator("./verify?email=" + this.email)
									}, 1500)
								} else {
									this.toast("邮件发送失败T^T", "error", "bottom")
								}
							})
							.catch(data => {
								this.toast("邮件发送失败T^T", "error", "bottom")
							})
					}
				}
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
