<template>
	<view>
		<view class="image-list" v-for="(imgdata,imgdataKey) in imglisttwo" :key="imgdataKey">
			<!-- 标题 -->
			<view class="image-head">
				<p>
					{{imgdata.head}}
					<span>
						查看更多
						<i class="iconfont right" />
					</span>
				</p>
			</view>
			<!-- 图文列表 -->
			<view class="books" v-for="(information,dataKey) in imgdata.data" :key="dataKey" @click="imageTextBtn(dataKey)">
				<view class="books-img">
					<image :src="information.url" />
				</view>
				<view class="books-text">
					<p class="head">{{information.head}}</p>
					<view class="title-box">
						<p class="title">{{ information.author}}</p>
						<p class="title text-right">￥{{information.price}}</p>
					</view>
					<view class="title-bottom">
						<p class="title">{{information.title}}</p>
						<p class="title text-right ">原价：￥{{information.initialize}}</p>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		props: ['imglisttwo'],
		data() {
			return {

			}
		},
		methods: {
			imageTextBtn(imageTextBtn) {
			 	// console.log('你点击了第' + imageTextBtn + '个')
				uni.navigateTo({
					url: '../../pages/booksDetails/booksDetails'
				})
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
