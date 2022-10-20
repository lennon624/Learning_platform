<template>
	<view>
		<!-- 弹窗 -->
		<view class="more-shade" v-if="moreOff" @click.stop="moreOff = false">
			<view class="more">
				<view class="more-list more-underline" @click.stop="examine">查看详情</view>
				<view class="more-list more-underline remove" @click.stop="shiftOut">移出</view>
				<view class="more-list" @click.stop="moreOff =false">取消</view>
			</view>
		</view>
		<view class="bookrack-body" :class="{'bookrack-active' :checkboOff}">
			<view class="book-top">
				<view class="top-left">
					<view class="top-btn" v-if="!checkboOff" @click="managementBtn">
						<view>管理</view>
					</view>
					<view class="top-btn" v-if="checkboOff" @click="accomplish">
						完成
					</view>
				</view>
				<view class="top-right">
					<view class="input-cancel" v-if="searchOff">
						<view class="cancel-left">
							<input class="uni-input" confirm-type="search" maxlength="50" placeholder="请输入关键词搜索" />

						</view>
						<view class="cancel-right">
							<view class="top-btn" @click="cancelBtn">取消</view>
						</view>
					</view>
					<view class="top-btn" v-if="!checkboOff && !searchOff" @click="searchBtn">
						<!-- 搜索按钮 -->
						<span class="iconfont search"></span>
					</view>
				</view>
			</view>
			<view class="book-rack">
				<view class="book-card" v-for="(bookList,bookKey)  in bookData" :key="bookKey" @click="bookCardBtn($event,bookKey,'bookHeight' + bookKey,bookList)"
				 ref="bookcard">
					<view class="book-checkbox" v-if="checkboOff">
						<view class="checkboxcard" :class="{'checkboxactiva':bookList.checked}">
							<view v-if="bookList.checked" class="check"></view>
						</view>
					</view>
					<view class="image-text" :id="'bookHeight' + bookKey">
						<view class="ripple" v-if="bookKey == bookNum" :style="{ top: leftY + 'px', left: topX + 'px' }"></view>
						<view class="book-left">
							<image class="book-img" :src="bookList.imgSrc"></image>
						</view>
						<view class="book-right">
							<view class="flex book-text">
								<view class="head">{{bookList.courseName}}</view>
								<view class="author">{{bookList.etc.teacher_name}} 著</view>
								<view class="schedule">
								<!-- 	<view class="schedule-text">已读{{bookList.schedle}}%</view> -->
									<view class="book-icon" v-if="!checkboOff">
										<i class="iconfont omit" @click.stop="omitBtn(bookKey,bookList.id,bookList)"></i>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>


		</view>
		<!-- 底下悬浮窗口 -->
		<view class="book-bottom" v-if="checkboOff">
			<view class="bottom-box">
				<view class="bottom-box-left">
					<view class="top-btn" v-if="checkboOff && !checkAllOff" @click="checkAll">
						<view>全选</view>
					</view>
					<view class="top-btn" v-if="checkboOff && checkAllOff" @click="checkNooAll">
						<view>全不选</view>
					</view>
				</view>
				<view class="bottom-box-right">
					<view class="top-btn" @click="deleteBtn">
						<view>移出书架({{selection.length}})</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 吐司 -->
		<view>
			<u-toast ref="uToast" />
		</view>
	</view>
</template>

