<template>
	<view class="">
		<!-- 顶栏 -->
		<u-navbar height=44 :is-back="true" back-icon-color="white" title="讨论详情" title-color="white" :background="barBackground"></u-navbar>
		<!-- 列表 -->
		<scroll-view scroll-y="true">
			<view class="parent-talking"><talking-list isParentTalking="true" :talkingList="talkingList" importComponentName="TalkingDetail"></talking-list></view>
			<view class="son-talking">
				<scroll-view scroll-y="true"><talking-list :talkingList="childList" importComponentName="TalkingDetail"></talking-list></scroll-view>
			</view>
		</scroll-view>
		<!-- 提示回复操作 -->
		<u-button class="reply-tip" size="medium" shape="circle">提示：点击任一条讨论进行回复~</u-button>
	</view>
</template>

<script>
import { getTalkingsByTalkingId } from '@/api/modules/talkings';
import talkingList from '@/pages/talkings/talking-list.vue';
export default {
	name: 'TalkingDetail',
	components: {
		talkingList
	},
	data() {
		return {
			talkingId: '',
			talkingList: [],
			childList: [],
			barBackground: {
				// backgroundColor: '#001f3f'
				// 渐变色
				backgroundImage: 'linear-gradient(45deg, #22a54c, #244f98)'
			}
		};
	},
	onLoad: function(option) {
		this.talkingId = option.talkingId;
	},
	onShow() {
		//根据讨论id获取讨论列表（本身的和子列表）
		// console.log(this.talkingId);
		this.getTalkingsByTalkingId1(this.talkingId);
	},
	methods: {
		getTalkingsByTalkingId1(talkingId) {
			getTalkingsByTalkingId({ talkingId: talkingId })
				.then(resp => {
					if (resp.data.statusCode == '200') {
						this.talkingList = resp.data.data;
						this.childList = resp.data.data[0].replys;
					}
				})
				.catch(data => {
					this.toast('获取评论列表失败', 'fail', 'center');
				});
		}
	}
};
</script>

<style>
.reply-tip {
	z-index: 30;
	position: fixed;
	right: 5%;
	bottom: 2%;
	background-color: #1b3767;
	font-size: 35rpx;
	color: white;
}
</style>
