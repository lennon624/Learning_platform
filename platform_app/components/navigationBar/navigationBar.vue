<template>
	<view class="navigation-body">
		<view class="navigation-nav">
			<view class="nav-head">
				{{head}}
			</view>
		</view>
	</view>
</template>
<script>
	export default {
	    components: {},
		props:['head'],
	    data() {
	        return {
				
			}
	    },
	    methods: {
	   
	    }
	}
</script>

<style scoped lang="scss">
	
	.navigation-body {
		// height: $barHeight;
		line-height: $barHeight;
		background-color: #fff;
		position: fixed;
		padding-top: $statusBarH;
		right: 0;
		left: 0;
		z-index: 100;
	}

	.navigation-nav {
		padding: 0 $bleed;
		display: flex;
		height: $barHeight;
		/* background-color: #333; */
	}
	.nav-head {
		flex: 1;
		text-align: left;
		padding-right: 30px;
		font-size: 18px;
		line-height: $barHeight;
	}
	/*苹果x适配 H5APP*/
	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
		.navigation-body{
			padding-top: $barTopHeight;
			/* #ifdef H5 */
			padding-top: 0;
			/* #endif */
		}
	}
	
	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
		.navigation-body{
			padding-top: $barTopHeight;
			/* #ifdef H5 */
			padding-top: 0;
			/* #endif */
		}
	}
	
	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
		.navigation-body{
			padding-top: $barTopHeight;
			/* #ifdef H5 */
			padding-top: 0;
			/* #endif */
		}
	}
</style>
