<template>
	<view class="index-body">
		<u-navbar title-color="#fff" :is-fixed="isFixed" :is-back="isBack" :border-bottom="borderBottom">
			<view class="slot-wrap" v-if="useSlot">
				<text>{{head}}</text>
			</view>
		</u-navbar>

		<view class="content">
			<home v-if="index === 0" />
			<classify v-if="index === 1" />
			<search v-if="index === 2" />
			<mine v-if="index === 4" />
		</view>
		<bookrack :studentId='studentId' v-if="index === 3" />
		<!-- 底部标签栏 -->
		<bottom @toIndex="toIndex"></bottom>



	</view>

</template>

<script>
	import home from '../home/home.vue'
	import classify from '../classify/classify.vue'
	import bookrack from '../bookrack/bookrack.vue'
	import search from '../search/search.vue'
	import mine from '../mine/mine.vue'
	import bottom from '../../components/bottom/bottom.vue'
	export default {
		components: {
			home,
			classify,
			bookrack,
			search,
			mine,
			bottom,
		},
		data() {
			return {
				//uView UI官方导航组件  详情请看官方文档
				useSlot: true,
				immersiveA: true,
				isBack: false,
				isFixed: true,
				borderBottom: false,

				index:0,
				head: '首页',
				skeletonOff: false,
				studentId: ""
			}
		},
		onLoad() {

		},
		methods: {
			toIndex(index) {
				this.index = index
				if (index == 0) {
					this.head = '首页'
				} else if (index == 1) {
					this.head = '分类'
				} else if (index == 2) {
					this.head = '搜索'
				} else if (index == 3) {
					this.head = '选课'
				} else if (index == 4) {
					this.head = '我的'
				}
			},

		},
		created() {},
		mounted() {
			uni.getStorage({
				key: "user",
				success: (res => {
					this.studentId = res.data.etc.id;
		
				})
			})
		}
	}
</script>

<style scoped lang="scss">
	/*苹果x适配 H5APP*/

	.index-body {
		position: fixed;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.content {
		position: fixed;
		top: $stairTop;
		/* #ifdef H5 */
		top: 44px;
		/* #endif */
		left: 0;
		right: 0;
		bottom: $navHeight;
		overflow-y: auto;
	}

	.slot-wrap {
		text-align: left;
		padding-right: 30px;
		font-size: 32upx;
		padding: 0 $bleed;
	}

	.index-body::-webkit-scrollbar {
		display: none !important;
	}

	.content::-webkit-scrollbar {
		display: none !important;
	}

	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
		.content {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
		}

		/* #ifdef H5 */
		.content {
			top: $barHeight;
			bottom: $navHeight;
		}


		/* #endif */
	}

	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
		.content {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
		}
		

		/* #ifdef H5 */
		.content {
			top: $barHeight;
			bottom: $navHeight;
		}


		/* #endif */
	}


	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {

		.content {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
		}

		/* #ifdef H5 */
		.content {
			top: $barHeight;
			bottom: $navHeight;
			
		}

		/* #endif */
	}
</style>
