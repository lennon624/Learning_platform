<template>
	<view class="bottom-nav">
		<view class="nav-box">
			<view class="nav-tab" :class="{'nav-tab-active':home===0}" @click="toIndex(0)">
				<view class="nav-icon">
					<view class="circle">
						<i class="iconfont home" />
					</view>
				</view>
				<view class="nav-text">
					首页
				</view>
			</view>
			<view class="nav-tab" :class="{'nav-tab-active':home===1}" @click="toIndex(1)">
				<view class="nav-icon" >
					<view class="circle">
						<i class="iconfont classify" />
					</view>
				</view>
				<view class="nav-text">
						分类
				</view>
			</view>
			<!-- 跳转独立的搜索页 -->
			<view class="nav-tab" :class="{'nav-tab-active':home===2}" @click="toIndex(2)">
				<view class="nav-icon" >
					<view class="circle">
						<i class="iconfont search"/>
					</view>
				</view>
				<view class="nav-text">
					搜索
				</view>
			</view>
			<view class="nav-tab" :class="{'nav-tab-active':home===3}" @click="toIndex(3)">
				<view class="nav-icon" >
					<view class="circle">
						<i class="iconfont bookrack" />
					</view>
				</view>
				<view class="nav-text">
					选课
				</view>
			</view>
			<view class="nav-tab" :class="{'nav-tab-active':home===4}" @click="toIndex(4)">
				<view class="nav-icon" >
					<view class="circle">
						<i class="iconfont mine" />
					</view>
				</view>
				<view class="nav-text">
					我的
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				home: 0,
			}
		},
		onLoad() {

		},
		methods: {
			toPage(url) {
				uni.navigateTo({
					url
				})
			},
			toIndex(index) {
					this.$emit('toIndex', index)
					this.home = index
			}
		}
	}
</script>

<style scoped lang="scss">
	.bottom-nav {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		width: 100%;
		box-shadow: 0 -4upx 12upx rgba(0, 0, 0, 0.06);
		background: #fff;
		z-index: 100;
	}

	.nav-tab {
		flex: 1;
		text-align: center;
		width: 100%;
		height: $navHeight;
	}

	.nav-icon {
		height: 60upx;
		color: $mediumGrey;
		box-sizing:border-box;
		padding: 8upx 0 4upx 0;
	}

	.nav-icon .iconfont {
		font-size: 40upx;
	}

	.nav-text {
		font-size: 22upx;
		height: 38upx;
		color: $mediumGrey;
		font-weight: 400;
	}

	.nav-tab-active .circle {
		height: 76upx;
		width: 76upx;
		line-height: 76upx;
		position: relative;
		top: -28upx;
		left: calc(50% - 38upx);
		border-radius: 50%;
		background: $dominantHue;
		box-shadow: 0 6upx 12upx rgba(0, 0, 0, 0.2);
	}

	.nav-tab-active .iconfont {
		color: #fff;
	}

	.nav-tab-active .nav-text{
		color: $dominantHue;
	}
	.nav-box{
		/* position: absolute; */
		display: flex;
		width: 100%;
		box-sizing:border-box;
	}
	/*苹果x适配 H5APP*/
	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
		.bottom-nav {
			height: $navBoxHeight + $navHeight;
		}
		/* #ifdef H5 */
		.bottom-nav {
			height:  $navHeight;
		}
		/* #endif */
	
	
	}
	
	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
		.bottom-nav {
			height: $navBoxHeight + $navHeight;
		}
		/* #ifdef H5 */
		.bottom-nav {
			height:$navHeight;
		}
		/* #endif */
	
	}
	
	
	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
		.bottom-nav {
			height: $navBoxHeight + $navHeight;
		}
		/* #ifdef H5 */
		.bottom-nav {
			height:  $navHeight;
		}
		/* #endif */
	}
</style>
