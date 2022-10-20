<template>

	<view class="">
		<!-- banner -->
		<banner />

		<!-- 金刚区 -->
		<view style="margin-top: 50rpx;">
			<view class="tuijian1">探索</view>
			<scroll-view scroll-x="true" class="scroll">
				<view class="function">


					<view class="select" v-for="(selectDatas,selectKey) in selectData" :key="selectKey">
						<view @click="selectBtn(selectKey)">
							<view class="select-img" :class="{}">
								<image :src="selectDatas.url" mode="aspectFill" class="images1"></image>
							</view>
							<span class="kebiao">
								<text>{{selectDatas.text}}</text>
							</span>
						</view>

					</view>


				</view>
			</scroll-view>
		</view>


		<!-- 推荐图文列表 -->
		<!-- <imgList :imageList="imageLists" /> -->

		<!-- 推荐课程 -->
		<recommendedList v-show="showRecommend" :imageList="recommendedList" />

		<!-- 文学图文列表 -->
		<!-- <imgList :imageList="listenList" /> -->

		<!-- 精品好书图文列表 -->
		<!-- <imgListTwo :imglisttwo="likeList" /> -->

		<!-- 热门好课 -->
		<popCourseList :courseList="popularCourseList"></popCourseList>


	</view>
</template>

<script>
	import imgList from '../../components/imglist/imglist.vue'
	import imgListTwo from '../../components/imglisttwo/imglisttwo.vue'
	import popCourseList from "../../components/popCourseList/popCourseList.vue"
	import banner from '../../components/banner/banner.vue'
	import recommendedList from "../../components/recommendedList/recommendedList.vue"
	import {
		getRecommendedList,
		getPopularCourses
	} from "../../api/modules/course.js"

	export default {
		components: {
			imgList,
			imgListTwo,
			banner,
			recommendedList,
			popCourseList
		},

		data() {
			return {
				// 用户
				user: {},

				// 是否显示推荐，用户未登录则不显示推荐栏
				showRecommend: false,

				selectData: [{
						url: "../../static/img/arts_and_humanities.jpeg",
						text: "艺术与人文"
					},
					{
						url: "../../static/img/business.jpeg",
						text: "商务与社科"
					},
					{
						url: "../../static/img/computer_science.jpeg",
						text: "计算机科学"
					},
					{
						url: "../../static/img/health.jpeg",
						text: "生物与农业"
					},
					{
						url: "../../static/img/math_and_logic.jpeg",
						text: "数学与逻辑"
					},
					{
						url: "../../static/img/physical_science_and_engineering.jpeg",
						text: "物理材料学"
					},

				],
				imageLists: [{
					head: '推荐',
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

				// 课程推荐列表
				recommendedList: [{
					head: '为你推荐',
					imgdata: [{
						data: [],
					}, ],
				}, ],

				listenList: [{
					head: '文学',
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
				}, ],

				// 热门课程
				popularCourseList: [{
					head: '热门好课',
					data: [{
						id: "",
						courseName: "",
						teacherId: "",
						introduction: "",
						imgSrc: "",
						notice: "",
						status: "",
						etc: {
							teacher_name: "",
							teacher_id: "",
							school_name: "",
							school_id: ""
						}
					}, ]
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

			// 获取热门课程
			getPopularCourses().then(res => {
				if (res.data.success) {
					this.popularCourseList[0].data = res.data.data
				}
			})
		},

		methods: {
			selectBtn(selectKey) {
				// 金刚区按钮 
				console.log('你点了第' + selectKey + '个选项')
				uni.navigateTo({
					url: '../../pages/rankingList/rankingList'
				})
			},
		}
	}
</script>

<style scoped lang="scss" scoped>
	.banner {
		padding: 20upx $bleed 20rpx $bleed;
		box-sizing: border-box;
		margin-top: 20rpx;
		border-radius: 20rpx;
		background-color: white;

	}

	/deep/.wrap {
		background-color: white;
	}


	/deep/.banner image {
		width: 100%;
		border-radius: 20rpx;
	}

	.function {
		padding: 30rpx 0 40rpx 0;
		display: flex;


	}

	.select {
		flex: 1;
		width: 100%;
		text-align: center;

	}

	// .select-img {
	// 	padding: 10rpx 10rpx 4rpx 0;
	// }

	// .select-img image {
	// 	height: 186rpx;
	// 	width: 186rpx;
	// }


	.select text {
		font-size: 26upx;
		line-height: 26upx;
	}

	// .select text {
	// 	// display: block;
	// 	// position: absolute;
	// 	// bottom: 90rpx;
	// 	// margin: 0;
	// 	// font-size: 32rpx;
	// 	// line-height: 26rpx;
	// 	// color: white;

	// }
	.kebiao {
		// display: block;
		position: absolute;
		bottom: 130rpx;
		margin-left: -12%;
		font-size: 30rpx;
		line-height: 26rpx;
		color: white;
		word-break: keep-all;
		font-family: inherit;


	}

	.images1 {
		width: 314rpx;
		height: 192rpx;
		/* border-top-left-radius:18rpx;
			border-top-right-radius:18rpx; */
		border-radius: 18rpx;
		margin-left: 20rpx;
		margin-top: -2rpx;
		margin-right: 25rpx;



	}

	.tuijian1 {

		font-size: 40rpx;
		display: inline;
		margin-left: 25rpx;
		font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
	}
</style>
