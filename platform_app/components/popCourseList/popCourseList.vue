<template>
	<view>
		<view class="image-list" v-for="(imgdata,imgdataKey) in courseList" :key="imgdataKey">
			<!-- 标题 -->
			<view class="image-head">
				<p>
					{{imgdata.head}}
				</p>
			</view>
			<!-- 图文列表 -->
			<view class="books" v-for="(information,dataKey) in imgdata.data" :key="dataKey" @click="imageTextBtn(information,dataKey)">
				<view class="books-img">
					<image :src="information.imgSrc" />
				</view>
				<view class="books-text">
					<p class="head">{{information.courseName}}</p>
					<view class="title-box">
						<p class="title">{{ information.etc.teacher_name}}</p>
					</view>
					<view class="title-bottom">
						<p class="title">{{information.introduction}}</p>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		props: ['courseList'],
		data() {
			return {

			}
		},
		methods: {
			imageTextBtn(information, imageTextBtn) {
				let data = information
				uni.setStorage({
					key: 'course',
					data: data,
					success: function() {
						uni.navigateTo({
							url: "../../pages/booksDetails/booksDetails"
						})
					}
				});
			}
		}
	}
</script>

<style scoped lang="scss">
	.image-list {
		padding: 0 $bleed;
	}

	.image-head {
		line-height: 120upx;
		height: 120upx;

	}

	.image-head p {
		font-size: 38upx;
	}

	.image-head span,
	.image-head span i {
		float: right;
		color: $mediumGrey;
		font-size: 26upx;
	}

	.image-head span .iconfont {
		font-size: 20upx;
	}

	.books {
		display: flex;
		margin-bottom: 30upx;
	}

	.books-img image {
		width: 160upx;
		height: 212upx;
		background-color: $skeletonColor;
		border-radius: 8upx;
	}

	.books-text {
		height: 212upx;
		padding: 16upx 0 16upx 20upx;
		box-sizing: border-box;
	}

	.books-text .head {
		font-size: 30upx;
		height: 40upx;
		line-height: 40upx;
	}

	.title {
		font-size: 26upx;
		color: $mediumGrey;
	}

	.title-box {
		display: flex;
		height: 104upx;
		line-height: 104upx;
	}

	.title-box .title {
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}

	.title-box .text-right {
		text-align: right;
		font-size: 36upx;
		color: $redAll;
	}

	.title-bottom {
		height: 36upx;
		line-height: 36upx;
		display: flex;
	}

	.title-bottom .title {
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}

	.title-bottom .text-right {
		text-align: right;
		max-width: 160upx;
		text-decoration: line-through
	}
</style>
