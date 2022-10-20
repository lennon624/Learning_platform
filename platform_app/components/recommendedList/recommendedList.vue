<template>
	<view>
		<view class="image-list" v-for="(imageLists,imgListKey) in imageList" :key="imgListKey">
			<!-- 标题 -->
			<view class="image-head">
				<p>
					{{imageLists.head}}
				</p>
			</view>
			<!-- 图文列表 -->
			<view class="books" v-for="(imageData,imageDataKey ) in imageLists.imgdata" :key="imageDataKey">
				<view class="books-left" :class="{'books-centre' :imgKey===1 ,'books-right' :imgKey===2 , }" v-for="(img,imgKey ) in imageData.data"
				 :key="imgKey">
					<view class="books-box" @click="booksBtn(imageDataKey,imgKey,img)">
						<view class="books-top">
							<image class="books-img" :src="img.imgSrc" />
						</view>
						<view class="books-text">
							<text class="books-title">
								{{img.courseName}}
							</text>
							<text class="books-author">
								{{img.etc.teacher_name}}
							</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		props: ['imageList'],
		data() {
			return {

			}
		},
		methods: {
			booksBtn(imageDataKey, imgKey, img) {
				let data = img
				uni.setStorage({
				    key: 'course',
				    data: data,
				    success: function () {
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
	$booksimgW:190upx;
	$booksimgH:220upx;

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
	}

	.books-left {
		flex: 1;
		padding-bottom: 38upx;
	}

	.books-img {
		width: $booksimgW;
		height: $booksimgH;
		background-color: $skeletonColor;
		border-radius: 8upx;
	}

	.books-box {
		width: $booksimgW;
		display: inline-block;

	}

	.books-centre {
		text-align: center;
	}

	.books-right {
		text-align: right;
	}

	.books-text {
		padding: 12upx 0 0upx 0;


	}

	.books-title {
		font-size: 26upx;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.books-author {
		font-size: 24upx;
		text-align: left;
		color: $mediumGrey;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}
</style>
