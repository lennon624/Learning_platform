<template>
	<view>

		<view class="input-btn" @click="inputBtn">
			<view class="input-box">
				<view class="input-left">
					{{searchText}}
				</view>
				<view class="input-right">
					<i class="iconfont search" />
				</view>
			</view>
		</view>
		<view>
			<!-- 猜你喜欢图文列表 -->
			<!-- <imgList :imageList="imageLists" /> -->
			<!-- 听书图文列表 -->
			<!-- <imgList :imageList="listenList" /> -->
			<!-- 精品好书图文列表 -->
			<!-- <imgListTwo :imglisttwo="likeList" /> -->

			<!-- 推荐课程 -->
			<recommendedList v-show="showRecommend" :imageList="recommendedList" />
		</view>
	</view>

</template>

<script>
	import imgList from '../../components/imglist/imglist.vue'
	import imgListTwo from '../../components/imglisttwo/imglisttwo.vue'
	import {
		getRecommendedList,
		getPopularCourses
	} from "../../api/modules/course.js"

	export default {
		components: {
			imgList,
			imgListTwo
		},
		data() {
			return {
				searchText: "点击搜索课程",
				user: {},
				// 课程推荐列表
				recommendedList: [{
					head: '猜你喜欢',
					imgdata: [{
						data: [],
					}, ],
				}, ],
				// 是否显示推荐，用户未登录则不显示推荐栏
				showRecommend: false,
				imageLists: [{
					head: '猜你喜欢',
					imgdata: [{
						data: [{
								url: "https://s2.ax1x.com/2020/03/04/35f6C4.png",
								title: "我是个妈妈，我需要铂金包",
								author: "温妮斯蒂·马丁"
							},
							{
								url: "https://s2.ax1x.com/2020/03/04/35fhb6.png",
								title: "迷人的材料",
								author: "马克·米奥多尼克"
							},
							{
								url: "https://s2.ax1x.com/2020/03/04/35fs5F.png",
								title: "恋爱口语:我们到底要跟男人聊什么？",
								author: "刘慈欣"
							},
						],

					}, ],
				}, ],
				listenList: [{
					head: '听书',
					imgdata: [{
						data: [{
								url: "https://s2.ax1x.com/2020/03/05/3T5pLj.png",
								title: "你在怕什么：与死亡有关的六幅画",
								author: "陈诺"
							},
							{
								url: "https://s2.ax1x.com/2020/03/05/3T5Ces.png",
								title: "切尔诺贝利的祭祷",
								author: "S.A.阿列克谢耶维奇"
							},
							{
								url: "https://s2.ax1x.com/2020/03/05/3T5Pwn.png",
								title: "流浪地球",
								author: "刘慈欣"
							},

						],

					}, ],
				}, ],
				likeList: [{
					head: '精品好书',
					data: [{
							url: "https://s2.ax1x.com/2020/03/05/3THGsU.png",
							head: "高情商沟通力",
							author: "张超",
							price: "20",
							title: "人是感性的动物，只会偶尔的理性，人与人沟通不是为了找一个答案，而是想要多一份理解和认同。",
							initialize: "30"
						},
						{
							url: "https://s2.ax1x.com/2020/03/05/3TH8MT.png",
							head: "危险的维纳斯",
							author: "东野圭吾",
							price: "10",
							title: "维纳斯」不是某个人，而是存在于我们每个人心中令人疯狂的东西。",
							initialize: "30"
						},
					]
				}, ]
			}
		},
		onLoad() {

		},
		mounted() {
			// 获取用户登录信息
			try {
				const value = uni.getStorageSync('user');
				if (value) {
					this.user = value;
					this.showRecommend = true
				}
			} catch (e) {
				// error
			}
			// 获取课程推荐列表
			getRecommendedList({
				userId: this.user.id,
				howMany: "20"
			}).then(res => {
				if (res.data.success) {
					this.recommendedList[0].imgdata[0].data = res.data.data
				}
			})
		},
		methods: {
			inputBtn() {
				uni.navigateTo({
					url: '../../pages/searchList/searchList?id=true'
				})
			},
			searchBtn() {
				console.log(12323123)
			},
		}
	}
</script>

<style scoped lang="scss">
	.slot-wrap {
		text-align: left;
		padding-right: 30px;
		font-size: 32upx;
		padding: 0 $bleed;
	}

	.search-body {
		position: fixed;
		top: 0 !important;
		bottom: 0 !important;
		left: 0;
		right: 0;
		z-index: 2000;
		background-color: #fff;
		overflow-y: auto;

	}

	.search-body::-webkit-scrollbar {
		display: none !important;
	}

	.subject-list {
		position: fixed;
		top: 0;
		bottom: 0;
		right: 0;
		left: 0;
		background-color: #fff;
		z-index: 510;

		.subject-nav {
			height: $barHeight;
			line-height: $barHeight;
			padding: 0 $bleed;

			.return {
				font-size: 36upx;
				color: #333;
			}
		}
	}

	.input-btn {
		padding: 40upx $bleed;

		.input-box {
			height: 104upx;
			line-height: 104upx;
			padding: 0 10upx 0 30upx;
			border-radius: 8upx;
			background-color: $lightBlue;

			.input-left {
				float: left;
				width: calc(100% - 60upx);
				color: $dominantHue;
				font-size: 28upx;
			}

			.input-right {
				float: right;
				width: 60upx;
				text-align: center;
				line-height: 104upx;

				.search {
					font-size: 32upx;
					font-weight: bold;
					color: $dominantHue;
				}
			}
		}
	}

	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
		.search-body {
			padding-bottom: $navBoxHeight !important;
			/* #ifdef H5 */
			padding: 0 !important;
			/* #endif */
		}
	}

	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
		.search-body {
			padding-bottom: $navBoxHeight !important;
			/* #ifdef H5 */
			padding: 0 !important;
			/* #endif */
		}

	}


	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
		.search-body {
			padding-bottom: $navBoxHeight !important;
			/* #ifdef H5 */
			padding: 0 !important;
			/* #endif */
		}
	}
</style>
