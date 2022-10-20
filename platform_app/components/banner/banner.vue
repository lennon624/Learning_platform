<template>
	<view class="banner-box">
		<view class="wrap">
			<u-swiper class="banner-bg" :duration="1000" :height="200" :interval="4000" :list="banner"></u-swiper>
		</view>

	</view>

</template>

<script>
	export default {
		data() {
			return {
				banner: [{
						image: 'https://s1.ax1x.com/2020/04/24/JD9GJH.jpg'
					},
					{
						image: 'https://s1.ax1x.com/2020/05/28/tZmqmt.jpg'
					},
					{
						image: 'https://s1.ax1x.com/2020/05/28/tZMsN6.jpg'
					},
				]
			}
		},
		methods: {

		},
		onLoad() {
		
		}
	}
</script>

<style scoped lang="scss">
	$bannerH: 200rpx;
	.banner-box{
		padding: 40upx $bleed 0 $bleed;
	}
	.wrap {
		height: $bannerH;
		background-color: $skeletonColor;
	}
	.banner-bg{
		background-color: $skeletonColor;
	}

</style>
