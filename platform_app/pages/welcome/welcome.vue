<template>
	<view class="container">
		<view class="login_body">
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
							<text>  </text>
						</view>
					</u-col>
					<u-col span="2">
						<view style="font-size: 35rpx; text-align: center; margin-right: 25rpx;" @click="goRegister()">
							<text>注册</text>
						</view>
					</u-col>
				</u-row>
			</view>


			<view class="dasabi">
				<text>Dasabi</text>
			</view>
			<view class="introduction">
				<text>可以学习你喜欢的任何内容:语言、社会科学、C++, 随便什么。</text>
			</view>
			<view class="begin">
				<u-button class="begin_1">开始吧</u-button>

				<u-button class="begin_2" @click="login">登录</u-button>

			</view>
		</view>
		<view>
			<u-toast ref="uToast" />
		</view>
	</view>
</template>

<script>
	import {
		hello
	} from '../../api/modules/user.js'

	export default {
		data() {
			return {

			}
		},
		methods: {

			//登录
			login() {
				uni.navigateTo({
					url: '/pages/login/login'
				});
			},
			click() {
				hello({
						text: "hello"
					})
					.then(res => {
						console.log(res)
						this.$refs.uToast.show({
							title: res.data.message,
							type: 'success',
						})
					})
					.catch(res => {
						this.$refs.uToast.show({
							title: res.data,
							type: 'success',
						})
					})
			},
			
			goRegister() {
				uni.navigateTo({
					url: "../register/register"
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

<style lang="scss">
	.container {
		color: #FFFFFF;
	}

	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
	}

	.top-bar {
		position: absolute;
		width: 100%;
	}

	.login_body {
		width: 100%;
		height: 100%;
		position: absolute;
		background: #001934;
		text-align: center;

		.go_register {
			padding: 70rpx 38rpx 0px 634rpx;

			.go_register_button {

				width: 78rpx;
				height: 50rpx;
				background: red;
				font-size: 32rpx;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				text-align: left;
				color: #ffffff;
				line-height: 200rpx;

			}
		}

		.dasabi {
			padding-top: 202rpx;
			width: 750rpx;
			height: 156rpx;

			text {
				font-size: 144rpx;
				font-family: Microsoft YaHei, Microsoft YaHei-Bold;
				font-weight: Bold;
				text-align: left;
				color: #ffffff;
				line-height: 12rpx;
			}
		}

		.introduction {
			padding: 200rpx 80rpx 0rpx;
			width: 100%;
			height: 240rpx;

			text {
				font-size: 48rpx;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				color: #ffffff;
				line-height: 80rpx;
			}
		}

		.begin {
			padding-top: 400rpx;
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
	}
</style>
