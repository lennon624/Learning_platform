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
				<h1>确认你的邮箱验证码</h1>
				<view class="tips-text">
					<h3>我们已经向你的邮箱发送了一个验证码，记得查看邮箱的新邮件。</h3>
				</view>
			</view>

			<!-- 输入框 -->
			<view style="position: absolute; top: 480rpx; width: 80%; margin: 0 auto; left: 0; right: 0;">
				<u-input type="text" v-model="code" border placeholder="你的邮箱验证码..." />
				<view style="text-align: right; margin-top: 10rpx;">
					<h4>
						没收到？
						<u-button type="primary" shape="circle" size="mini" @click="resend()" :disabled="disabled">{{btntxt}}</u-button>
					</h4>
				</view>
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
		sendEmailCode,
		checkEmailCode
	} from '../../api/modules/user.js';
	export default {

		data() {
			return {
				code: '',
				type: 'text',
				border: true,
				time: 0,
				btntxt: "重新发送",
				disabled: false,
				email: ''
			}

		},

		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.email = option.email
		},

		methods: {
			// 确认验证码
			confirm() {
				// 判断验证码是否为空
				// 若不为空，则与后端验证验证码的正确性
				// 验证成功后跳转至重置密码界面
				if (this.code == "" || this.code == null) {
					this.toast("验证码不能为空噢", "warning", "bottom")
				} else {
					checkEmailCode({
							email: this.email,
							email_code: this.code
						})
						.then(res => {
							if (res.data.success) {
								this.toast("验证成功~", "success", "bottom")
								setTimeout(() => {
									this.navigator("./reset?user_id=" + res.data.data.id)
								}, 1500)
							} else {
								this.toast(res.data.message + "，再检查下吧~", "error", "bottom")
							}
						})
						.catch(data => {
							console.log(data)
							this.toast(data + "，再检查下吧~", "error", "bottom")
						})
				}
			},

			// 重新发送验证码
			resend() {
				sendEmailCode({
						email: this.email,
						type: "1"
					})
					.then(res => {
						if (res.data.success) {
							this.toast("邮件发送成功，请注意查收~", "success", "bottom")
						}
					})

				this.time = 60;
				this.timer();
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
					this.btntxt = "重新发送";
					this.disabled = false;
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
