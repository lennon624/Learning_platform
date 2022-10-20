<template>
	<view class="main">
<view class="head">
	<text class="headleft">我的课程</text>
	<u-icon name="star" class="headright"></u-icon>
</view>
<view class="body">
	<u-subsection :list="list" :current="0" bg-color="#001934" @change="sectionChange"></u-subsection>
	</view>
	
	
	<view class="bodycard">
		<u-card >
				<view class="" slot="body">	
					<view class="u-body-item u-flex u-row-between u-p-b-0">			
						<image src="../../pic/dasi.jpg" mode="aspectFill"></image>
						<view class="u-body-item-title u-line-2" >三角形打野教学</view>		
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="play-right-fill" size="34" color="" label="继续进行学习"></u-icon>
				<text style="padding-left: 280upx;">60/100</text>
				<u-icon name="order" size="34" ></u-icon></view>
			</u-card>
			
			<u-card >
					<view class="" slot="body">	
						<view class="u-body-item u-flex u-row-between u-p-b-0">			
							<image src="../../pic/bingdu.jpg" mode="aspectFill"></image>
							<view class="u-body-item-title u-line-2">生物学</view>		
						</view>
					</view>
					<view class="" slot="foot"><u-icon name="play-right-fill" size="34" color="" label="继续进行学习"></u-icon>
					<text style="padding-left: 280upx;">60/100</text>
					<u-icon name="order" size="34" ></u-icon></view>
				</u-card>
				
				<u-card >
						<view class="" slot="body">	
							<view class="u-body-item u-flex u-row-between u-p-b-0">			
								<image src="../../pic/hangtian.jpg" mode="aspectFill"></image>
								<view class="u-body-item-title u-line-2">天体学</view>		
							</view>
						</view>
						
						<view class="" slot="foot">
							<u-icon name="play-right-fill" size="34" color="" label="继续进行学习"></u-icon>
							<text style="padding-left: 280upx;">60/100</text>
							<u-icon name="order" size="34" ></u-icon>
						</view>
						
					</u-card>
			
			
	</view>
	
	
	
	</view>
	
</template>

<script>
	

	export default {
		
		data() {
			return {
				list: [
									{
										name: '正在学'
									}, 
									{
										name: '已学完'
									}, 
									
								],
								current: 1,
								
			}
		},
		methods: {
			sectionChange(index){
				this.current=index;	
				
			}
		}
	}
</script>

<style lang="scss">
	.main{
		width: 375px;
		height: 667px;
		background: #001934;
		.head{
			width: 375px;
			height: 40px;
			background: #001934;
			.headleft{
				font-size: 20px;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				text-align: left;
				color: #f8f8f8;
				padding-left: 18upx;padding-top: 20upx;
			};
			.headright{
				font-size: 20px;
				font-family: Microsoft YaHei, Microsoft YaHei-Normal;
				font-weight: Normal;
				text-align: left;
				color: #f8f8f8;
				padding-left: 500upx;padding-top: 20upx;
			}
		}
		
		.body{
			
		},
		.bodycard{
			.u-card-wrap { 
					background-color: $u-bg-color;
					padding: 1px;
				}
				
				.u-body-item {
					font-size: 32rpx;
					color: #333;
					padding: 20rpx 10rpx;
				}
					
				.u-body-item image {
					width: 120rpx;
					flex: 0 0 120rpx;
					height: 120rpx;
					border-radius: 8rpx;
					margin-left: 12rpx;
				}
		}
	}

	
</style>