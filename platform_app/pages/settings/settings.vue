<template>
	<view>
		<view class="status_bar">
			<!-- 这里是状态栏 -->
		</view>

		<view class="content">
			<!-- 正文内容 -->
			<u-navbar back-text="返回" title="设置" :background="background" back-icon-color="#FFFFFF" :back-text-style="text"
			 title-color="#FFFFFF"></u-navbar>

			<view v-show="isLoggedIn">
				<!-- 确认按钮 -->
				<view style="position: absolute; width: 80%; margin: 0 auto; bottom: 50rpx; left: 0; right: 0;">
					<u-button type="primary" shape="circle" @click="logout()">退出账号</u-button>
				</view>
			</view>
		</view>
		
		<!-- 退出登录确认模态框 -->
		<u-modal v-model="show" :content="content" show-cancel-button :show-title="false" @confirm="confirm()"></u-modal>

		<!-- 吐司 -->
		<view>
			<u-toast ref="uToast" />
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				background: {
					backgroundColor: '#55aaff',

					// 导航栏背景图
					// background: 'url(https://cdn.uviewui.com/uview/swiper/1.jpg) no-repeat',
					// 还可以设置背景图size属性
					// backgroundSize: 'cover',

					// 渐变色
					// backgroundImage: 'linear-gradient(45deg, rgb(96, 182, 248), rgb(19, 161, 255))'
				},

				// 返回图标右边的辅助提示文字的样式
				text: {
					color: "#FFFFFF"
				},

				// 账号登录状态
				isLoggedIn: false,
				
				// 模态框开关
				show: false,
				// 模态框内容
				content: '确认要退出登录吗？'
			}
		},

		onLoad() {
			let that = this
			uni.getStorage({
				key: 'user',
				success: (res => {
					if (res.data == null) {
						that.isLoggedIn = false
					} else {
						that.isLoggedIn = true
					}
				})
			})
		},

		methods: {
			// 退出登录
			logout() {
				this.show = true
			},
			
			// 确认退出
			confirm() {
				if (this.isLoggedIn) {
					let that = this;
					uni.removeStorage({
						key: 'user',
						success: function(res) {
							that.toast("账号退出成功~", "success", "bottom")
							setTimeout(() => {
								uni.reLaunch({
									url: '../index/index'
								})
							}, 1500)
						}
					})
				}
			}

		}
	}
</script>

<style>
	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
	}

	.content {
		color: #FFFFFF;
	}
</style>
