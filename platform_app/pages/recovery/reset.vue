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
				<h1>验证成功</h1>
				<view class="tips-text">
					<h3>在下方输入你的新密码吧。</h3>
				</view>
			</view>

			<!-- 输入框 -->
			<view style="position: absolute; top: 480rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="password" v-model="newPassword" border placeholder="新密码..." />
			</view>
			<view style="position: absolute; top: 580rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="password" v-model="passwordAgain" border placeholder="重复一次密码..." />
			</view>

			<!-- 确认按钮 -->
			<view style="position: absolute; width: 80%; margin: 0 auto; bottom: 50rpx; left: 0; right: 0;">
				<u-button type="primary" shape="circle" @click="confirm()">确认重置</u-button>
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
		resetPassword
	} from '../../api/modules/user.js';
	export default {
		data() {
			return {
				newPassword: "",
				passwordAgain: "",
				user_id: ""
			}
		},
		
		// 获取上个页面传递的参数
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.user_id = option.user_id
		},
		
		methods: {
			// 确认验证码
			confirm() {
				// 验证输入合法性
				if(this.newPassword == "" || this.newPassword == null) {
					this.toast("新密码不能为空噢~", "warning", "bottom")
				} else {
					if(this.newPassword.length < 6) {
						this.toast("密码不能小于6位数噢~", "warning", "bottom")
					} else {
						if(this.newPassword != this.passwordAgain) {
							this.toast("两次密码不一致噢~", "warning", "bottom");
						} else {
							// 重置密码
							resetPassword({
								user_id: this.user_id,
								newPassword: this.newPassword
							})
							.then(res => {
								if(res.data.success) {
									this.toast(res.data.message + "~", "success", "bottom");
									// 进入登录页
									setTimeout(() => {
										this.goLogin()
									}, 1500)
								} else {
									this.toast("密码重置失败T^T一定是哪里出了问题", "success", "bottom");
								}
							})
							.catch(data => {
								this.toast("密码重置失败T^T一定是哪里出了问题", "success", "bottom");
							})
						}
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
