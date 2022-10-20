<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";
	@import "@/static/iconfont.css";
	
	
	//全局图标
	@import url("static/icon/iconfont.css");
	// 骨架css
	@import './static/css/skeleton.scss';
	
	/*每个页面公共css */
	page {
		font-size: 24upx;
		color: $blackAll;
		font-family: 'PingFangSC-Regular', 'Source Han Sans CN' !important;
	}
	
	image {
		width: auto;
		height: auto;
	}
	
	// 高度不包括内外边距解决方法
	* {
		box-sizing: border-box;
	}
	
	
	/*苹果x适配 H5APP*/
	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
	
		/* #ifdef H5 */
	
		/* #endif */
	
	
	}
	
	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
	
		/* #ifdef H5 */
	
		/* #endif */
	
	}
	
	
	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
	
		/* #ifdef H5 */
	
		/* #endif */
	}
	
	
</style>
<script>
	export default {
		onLaunch: function() {
			console.log('App Launch');

			// 判断用户是否登录，若未登录则显示欢迎页
			uni.getStorage({
				key: "user",
				success: function(res) {
					let user = res.data;
					if (user.id == null) {
						setTimeout(() => {
							uni.navigateTo({
								url: '../../index/welcome',
								animationType: 'pop-in',
								animationDuration: 200
							});
						}, 3000)
					}
				}
			})
			
		},
		onShow: function() {
			console.log('App Show');
		},
		onHide: function() {
			console.log('App Hide');
		}
	};
</script>

<style>
	/* 解决头条小程序组件内引入字体不生效的问题 */
	/* #ifdef MP-TOUTIAO */
	@font-face {
		font-family: uniicons;
		src: url('/static/uni.ttf');
	}

	/* #endif */
</style>
