
<!-- 横向选项卡（水平滚动导航栏） -->
<template>
	<view>
		<!-- 顶部导航栏 -->
		<view class="horizonal-tab">
			<scroll-view scroll-x="true" scroll-with-animation class="scroll-tab">
				<block v-for="(item,index) in tabBars" :key="index">
					<view class="scroll-tab-item" :class="{'active': tabIndex==index}" 
					@tap="toggleTab(index)">
						{{item.name}}
						<view class="scroll-tab-line"></view>
					</view>
				</block>
			</scroll-view>
		</view>
		
		<!-- 内容区 -->
		<view class="content">
			<!-- 滑块视图 -->
			<swiper :current="tabIndex" @change="tabChange"><!-- current:当前所在滑块的index -->
				<swiper-item v-for="(content,index) in contentList" :key="index">
					<view class="content">{{content}}</view>
				</swiper-item>
			</swiper>
		</view>
		
		
	</view>
</template>
 
<script>
	export default {
		data () {
			return {
				tabIndex: 0, /* 选中标签栏的序列,默认显示第一个 */
				contentList: [
					"关注",
					"推荐",
					"热点",
					"体育",
					'财经',
					'娱乐',
				],
				tabBars:[
					{
						name: '关注',
						id: 'guanzhu'
					},
					{
						name: '推荐',
						id: 'tuijian'
					},
					{
						name: '热点',
						id: 'redian'
					},
					{
						name: '体育',
						id: 'tiyu'
					},
					{
						name: '财经',
						id: 'caijing'
					},
					{
						name: '娱乐',
						id: 'yule'
					}
				]
			}
		},
		methods:{
			//切换选项卡
			toggleTab (index) { 
				this.tabIndex=index;
			},
			//滑动切换swiper
			tabChange (e) { 
				console.log(e);
				this.tabIndex=e.detail.current;
			}
		}
	}
	
</script>
 
<style>
	.horizonal-tab{
		
	}
	.horizonal-tab .active{
		color: red;
	}
	.scroll-tab{
		white-space: nowrap; /* 必要，导航栏才能横向*/
		border-bottom: 1rpx solid #eee;
		text-align: center;
	}
	.scroll-tab-item{
		display: inline-block; /* 必要，导航栏才能横向*/
		margin: 20rpx 30rpx 0 30rpx;
	}
	.active .scroll-tab-line{
		border-bottom: 5rpx solid red;
		border-top: 5rpx solid red;
		border-radius: 20rpx;
		width: 70rpx;
		
	}
</style>