<script>
	import {
		getCourseById,
		getpcategorybycourse,
		getMyCourseList
	} from "@/api/modules/course.js";
	import {
		queryMyList,
		addCourse2List,
		removeCourseFromList,
		removeCourses
	} from "@/api/modules/student";
	export default {
		props: ["studentId"],
		components: {},
		data() {
			return {
				bookNum: null, // 
				topX: '', //x轴
				leftY: '', //y轴
				selection: [], //多选
				checkboOff: false, //显示多选开关
				checkAllOff: false, //全选全不选
				searchOff: false, //搜索
				moreOff: false, //更多弹窗开关
				moreId: '', //移除书籍ID
				courses: '',
				courseID:'',
				COURSE:'',
				bookData: [{
						id: "",
						imgSrc: "",
						introduction: "",
						notice: "",
						status: "",
						teacherId: "",
						etc: {
							teacher_name: "",
							teacher_id: "",
							school_name: "",
							school_id: ""
						}
					},
				]
			}
		},
		created() {
			
		},
		mounted() {

			
			this.getAll();
		},
		onLoad() {

		},
		methods: {
			//批量删除选课
			removeCourses() {
				console.log(this.selection);
				removeCourses({
						studentId: this.studentId,
						ids:this.selection.join(',')
					})
					.then(res => {
						console.log(res);
						//刷新页面
						this.getAll()
					})
					.catch(err => {
						this.toast(err, "error");
					});
				
			},
			//取消选课
			removecourse() {
				removeCourseFromList({
						student_id: this.studentId,
						course_id: this.courseID
					})
					.then(res => {
						console.log(res);
						//刷新页面
						this.getAll()
					})
					.catch(err => {
						this.toast(err, "error");
					});
				
			},
			//根据学生Id查询学生选课
			getAll() {
				
				getMyCourseList({
					
						studentId: this.studentId
					})
					.then(res => {
						this.bookData = res.data.data;
						// this.toast(JSON.stringify(res.data), "success", "bottom")
					})
					.catch(err => {
						this.toast(err, 'error');
					});
			},

			bookCardBtn(e, value, id, course) { //卡片点击按钮
				//清空遗留数据
				this.bookNum = null
				this.topX = null
				this.leftY = null
				//开始脑残逻辑
				this.bookNum = value //第几个波纹效果显示
				this.topX = e.detail.x //获取相对于屏幕X轴坐标，并赋值 
				const query = uni.createSelectorQuery().in(this);
				query.select('#' + id).boundingClientRect(data => {
					let cardT = JSON.stringify(data.top) //获取点击容器到顶距离
					let cardY = e.detail.y //获取相对于屏幕y轴坐标，并赋值
					this.leftY = Number(cardY) - Number(cardT) //计算容器内点击Y轴坐标
				}).exec();
				if (this.checkboOff == true) { //多选状态下的事件
					this.selection = []
					this.topX = e.detail.x - 44
					this.bookData[value].checked = !this.bookData[value].checked
					this.bookData.forEach(item => {
						if (item.checked === true) {
							this.selection.push(item.id)
						}
					})
					if (this.selection.length == this.bookData.length) {
						this.checkAllOff = true
					} else if (this.selection.length < this.bookData.length) {
						this.checkAllOff = false
					}

					console.log(this.selection)
				} else { //非多选状态下的事件
					console.log('你点了第' + value + '个')
					// 跳转页面
					uni.setStorage({
						key: 'course',
						data: course,
						success: function() {
							uni.navigateTo({
								url: "../../pages/booksDetails/booksDetails"
							})
						}
					});

				}
				//去TM的安卓APP、苹果APP、微信小程序、微信网页、H5的全兼容，具体兼容那些版本我不测了。
			},
			managementBtn() { //管理按钮，打开多选和删除。
				this.selection = []
				this.checkboOff = true
				this.bookData.forEach(item => {
					this.$set(item, 'checked', false)
				})
			},
			checkAll() { //全选按钮
				//清空缓存
				this.selection = []
				//全选全不选显示切换
				this.checkAllOff = !this.checkAllOff
				//遍历数组
				this.bookData.forEach(item => {
					//新增属性
					this.$set(item, 'checked', true)
					this.selection.push(item.id)
				})
				console.log(this.selection)
			},
			checkNooAll() { //全不选按钮
				//清空缓存
				this.selection = []
				//全选全不选显示切换
				this.checkAllOff = !this.checkAllOff
				//遍历数组
				this.bookData.forEach(item => {
					this.$set(item, 'checked', false)
				})
				console.log(this.selection)
			},
			omitBtn(value, id,course) { //更多，打开针对单个的删除处理。
				console.log('你点了第' + value + '个更多，id为' + id)
				this.moreId = ''
				this.moreId = id
				this.moreOff = true
				this.courseID = id
				this.COURSE = course
			},
			deleteBtn() { //移出书架按钮
				console.log(this.selection)
				this.removeCourses();
				if (this.selection.length > 0) {
					//移除书架后缓存清空复位
					this.selection = []
					this.checkboOff = false
					this.checkAllOff = false
				} else {

				}
			},
			searchBtn() { //搜索按钮
				this.searchOff = true
			},
			cancelBtn() { //取消按钮
				// 清空复位
				this.searchOff = false
				this.selection = []
				this.checkboOff = false
				this.checkAllOff = false
			},
			accomplish() { //完成按钮
				// 清空复位
				this.searchOff = false
				this.selection = []
				this.checkboOff = false
				this.checkAllOff = false
			},
			examine() { //查看详情
				this.moreOff = false
				// 跳转页面
				uni.setStorage({
					key: 'course',
					data: this.COURSE,
					success: function() {
						uni.navigateTo({
							url: "../../pages/booksDetails/booksDetails"
						})
					}
				});
				console.log('你查看了ID为' + this.moreId + '的书籍详情')
			},
			shiftOut() { //移出按钮
				this.moreOff = false
				console.log('你移出了ID为' + this.moreId + '的书籍')
				this.removecourse()
			}
		},
		created() {}
	}
