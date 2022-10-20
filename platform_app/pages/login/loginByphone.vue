<template>
	<view class="login_body">
		<view class="">
			<view class="status_bar">
				<!-- 这里是状态栏 -->
			</view>
			<view class="top-bar">
				<u-row gutter="16" justify="center">
					<u-col span="2">
						<view style="margin-left: 25rpx;">
							<u-icon name="arrow-left" size="40" @click="goBack()"></u-icon>
						</view>
					</u-col>
					<u-col span="8">
						<view style="font-size: 40rpx; text-align: center;">
							<text>Dasabi</text>
						</view>
					</u-col>
					<u-col span="2">
						<view style="font-size: 35rpx; text-align: center; margin-right: 25rpx;" @click="goRegister()">
							<text>注册</text>
						</view>
					</u-col>
				</u-row>
			</view>
			<u-form :model="phoneForm" ref="phoneForm">
				<u-form-item label="+86" prop="tel">
					<u-input v-model="phoneForm.tel" label-width="120rpx" label-align="right" placeholder="请输入手机号码" />
				</u-form-item>
				<u-form-item label="验证码" prop="phonecode" label-width="100rpx">

					<u-input v-model="phoneForm.phonecode" placeholder="请输入验证码" />
					<!-- 验证码部分 -->
					<u-toast ref="uToast"></u-toast>
					<u-verification-code :seconds="seconds" @end="end" @start="start" ref="uCode" @change="codeChange"></u-verification-code>
					<u-button type="success" size="mini" @tap="getCode" slot="right">{{tips}}</u-button>


				</u-form-item>
			</u-form>
			<view class="begin">
				<u-button class="begin_1" @click="submit">登录</u-button>



			</view>
			<view class="loginBycommon">
				<navigator url="/pages/login/login">用户名/密码登录</navigator>

			</view>
		</view>





	</view>
</template>

<script>
	import {
		hello,
		login,
		sendTelCode,
		loginByTel
	} from '../../api/modules/user.js'
	

	export default {
		data() {
			return {
				phoneForm: {
					tel: "", //手机号
					phonecode: "" //手机号验证码
				},

				tips: '',
				// refCode: null,
				seconds: 10,

				rules: {
					tel: [{
							required: true,
							message: '请输入手机号码',
							// 可以单个或者同时写两个触发验证方式
							trigger: 'blur,change'
						},
						// 正则判断为数字
						{
							pattern: 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/,
							message: '电话号码格式不正确',
							trigger: 'blur,change'
						},
					],
					phonecode: [{
						required: true,
						message: '验证码不能为空',
						// 可以单个或者同时写两个触发验证方式
						trigger: 'blur,change'
					}]
				}
			}
		},

		// 必须要在onReady生命周期，因为onLoad生命周期组件可能尚未创建完毕
		onReady() {
			this.$refs.phoneForm.setRules(this.rules);
		},

		methods: {


			submit() {
				this.$refs.phoneForm.validate(valid => {
					if (valid) {
						//手机快捷登录验证
						loginByTel({
								tel: this.phoneForm.tel,
								inputCode: this.phoneForm.phonecode
							})
							.then(resp => {
								if (resp.data.statusCode === "200") {
									let user = resp.data.data.user;
								uni.setStorage({
									key: "user",
									data: user
								})
							
								
									this.toast(resp.data.message, 'success', "bottom");
									if (resp.data.data.isNew == "1") {
										//1 表示新用户 跳转到信息完善界面
										uni.switchTab({
											url: "../tabBar/home/home",
											animationType: 'pop-in',
											animationDuration: 200
										});
									} else {
										uni.reLaunch({
											url: '../index/index',
											animationType: 'pop-in',
											animationDuration: 200
										});
									}
								} else {
									this.toast(resp.data.message, 'error');
								}
							})
							.catch(data => {
								this.toast(data, 'error');
							});
						console.log('验证通过');
					} else {
						console.log('验证失败');
					}
				});
			},

			codeChange(text) {
				this.tips = text;
			},
			getCode() {



				if (this.$refs.uCode.canGetCode) {
					// 模拟向后端请求验证码

					sendTelCode({
							tel: this.phoneForm.tel
						})
						.then(resp => {
							if (resp.data.statusCode === "200") {
								this.toast(resp.data.message, 'success', "bottom");
							}
						})
						.catch(data => {
							this.toast(data, 2);
						});

					uni.showLoading({
						title: '正在获取验证码'
					})
					setTimeout(() => {
						uni.hideLoading();
						// 这里此提示会被this.start()方法中的提示覆盖
						this.$u.toast('验证码已发送');
						// 通知验证码组件内部开始倒计时
						this.$refs.uCode.start();
					}, 2000);
				} else {
					this.$u.toast('倒计时结束后再发送');
				}
			},
			end() {
				this.$u.toast('倒计时结束');
			},
			start() {
				this.$u.toast('倒计时开始');
			},
			
			// 进入注册页
			goRegister() {
				uni.navigateTo({
					url: "../register/register"
				})
			},
			
			// 返回上一级
			goBack() {
				uni.navigateBack({
					delta: 1
				});
			}


		}
	}
</script>

<style lang="scss" scoped>
	/deep/.uni-input-input {
		color: white;
	}

	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
	}

	.top-bar {
		position: absolute;
		width: 100%;
		color: #FFFFFF;
	}

	.login_body {
		width: 100%;
		height: 100%;
		position: absolute;
		background: #001934;
		text-align: center;

		.u-form {
			padding: 200rpx 130rpx 0rpx;
			background: #fffff;

			.u-form-item {

				margin-top: 50rpx;
				color: white;

				.u-input {
					color: white;
				}

			}
		}

		.begin {
			padding-top: 200rpx;
			width: 500rpx;
			margin: 0 auto;
			border: none;
			text-shadow: 0px 3px 6px 0px #f;

			.begin_1 {

				background: #0298db;
				font-size: 22px;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				text-align: center;
				color: #f8f8f8;
				line-height: 28px;
			}

			.begin_2 {
				margin-top: 40rpx;

				background: rgba(146, 143, 143, 0.48);
				font-size: 22px;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				text-align: center;
				color: #f8f8f8;
				line-height: 28px;
			}
		}

		.loginBycommon {
			text-align: right;
			margin-top: 100rpx;
			padding-right: 100rpx;
			color: #5093F2;
		}
	}
</style>
