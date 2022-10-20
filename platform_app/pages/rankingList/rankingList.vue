<template>
	
	<view class="books-body">
		<view class="" style="height: 100rpx">
			<u-navbar back-text="返回" ></u-navbar>
		</view>
		<!-- 左边切换 -->
		<view class="books-left">
			<view class="left-list" :class="{'books-left-active': listActive == bookDataKey}" v-for="(bookDataList,bookDataKey) in bookData"
			 @click="listBtnOne(bookDataKey,bookDataList.list)" :key="bookDataKey">
				<view class="vertical-bar"></view>
				{{bookDataList.list}}
			</view>
		</view>
		<view class="books-right" v-for="(bookDataList,bookDataKey) in bookData" :key="bookDataKey" v-if="listActive == bookDataKey">
			<view class="books-screen">
				<view class="books-label" :class="{'books-active':screenActive === 0 }" @click="screenBtn(0)">日榜</view>
				<view class="books-label" :class="{'books-active':screenActive ===  1 }" @click="screenBtn(1)">周榜</view>
				<view class="books-label" :class="{'books-active':screenActive === 2 }" @click="screenBtn(2)">月榜</view>
			</view>
			<!-- 图书列表 -->
			<view class="bookrack-body">
				<view class="books-box" v-for="(booksList,booksKey) in bookDataList.books" :key="booksKey" @click="booksBtn($event,booksKey,'bookHeight' + booksKey,booksList,booksList)"
				 :id="'bookHeight' + booksKey">
					<view class="books-image">
						<image class="book-img" :src="booksList.url"></image>
					</view>
					<view class="books-text">
						<view class="books-head">
							<view class="head-left">{{booksKey + 1}}. {{booksList.head}}</view>
							<view class="head-right" v-if="booksKey < 3">
								<image class="ranking-img" :src="ranking[booksKey]"></image>
								<!-- 图片按照原比例设置宽高 -->
							</view>
						</view>
						<view class="books-describe">
						<p>{{booksList.text}} <span class="popularity">{{booksList.popularity}}热度</span></p> 
						</view>
					</view>
					<view class="ripple" v-if="booksKey == bookNum" :style="{ top: leftY + 'px', left: topX + 'px' , }"></view>
				</view>
			</view>

		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				ranking:['https://s1.ax1x.com/2020/08/27/d4VqfS.png','https://s1.ax1x.com/2020/08/27/d4VbY8.png','https://s1.ax1x.com/2020/08/27/d4VOSg.png'],
				bookData: [{ //左边选项卡标签
						list: '榜单',
						books: [{
								url: "https://s1.ax1x.com/2020/08/12/ajXteS.png",
								head: "血疫：埃博拉的故事",
								text: "东方玄幻",
								popularity:'564333',
								author: "理查德·普雷斯顿",
								id: "1"
							},
							{
								url: "https://s1.ax1x.com/2020/08/12/ajjt6x.png",
								head: "当呼吸化为空气",
								text: "东方玄幻",
								popularity:'453534',
								author: "保罗·卡拉尼什",
								id: "2"
							},
							{
								url: "https://s1.ax1x.com/2020/08/12/ajjfHS.png",
								head: "浮生六记",
								text: "东方玄幻",
								popularity:'432321',
								author: "沈复 ",
								id: "3"

							},
							{
								url: "https://s1.ax1x.com/2020/08/12/ajjHcq.png",
								head: "小人物:我和父亲乔布斯",
								text: "东方玄幻",
								popularity:'345211',
								author: "[美]丽莎·布伦南·乔布斯",
								id: "4"

							},
							{
								url: "https://s1.ax1x.com/2020/05/25/tpsFDH.png",
								head: "法语有意思：从入门到不放弃",
								text: "东方玄幻",
								popularity:'323463',
								author: "曾小so",
								id: "5"

							},
							{
								url: "https://s1.ax1x.com/2020/08/12/ajvsVU.png",
								head: "史蒂夫·乔布斯传（修订版）",
								text: "东方玄幻",
								popularity:'313552',
								author: "沃尔特·艾萨克森 ",
								id: "6"

							},
							{
								url: "https://s1.ax1x.com/2020/08/12/ajvTaD.png",
								head: "只有医生知道",
								text: "东方玄幻",
								popularity:'297475',
								author: "张羽",
								id: "7"
							},
						]
					},
					{
						list: '小说',
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajjt6x.png",
							head: "当呼吸化为空气",
							text: "东方玄幻",
							popularity:'852223',
							author: "保罗·卡拉尼什",
							id: "2"
						}, ]
					},
					{
						list: '听书',
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajzdBV.png",
							head: "30 岁前的每一天：超实用梦想管理指南（新版）",
							text: "东方玄幻",
							popularity:'786798',
							author: "水湄物语",
							id: "8"
						}, ]
					},
					{
						list: '精选',
						amount: '1489',
						sortList: ['最热', '最新', '最火'],
						classList: ['全部', '职场心理', '面试心理', '谈话心理'],
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajjt6x.png",
							head: "当呼吸化为空气",
							text: "东方玄幻",
							popularity:'689074',
							author: "保罗·卡拉尼什",
							id: "2"
						}, ]
					},
					{
						list: '免费',
						amount: '6798',
						sortList: ['最热', '最新', '最火'],
						classList: ['全部', '文学艺术', '行为艺术', '话术艺术'],
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajzdBV.png",
							head: "30 岁前的每一天：超实用梦想管理指南（新版）",
							text: "东方玄幻",
							popularity:'546423',
							author: "水湄物语",
							id: "8"
						}, ]
					},
				],
				bookNum: null, //
				topX: '', //x轴
				leftY: '', //y轴
				listActive: 0, //左边tab
				screenActive: 0, //日榜 周榜 月榜

			}
		},

		methods: {
			screenBtn(value) {
				 //日榜 周榜 月榜
				this.screenActive = value
			},
			booksBtn(e, value, topH, booksdata) {
				//清空遗留数据
				this.bookNum = null
				this.topX = null
				this.leftY = null
				//开始脑残逻辑
				this.bookNum = value //第几个波纹效果显示
				this.topX = e.detail.x - 80 //获取相对于屏幕X轴坐标，并赋值 
				const query = uni.createSelectorQuery().in(this);
				query.select('#' + topH).boundingClientRect(data => {
					let cardT = JSON.stringify(data.top) //获取点击容器到顶距离
					let cardY = e.detail.y //获取相对于屏幕y轴坐标，并赋值
					this.leftY = Number(cardY) - Number(cardT) //计算容器内点击Y轴坐标
				}).exec();
                  
				  
				 
				 	
				 	uni.navigateTo({
				 		url: '../../pages/booksDetails/booksDetails'
				 	})
				  
			},
			listBtnOne(value, text) { //左边选项卡切换按钮
				this.lcalssActive = 0
				this.labelActive = 0
				this.bookNum = null
				this.listActive = value

			},
		}
	}
