<template>
	<view>
		<!-- 顶栏 -->
		<view class="top">
			<u-navbar height="44" :is-back="true" back-icon-color="black" title="消息中心" title-color="black" :background="barBackground"></u-navbar>
		</view>
		<!-- 分段器（选项卡） -->
		<view class="sub-section" style="background-color: white;width: 100%;">
			<u-subsection style="margin:20rpx 0 20rpx 0;width: 80%;left: 10%;right: 10%;" :list="list" :current="0" mode="subsection" active-color="#0086e7" @change="sectionChange"></u-subsection>
		</view>
		<!-- 内容 -->
		<scroll-view style="margin-top:130rpx;">
			<message-list ref="MessageList" :messageList="talkingList" :userId="userId" messageType="course" v-if="curNow==0"></message-list>
			<message-list ref="MessageList" :messageList="courseList" :userId="userId" messageType="talking" v-if="curNow==1"></message-list>
		</scroll-view>
		<!-- toast -->
		<view><u-toast ref="uToast" /></view>
	</view>
</template>

<script>
import MessageList from "@/pages/message/message-list.vue"
import { getMessage, isViewTransfer } from '@/api/modules/message';

export default {
	name: 'messageMain',
	components: {
		MessageList
	},
	data() {
		return {
			userId:"",
			talkingList:[],
			courseList:[],
			barBackground: {
				// backgroundColor: '#001f3f'
				// 渐变色
				backgroundImage: 'linear-gradient(45deg, #ffffff, #ffffff)'
			},
			list: [
				{
					name: '讨论区'
				},
				{
					name: '课程'
				}
			],
			curNow: 0
		};
	},
	mounted(){
		let that = this;
		uni.getStorage({
			key: 'user',
			success: function(res) {
				if (res.data == null) {
				} else {
					that.userId = res.data.id;
				}
			}
		});
		//获得消息列表
		this.getMessage1();
	},
	onShow() {
		this.getMessage1()
	},
	methods: {
		sectionChange(index) {
			this.curNow = index;
			//分段器切换的时候重新获取消息列表
			this.getMessage1()
		},
		getMessage1() {
			// console.log("调用父组件方法")
			getMessage({ userId: this.userId, messageType: "talking" })
				.then(resp => {
					if (resp.data.statusCode == '200') {
						this.talkingList = resp.data.data;
					}
				})
				.catch(data => {
					this.toast('获取消息列表失败', 'fail', 'center');
				});
			getMessage({ userId: this.userId, messageType: "course" })
				.then(resp => {
					if (resp.data.statusCode == '200') {
						this.courseList = resp.data.data;
					}
				})
				.catch(data => {
					this.toast('获取消息列表失败', 'fail', 'center');
				});
		},
	}
};
</script>

<style>
.sub-section {
	z-index: 30;
	position: fixed;
}
</style>
