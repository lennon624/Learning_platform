<template>
	<view class="pt">
		<view class="pt-verification-box">
			<view class="pt-verification-images">
				<view class="iconfont refresh" @click="refresh">&#xe64c;</view>
				<image mode="widthFix" :src="bgImg" class="bg-img"></image>
				<image :src="maskImg" class="drag-img" :style="{ left: dragWidth + 'px'}"></image>
			</view>
			<view class="pt-dragbar">
				<view :class="['pt-drag-area',{fail: fail,success: success}]" :style="{ width: dragWidth + 'px'}" v-if="dragWidth"></view>
				<movable-area class="pt-dragbar-area">
					<movable-view
						:class="['pt-dragbar-view',{active: dragWidth > 2,fail: fail,success: success}]"
						:direction="direction"
						@change="drag"
						@touchend="dragEnd"
						:damping="200"
						:x="x"
						:animation="false"
						:disabled="disabled"
						:data-dragWidth="dragWidth"><text class="iconfont">
							<block v-if="success">&#xe687;</block>
							<block v-else-if="fail">&#xe65c;</block>
							<block v-else>&#xe62a;</block>
						</text>
					</movable-view>
					<text v-if="dragWidth==0" class="tips">{{tips}}</text>
				</movable-area>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			bgImg: {
				type: String,
				default: ''
			},
			maskImg: {
				type: String,
				default: ''
			},
			left: {
				type: Number,
				default: 140
			},
			// 滑块滑动的方向
			direction: {
				type: String,
				default: 'horizontal'
			},
			tips: {
				type: String,
				default: '向右拖动滑块填充拼图'
			}
		},
		data() {
			return {
				disabled: false,
				dragWidth: 0,
				success: false,
				fail: false,
				x: 0
			};
		},
		methods: {
			// 开始拖拽
			drag(e){
				this.dragWidth = e.detail.x
			},
			// 停止滑动后判断前后误差不超过5px，可自行修改
			dragEnd(e){
				let mins = this.left - 5,
						max = this.left + 5
				this.x = e.currentTarget.dataset.dragwidth
				if(e.currentTarget.dataset.dragwidth > 0 && !this.disabled){
					let result = this.isRangeIn(e.currentTarget.dataset.dragwidth,max,mins)
					if(!result){
						this.fail = true
						uni.showToast({
							icon: 'none',
							title: '验证失败，再来一次吧',
							duration: 1500
						})
						// 验证失败延迟重置
						setTimeout(() => {
							this.$emit('refresh')
							this.dragWidth = 0
							this.x = 0
							this.fail = false
						},1500)
					}else{
						this.success = true
						this.disabled = true
						this.$emit('success')
					}
				}
			},
			isRangeIn(num,maxnum,minnum ) {
				var num = parseFloat(num);
				if(num <= maxnum && num >= minnum){
						return true;
				}
				return false;
			},
			refresh(){
				this.dragWidth = 0
				this.fail = false
				this.success = false
				this.x = 0
				this.disabled = false
				this.$emit('refresh')
			}
		}
	}
</script>

<style lang="scss">
	@font-face {
	  font-family: 'iconfont';  /* project id 2047533 */
	  src: url('https://at.alicdn.com/t/font_2047533_o8axbabfs3.ttf') format('truetype')
	}
	.iconfont {
	  font-family: iconfont !important;
	  font-size: 16px;
	  font-style: normal;
	  -webkit-font-smoothing: antialiased;
	  -moz-osx-font-smoothing: grayscale;
	}
	.pt{
		width: 640rpx;
		margin: 0 auto;
		&-verification-images{
			position: relative;
			.refresh{
				position: absolute;
				right: 20rpx;
				top: 20rpx;
				z-index: 10;
				color: #FFF;
				font-weight: bold;
			}
			.bg-img{
				width: 100%;
				vertical-align: top;
			}
			.drag-img{
				position: absolute;
				width: 112rpx;
				top: -2rpx;
				height: 320rpx;
				left: 0;
				z-index: 1;
			}
		}
		&-dragbar{
			position: relative;
			height: 80rpx;
			background-color: #F7F7F7;
			border: solid 2rpx #EEE;
			margin-top: 20rpx;
			.pt-drag-area{
				position: absolute;
				height: 80rpx;
				border: solid 2rpx $uni-color-primary;
				background-color: #D1E9F1;
				top: -2rpx;
				&.fail{
					border-color: $uni-color-error;
					background-color: #ffdbdb;
				}
				&.success{
					border-color: $uni-color-success;
					background-color: #d7ffe1;
				}
			}
			&-area{
				position: absolute;
				width: 100%;
				height: 100%;
				left: 0;
				top: 0;
				.tips{
					font-size: 24rpx;
					color: #999;
					position: absolute;
					left: 50%;
					top: 50%;
					transform: translate(-50%,-50%);
				}
			}
			&-view{
				width: 80rpx;
				height: 80rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				border: solid 2rpx #EEE;
				background-color: #FFF;
				top: -2rpx;
				left: 0;
				&.active{
					background-color: $uni-color-primary;
					border-color: $uni-color-primary;
					color: #FFF;
				}
				&.fail{
					background-color: $uni-color-error;
					border-color: $uni-color-error;
				}
				&.success{
					border-color: $uni-color-success;
					background-color: #00a029;
				}
			}
		}
	}
</style>
