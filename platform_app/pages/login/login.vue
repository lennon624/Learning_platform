<template>
	<view class="login_body">
		<view class="">
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
						<view style="font-size: 35rpx; text-align: center; margin-right: 25rpx;" @click="goRegister()">
							<text>注册</text>
						</view>
					</u-col>
				</u-row>
			</view>
			<u-toast ref="uToast" />
			<u-form :model="loginForm" ref="uForm">
				<u-form-item label="账号" prop="username">
					<u-input v-model="loginForm.username" placeholder="用户名/电子邮箱" />
				</u-form-item>
				<u-form-item label="密码" prop="password">
					<u-input v-model="loginForm.password" placeholder="输入密码" />
				</u-form-item>
			</u-form>
			<view class="begin">
				<u-button class="begin_1" @click="submit">登录</u-button>

				<u-button class="begin_2" @click="loginByphone">手机验证码登录</u-button>

			</view>
			<view class="forget">
				<navigator @tap="forget">忘记密码。。</navigator>
				
				
				
				
			</view>
		</view>



<!-- <pt-images-verification :left="codeObj.left" :bgImg="codeObj.bgImg" :maskImg="codeObj.maskImg" @refresh="refresh" @success="success"></pt-images-verification> -->



	</view>
</template>

<script>
	import {
		login,
		sendTelCode,
		loginByTel
	} from '../../api/modules/user.js'
	export default {
		
		// components: {
		//     pt-images-verification
		//   },
		data() {
			return {
				loginForm: {
					username: "",
					password: ""
				},

				rules: {
					username: [{
							required: true,
							message: '用户名/邮箱不能为空',
							// 可以单个或者同时写两个触发验证方式
							trigger: 'blur,change'
						},

					],
					password: [{

						min: 5,
						message: '密码不少于5个字符',
						trigger: 'blur,change'
					}, {
						required: true,
						message: '密码不能为空',
						// 可以单个或者同时写两个触发验证方式
						trigger: 'blur,change',
					}, ]
				}
			}
		},
		methods: {
			// // 随机获取验证码，此处随机可能会随机到重复数据，按需修改
			// 	init(){
			// 		let index = this.random(0,this.codeArray.length - 1)
			// 		this.codeObj = this.codeArray[index]
			// 	},
			// 	// 刷新验证码
			// 	refresh(){
			// 		this.init()
			// 	},
			// 	// 创建随机数
			// 	random(mins,maxs){
			// 		var randomNum = Math.round(Math.random() * (maxs - mins) + mins);
			// 		return randomNum;
			// 	},
			// 	// 验证成功
			// 	success(){
			// 		uni.showToast({
			// 			icon: 'none',
			// 			title: '验证成功'
			// 		})
			// 	},
			
				

			forget(){
				uni.navigateTo({
					url: "../recovery/recovery",
					animationType: 'pop-in',
					animationDuration: 200
				});
			},

			submit() {
				this.$refs.uForm.validate(valid => {
					if (valid) {
						//	发送数据


						login(this.loginForm)
							.then(resp => {
								// let token = resp.data.data.token;
								// setStore("token",token)
								if (resp.data.statusCode === "200") {
									let user = resp.data.data.user;
									uni.setStorage({
										key: "user",
										data: user
									})
									this.toast(resp.data.message, 'success', "bottom");
									uni.reLaunch({
										url: '../index/index',
										animationType: 'pop-in',
										animationDuration: 200
									});
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

			//手机验证码
			loginByphone() {
				uni.navigateTo({
					url: '/pages/login/loginByphone'
				});
			},

			goRegister() {
				uni.navigateTo({
					url: "../register/register"
				})
			},

			// 回到首页
			goHome() {
				uni.reLaunch({
					url: '../index/index'
				})
			}


		},

		// 必须要在onReady生命周期，因为onLoad生命周期组件可能尚未创建完毕
		onReady() {
			this.$refs.uForm.setRules(this.rules);
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

		.forget {
			margin-top: 100rpx;
			color: #5093F2;
		}
	}
</style>