</script>

<style scoped lang="scss">
	$bookTop:46px;
	$bookCardHeight:200upx;
	$second: 0.6s;

	.bookrack-body {
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

	.book-top {
		height: $bookTop;
		display: flex;
		border-bottom: 1px solid $brimColor;
		padding: 0 20upx;
		background-color: #fff;
		position: fixed;
		width: 100%;
		z-index: 100;
		margin-top: -2upx;

		.top-left {
			flex: 1;
			text-align: left;
			line-height: $bookTop;
			color: $dominantHue;
			max-width: 100upx;
		}

		.top-right {
			flex: 1;
			line-height: $bookTop;
			text-align: right;
			color: $mediumGrey;
		}
	}

	.book-rack::-webkit-scrollbar {
		display: none
	}

	.book-rack {
		padding-top: $bookTop;




		.uni-radio-group uni-label,
		uni-checkbox-group uni-label {
			padding-right: 0 !important;
		}

		.book-card {
			display: flex;

			.book-checkbox {
				flex: 1;
				line-height: $bookCardHeight;
				padding: 10px 0 10px $bleed;
				min-width: 88upx;
				max-width: 88upx;
				box-sizing: border-box;
				animation: book-interaction 0.1s;
			}

			@keyframes book-interaction {
				0% {
					min-width: 0upx;
					max-width: 0upx;
				}

				100% {
					min-width: 88upx;
					max-width: 88upx;
				}
			}

			.image-text {
				flex: 1;
				display: flex;
				height: $bookCardHeight + 40upx;
				padding: 10px $bleed 10px $bleed;
				position: relative;
				overflow: hidden;
				left: 0px;
				right: 0px;



				.book-left {
					flex: 1;
					min-width: 170upx;
					max-width: 170upx;
					height: $bookCardHeight;
					position: relative;
					z-index: 10;

					.book-img {
						width: 150upx;
						height: $bookCardHeight;
						border-radius: 8upx;
						overflow: auto;
						background-color: $skeletonColor;
					}
				}

				.book-right {
					position: relative;
					flex: 1;
					height: $bookCardHeight;
					z-index: 10;

					.book-text {
						padding: 4upx 0;
						height: 100%;
						box-sizing: border-box;

						.head {
							font-size: 30upx;
							height: 32upx;
							line-height: 32upx;
							color: $blackAll;
							font-weight: 400;
							overflow: hidden;
							text-overflow: ellipsis;
							display: -webkit-box;
							-webkit-line-clamp: 1;
							-webkit-box-orient: vertical;
						}

						.author {
							padding-top: 24upx;
							font-size: 28upx;
							color: $mediumGrey;
							height: calc(100% - 8upx - 24upx - 28upx);
						}

						.schedule {
							line-height: 28upx;
							height: 28upx;
							display: flex;

							.schedule-text {
								flex: 1;
								font-size: 28upx;
								color: $mediumGrey;
							}

							.book-icon {
								flex: 1;
								min-width: 55upx;
								max-width: 55upx;
								text-align: right;

								.omit {
									font-size: 40upx;
									line-height: 28upx;
									color: $lightGray;
									padding: 10upx;
								}
							}
						}
					}
				}
			}
		}
	}

	.top-btn {
		height: 48upx;
		line-height: 48upx;
		padding: 0upx 12upx;
		font-size: 28upx;
		display: inline-block;

		.search {
			font-size: 36upx;
		}
	}

	.top-btn:active {
		background-color: $rippleBg;
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

	@keyframes myfirst {
		from {
			box-shadow: 0 0 0px 30px $rippleBg;
			opacity: 1;
		}

		to {
			box-shadow: 0 0 0px 500px $rippleBg;
			opacity: 0.5;
		}
	}

	// 底部悬浮
	.book-bottom {
		position: fixed;
		left: 0;
		bottom: 0;
		right: 0;
		height: $navHeight + 12px;
		background-color: #fff;
		z-index: 110;
		border-top: 1px solid $brimColor;

		.bottom-box {
			height: $navHeight;
			padding: 0 15upx;
			line-height: $navHeight;
			color: $dominantHue;
			display: flex;

			.bottom-box-left {
				flex: 1;
				text-align: left;
			}

			.bottom-box-right {
				flex: 1;
				text-align: right;
			}
		}
	}


	//复选框 原生的处理起来有点难 
	.checkboxcard {
		width: 38upx;
		height: 38upx;
		border-radius: 8upx;
		border: 1px solid #DCDFE6;
		margin-top: calc(100upx - 20upx)
	}

	.checkboxactiva {
		border: 1px solid $dominantHue;
		box-shadow: 0 0 2px rgba(0, 134, 231, 0.5);
	}

	.check {
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.check::after {
		margin-top: -6upx;
		width: 6px;
		height: 12px;
		border-style: solid;
		border-color: $dominantHue;
		border-width: 0 2px 2px 0;
		-webkit-transform: rotateZ(45deg);
		content: " ";
		animation: check-interaction 0.1s;
	}

	@keyframes check-interaction {
		0% {
			width: 0px;
			height: 0px;
			margin-top: -6upx;
			margin-right: 0px;
		}

		35% {
			width: 6px;
			height: 0px;

		}

		100% {
			height: 12px;
			margin-top: -6upx;
			margin-right: 0px;
		}
	}



	.input-cancel {
		display: flex;

		.cancel-left {
			flex: 1;

			.uni-input {
				line-height: 36px;
				height: 36px;
				font-size: 28upx;
				margin-top: calc((46px - 36px) / 2);
				background-color: #f4f4f4;
				border-radius: 8upx;
				padding: 0 20upx;
				text-align: left;
			}
		}

		.cancel-right {
			flex: 1;
			max-width: 85upx;
			min-width: 85upx;
		}
	}



	.search-activa {
		z-index: 30;
		top: 20px;
		// bottom: 0px;
		animation: search-interaction 0.2s;
	}

	.subject {
		transition: top 0.2s;
	}

	@keyframes search-interaction {
		0% {
			top: $barHeight + 10px;
		}

		100% {
			top: 20px;
		}
	}

	.more-shade {
		position: fixed;
		background-color: rgba(0, 0, 0, 0.4);
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}

	.more {
		position: fixed;
		top: calc((100% - 300upx) / 2);
		left: 10%;
		right: 10%;
		background-color: #fff;
		border-radius: 4upx;

		.more-list {
			height: 100upx;
			line-height: 100upx;
			font-size: 30upx;
			padding: 0 40upx;
			color: $dominantHue;
		}

		.more-underline {
			border-bottom: 1px solid #F1F1F1;
		}

		.remove {
			color: $redAll;
		}
	}

	.bookrack-active {
		bottom: $navHeight + 12px;
	}

	/*苹果x适配 H5APP*/
	@media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) {
		.book-bottom {
			height: $navHeight + $navBoxHeight + 12px;
		}

		.bookrack-body {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
			/* #ifdef H5 */
			top: 44px;
			bottom: $navHeight;
			/* #endif */
		}

		.bookrack-active {
			bottom: $navHeight + $navBoxHeight + 12px;
		}


		.search-activa {
			z-index: 30;
			top: $barHeight;
			animation: search-interaction 0.2s;
		}

		@keyframes search-interaction {
			0% {
				top: $barHeight + $barTopHeight;
			}

			100% {
				top: $barHeight;
			}
		}
	}

	/*苹果xs适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
		.book-bottom {
			height: $navHeight + $navBoxHeight + 12px;
		}

		.bookrack-body {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
			/* #ifdef H5 */
			top: 44px;
			bottom: $navHeight;
			/* #endif */
		}

		.bookrack-active {
			bottom: $navHeight + $navBoxHeight + 12px;
		}


		.search-activa {
			z-index: 30;
			top: $barHeight;
			animation: search-interaction 0.2s;
		}

		@keyframes search-interaction {
			0% {
				top: $barHeight + $barTopHeight;
			}

			100% {
				top: $barHeight;
			}
		}
	}

	/*苹果xr适配 H5APP*/
	@media only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
		.book-bottom {
			height: $navHeight + $navBoxHeight + 12px;
		}

		.bookrack-body {
			top: $stairTopTow;
			bottom: $navHeight + $navBoxHeight;
			/* #ifdef H5 */
			top: 44px;
			bottom: $navHeight;
			/* #endif */
		}

		.bookrack-active {
			bottom: $navHeight + $navBoxHeight + 12px;
		}

		.search-activa {
			z-index: 30;
			top: $barHeight;
			animation: search-interaction 0.2s;
		}

		@keyframes search-interaction {
			0% {
				top: $barHeight + $barTopHeight;
			}

			100% {
				top: $barHeight;
			}
		}
	}
</style>
