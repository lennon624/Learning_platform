<template>
	<view class="books-body">
		<!-- 左边切换 -->
		<view class="books-left">
			<view class="left-list" :class="{'books-left-active': listActive == bookDataKey}" v-for="(bookDataList,bookDataKey) in bookData"
			 @click="listBtnOne(bookDataKey,bookDataList.categoryName,bookDataList.id)" :key="bookDataKey" >
				<view class="vertical-bar"></view>
				{{bookDataList.categoryName}}
			</view>
		</view>  
		<!-- 筛选弹出层 -->
		<view class="books-right" v-for="(bookDataList,bookDataKey) in bookData" :key="bookDataKey" v-if="listActive == bookDataKey">
			<view class="books-screen">
				<p class="amount">4本图书<span class="screen-span" @click="classifyBtn">最热·全部</span></p>
				<view class="filtrate" v-if="filtrateOff">
					<view class="label">
						<view class="label-list">
							<p>排序
								<span class="pack" @click="packUpBtn">
									收起
									<i class="iconfont pack-up"></i>
								</span>
							</p>
							<view class="label-boxs">
								<view v-for="(classList,classKey) in bookDataList.sortList" :key="classKey" class="label-span" :class="{'label-active':labelActive == classKey}"
								 @click="labelBtn(classKey,classList)">{{classList}}
								</view>
							</view>
						</view>
						<view class="label-list" v-if="bookDataList.classList">
							<p>
								分类标签
							</p>
							<view class="label-boxs">
								<view class="label-span" :class="{'label-active': lcalssActive == labelKey}" v-for="(labelList,labelKey) in bookDataList.classList"
								 :key="labelKey" @click="labelListBtn(labelKey,labelList)">
									{{labelList}}
								</view>
							</view>
						</view>
					</view>
					<!-- 遮罩层 -->
					<view class="shade" @click="packUpBtn"></view>
				</view>
			</view>
			<!-- 图书列表 -->
			<view class="bookrack-body">
				<view class="books-box" v-for="(booksList,booksKey) in courses" :key="booksKey" @click="booksBtn($event,booksKey,'bookHeight' + booksKey,booksList)"
				 :id="'bookHeight' + booksKey">
					<view class="books-image">
						<image class="book-img" :src="booksList.imgSrc"></image>
					</view>
					<view class="books-text">
						<view class="text-box">
							<view class="books-head">{{booksList.courseName}}</view>
							<view class="books-describe">{{booksList.introduction}}</view>
						</view>
						<view class="books-author">{{booksList.etc.teacher_name}} 任课</view>
					</view>
					<view class="ripple" v-if="booksKey == bookNum" :style="{ top: leftY + 'px', left: topX + 'px' , }"></view>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	 
	import {
		getCourseByCategory
	} from "@/api/modules/course.js";
	export default {
		components: {},
		props: ['bookData'],
		data() {
			return {
				bookNum: null, //
				topX: '', //x轴
				leftY: '', //y轴
				filtrateOff: false, //筛选弹出层
				lcalssActive: 0, //分类标签
				labelActive: 0, //排序标签 0最热 1最新 2完结
				listActive: 0, //左边tab
				courses:[],
				id:null,
			}
		},
		onLoad() {

		},
		mounted() {
			
			console.log(this.bookData);
			
			// this.getCourseByCategory1(this.bookData[0].id)
		},
		//自动触发点击事件
		// directives:{
		//       trigger:{
		//         inserted(el,binging){
		//           console.log("自动触发事件")
				  
		//           el.click()
		//         }
		//       }
		// },
		methods: {
			
			
			//通过选项卡查询对应课程
			getCourseByCategory1(id) {
				getCourseByCategory({category_id : id})
					.then(res => {
						this.courses = res.data.data;
						console.log(this.courses)
					})
					.catch(err => {
						// this.toast(err, 'error');
					});
			},
			
			booksBtn(e,value,topH,booksdata) {
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
				//接下来是组件父子交流
				let data = booksdata
				uni.setStorage({
				    key: 'course',
				    data: booksdata,
				    success: function () {
						console.log(data);
				       uni.navigateTo({
				       	url: "../../pages/booksDetails/booksDetails"
				       })
				    }
				});
				
				this.$emit('booksDataBtn', data);
			},
			listBtnOne(value, text,id) { //左边选项卡切换按钮
			this.id = id,
			this.getCourseByCategory1(this.id),
			
			
				this.lcalssActive = 0
				this.labelActive = 0
				this.bookNum = null
				this.listActive = value
				console.log(this.listActive);
				console.log(id);
				//接下来是组件父子交流
				let data = text
				this.$emit('listTabBtn', data);
			},
			labelBtn(value, text) { //排序标签 1最热 2最新 3完结
				this.labelActive = value

				//接下来是组件父子交流
				let data = text
				this.$emit('labelSortBtn', data);

			},
			labelListBtn(value, text) { //分类标签
				this.lcalssActive = value
				//接下来是组件父子交流
				let data = text
				this.$emit('classLabelBtn', data);
			},
			classifyBtn() { //打开筛选
				this.filtrateOff = true
			},
			packUpBtn() { //收起筛选
				this.filtrateOff = false
			}

		}
	}
</script>

<style scoped lang="scss">
	$classifyTopH:88upx;
	$booksH:176upx;
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
		flex: 1;
		min-width: 160upx;
		max-width: 160upx;
		height: 100%;
		overflow-y: auto;
		background-color: $bgLightGray;
	}

	.books-right {
		flex: 1;
		overflow-y: auto;
		min-width: calc(100% - 160upx);
		max-width: calc(100% - 160upx);

		.books-screen {
			height: 80upx;
			width: 100%;
			background-color: #fff;
			padding: 0 $bleed;

			.amount {
				font-size: 24upx;
				color: $mediumGrey;
				line-height: 80upx;

				.screen-span {
					float: right;
					height: 50upx;
					line-height: 46upx;
					font-size: 22upx;
					border: 2upx solid $brimColor;
					padding: 0 24upx;
					border-radius: 52upx;
					color: $blackAll;
					margin-top: 15upx;

				}
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
			min-width: 152upx;
			max-width: 152upx;
			height: $booksH;
			position: relative;
			z-index: 10;

			.book-img {
				width: 132upx;
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
			padding: 4upx 0;
			position: relative;
			z-index: 10 !important;

			.text-box {
				height: $booksH - 38upx;
			}

			.books-head {
				font-size: 28upx;
				line-height: 36upx;
				font-weight: 400;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
				margin-bottom: 10upx;
			}

			.books-describe {
				color: $mediumGrey;
				font-size: 26upx;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 1;
				-webkit-box-orient: vertical;
			}

			.books-author {
				color: $mediumGrey;
				font-size: 26upx;
				line-height: 30upx;
				height: 30upx;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 1;
				-webkit-box-orient: vertical;
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
