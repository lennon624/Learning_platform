<template>
	<view>
		<view class="top">
			<!-- 头部背景 -->
			<view class="header"><canvas class="background" canvas-id="myCanvas" id="myCanvas"></canvas></view>
			<view class="header"><canvas class="background" canvas-id="myCanvas2" id="myCanvas2"></canvas></view>
			<view class="header"><canvas class="background" canvas-id="myCanvas3" id="myCanvas3"></canvas></view>

			<!-- 图标 -->
			<view class="icons">
				<u-row gutter="16" justify="around">
					<u-col span="6">
						<u-icon name="bell" size="50" color="#FFFFFF"></u-icon>
					</u-col>
					<u-col span="6" @click="goSettings()">
						<u-icon name="setting" size="50" color="#FFFFFF"></u-icon>
					</u-col>
				</u-row>
			</view>

			<!-- 用户信息卡片 -->
			<view class="user-info">
				<uni-card title="欢迎" note="Tips : 好好学习 天天向上" isShadow="true">
					<view id="login">
						<u-row gutter="16" justify="around">
							<u-col span="3">
								<u-avatar :src="src" style="float: left;"></u-avatar>
							</u-col>
							<u-col span="8" @click="userInfo()">
								<view style="font-size: 23px;">
									<text>{{username}} </text>
								</view>
							</u-col>
							<u-col span="1">
								<view style="float: right;">
									<u-icon name="arrow-right"></u-icon>
								</view>
							</u-col>
						</u-row>
					</view>
				</uni-card>
			</view>
		</view>

		<!-- 功能选项列表 -->
		<view class="func-list">
			<u-cell-group>
				<u-cell-item title="我的兴趣">
					<u-icon slot="icon" size="35" name="bookmark" custom-prefix="custom-icon" color="#333333" style="margin-right: 10px;"></u-icon>
				</u-cell-item>
				<u-cell-item title="我的下载">
					<u-icon slot="icon" size="35" name="download" custom-prefix="custom-icon" color="#333333" style="margin-right: 10px;"></u-icon>
				</u-cell-item>
			</u-cell-group>
			<u-divider color="#649CF8" half-width="35%" style="margin-top: 10px;margin-bottom: 10px;">(｀・ω・´)</u-divider>
			<u-cell-group>
				<u-cell-item title="分享App" @click="openShare">
					<u-icon slot="icon" size="35" name="share" custom-prefix="custom-icon" color="#333333" style="margin-right: 10px;"></u-icon>
				</u-cell-item>
				<u-cell-item title="给我打分" @click="openRate">
					<u-icon slot="icon" size="35" name="star" custom-prefix="custom-icon" color="#333333" style="margin-right: 10px;"></u-icon>
				</u-cell-item>
			</u-cell-group>
		</view>

		<!-- 分享popup -->
		<uni-popup ref="popup1" type="share">
			<uni-popup-share title="分享到" @select="select"></uni-popup-share>
		</uni-popup>
		<!-- 打分popup -->
		<uni-popup ref="popup2" type="rate">
			<uni-popup-rate title="给我打分" @select="select"></uni-popup-rate>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	import uniPopupShare from '@/components/uni-popup/uni-popup-share.vue';
	import uniPopupRate from '@/components/uni-popup/uni-popup-rate.vue';

	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
			uniPopupShare,
			uniPopupRate
		},

		data() {
			return {
				src: '',
				text: '无头像',
				username: '登录/注册',
				windowWidth: 0,
				windowHeight: 0,
				// rateValue:5,//评分数值
			};
		},

		// 获取用户名
		onShow() {
			let that = this;
			uni.getStorage({
				key: "user",
				success: function(res) {
					if (res.data == null) {
						that.username = "登录/注册"
					} else {
						that.username = res.data.username
					}
				}
			})

		},
		onReady(e) {
			// 获取屏幕宽高
			uni.getSystemInfo({
				success(res) {
					this.windowWidth = res.windowWidth;
					this.windowHeight = res.windowHeight;

					// 绘制头部卡片背景
					var context = uni.createCanvasContext('myCanvas');
					context.setLineWidth(2);
					context.arc(this.windowHeight * 0.3, -this.windowWidth * 1.1, this.windowHeight, 0, 2 * Math.PI, true);
					context.setFillStyle('#13a1ff');
					context.fill();
					context.draw();

					var context2 = uni.createCanvasContext('myCanvas2');
					context2.setLineWidth(2);
					context2.arc(this.windowHeight * 0.44, -this.windowWidth * 0.437, this.windowHeight * 0.484, 0, 2 * Math.PI,
						true);
					context2.setFillStyle('#60b6f8');
					context2.fill();
					context2.draw();

					var context3 = uni.createCanvasContext('myCanvas3');
					context3.setLineWidth(2);
					context3.arc(-this.windowHeight * 0.073, -this.windowWidth * 0.535, this.windowHeight * 0.558, 0, 2 * Math.PI,
						true);
					context3.setFillStyle('#55aaff');
					context3.fill();
					context3.draw();
				}
			});
		},
		methods: {
			userInfo() {
				// 若用户未登录，进入登录页面
				if (true) {
					uni.navigateTo({
						url: '../../login/login',
						animationType: 'pop-in',
						animationDuration: 200
					});
				} else {
					// 进入用户信息页
				}
			},
			openShare() {
				this.$refs.popup1.open();
			},
			openRate() {
				this.$refs.popup2.open();
			},
			
			// 进入设置页
			goSettings() {
				uni.navigateTo({
					url: "../../settings/settings",
					animationType: "pop-in",
					animationDuration: 200
				})
			}
		}
	};
</script>

<style>
	.top {
		position: relative;
	}

	.header {
		position: absolute;
		width: 100%;
		height: 350rpx;
	}

	.user-info {
		position: absolute;
		top: 140rpx;
		right: 50rpx;
		left: 50rpx;
	}

	.icons {
		position: absolute;
		width: 120rpx;
		right: 35rpx;
		top: 55rpx;
	}

	.func-list {
		position: absolute;
		width: 100%;
		margin-top: 500rpx;
	}

	.background {
		margin: 0;
		width: 100%;
		height: 500rpx;
	}
</style>