</script>

<style scoped lang="scss">
	$booksH:128upx;
	$booksW:96upx;
	$second: 0.6s;

	.books-body {
		display: flex;
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
	}

	.books-left {
		margin-top: 200rpx;
		flex: 1;
		min-width: 160upx;
		max-width: 160upx;
		height: 100%;
		overflow-y: auto;
		background-color: $bgLightGray;
	}

	.books-right {
		margin-top: 150rpx;
		flex: 1;
		overflow-y: auto;
		min-width: calc(100% - 160upx);
		max-width: calc(100% - 160upx);

		.books-screen {
			height: 80upx;
			width: 100%;
			background-color: #fff;
			padding: 0 $bleed;

			.books-label {
				display: inline-table;
				margin-right: 20upx;
				margin-top: 16upx;
				height: 48upx;
				font-size: 24upx;
				padding: 0 26upx;
				line-height: 48upx;
				border-radius: 48upx;
				background-color: $bgLightGray;
				color: $mediumGrey;
			}

			.books-active {
				background-color: $lightBlue;
				color: $dominantHue;
			}

		}

		.bookrack-body {
			height: calc(100% - 80upx);
			width: 100%;
			overflow-y: auto;
			padding-bottom: 40upx;
		}

		.books-box {
			display: flex;
			position: relative;
			overflow: hidden;
			height: $booksH + 40upx;
			padding: 20upx $bleed 20upx $bleed;
		}

		.books-image {
			flex: 1;
			min-width: $booksW + 20upx;
			max-width: $booksW + 20upx;
			height: $booksH;
			position: relative;
			z-index: 10;

			.book-img {
				width: $booksW;
				height: $booksH;
				border-radius: 8upx;
				overflow: auto;
				background-color: $skeletonColor;
			}
		}

		//波纹效果
		.ripple {
			width: 1px;
			height: 1px;
			position: absolute;
			z-index: -5;
			background-color: $rippleBg;
			box-shadow: 0 0 0px 500px $rippleBg;
			border-radius: 50%;
			animation: myfirst $second;
			opacity: 0.6;
		}

		.books-text {
			flex: 1;
			padding: 8upx 0;
			position: relative;
			z-index: 10 !important;
			height: $booksH;
			.books-head {
				display: flex;
				
				.head-left{
					font-size: 28upx;
					line-height: 38upx;
					font-weight: 400;
					height: 76upx;
					flex: 1;
					overflow: hidden;
					text-overflow: ellipsis;
					display: -webkit-box;
					-webkit-line-clamp: 2;
					-webkit-box-orient: vertical;
				}
				.head-right{
					flex: 1;
					min-width: 38upx;
					max-width: 38upx;
					.ranking-img{
						width: 38upx;
						height: 45upx;
					}
				}
			}

			.books-describe {
				height: $booksH - 76upx - 16upx;
				color: $mediumGrey;
				font-size: 26upx;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 1;
				-webkit-box-orient: vertical;
				.popularity{
					float: right;
					font-size: 24upx;
					font-size: 24upx;
				}
			}
		}

	}



	.left-list {
		height: 120upx;
		line-height: 120upx;
		text-align: center;
		font-size: 28upx;
		color: $mediumGrey;

		.vertical-bar {
			display: none;
			height: 60upx;
			width: 8upx;
			background-color: $dominantHue;
			border-radius: 8upx;
			float: left;
			margin-top: 30upx;
		}
	}

	.books-left-active {
		color: $dominantHue;
		background-color: #fff;
		padding-right: 8upx;

		.vertical-bar {
			display: block;
		}
	}

	.bookrack-body::-webkit-scrollbar {
		display: none !important;
	}



	@keyframes myfirst {
		from {
			box-shadow: 0 0 0px 30px $rippleBg;
			opacity: 1;
			// z-index: 5!important;
		}

		to {
			box-shadow: 0 0 0px 500px $rippleBg;
			opacity: 0.5;
			// z-index: 5!important;
		}
	}

	.filtrate {
		position: absolute;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 100;

		.shade {
			position: absolute;
			top: 0;
			bottom: 0;
			left: 0;
			right: 0;
			background-color: rgba(0, 0, 0, 0.5);
		}

		.label {
			width: 100%;
			padding: 20upx $bleed 40upx $bleed;
			background-color: #fff;
			position: absolute;
			z-index: 200;

			.label-list {
				padding: 30upx 0 10upx 0;
				color: $mediumGrey;
				font-size: 24upx;

				.pack {
					color: $dominantHue;
					float: right;

					.pack-up {
						font-size: 24upx;
						margin-left: 4upx;
						display: inline-table;
					}
				}

				.label-boxs {
					padding: 0 10upx;

					.label-span {
						font-size: 26upx;
						color: $mediumGrey;
						display: inline-block;
						height: 56upx;
						line-height: 56upx;
						padding: 0 40upx;
						margin: 30upx 30upx 0 0;
						background-color: $bgLightGray;
						border-radius: 56upx;
					}

					.label-active {
						color: $dominantHue;
						background-color: $lightBlue;
					}
				}
			}
		}
	}
</style>
