<template>
	<view>
		<!-- {{ messageType }} -->
		<!-- 没有消息提示 -->
		<view v-if="messageList == null" class="" style="margin: 300rpx auto;text-align: center;">
			<u-image src="../../pic/nomessage.jpg" style="margin: 15rpx auto;" width="200rpx" height="200rpx" shape="circle"></u-image>
			<text style="font-size: 30rpx;color: grey;">您还没收到任何消息哦~</text>
		</view>
		<!-- 消息列表 -->
		<u-cell-group>
			<u-cell-item v-for="item in messageList" :key="item.id" :arrow="true" :center="true">
				<u-row gutter="16" justify="start">
					<u-col @click="toDetail(item)">
						<view class="">
							<text :class="{isview:item.isView=='1',notview:item.isView=='0'}">{{ item.senderName }} 回复了你的讨论</text>
						</view>
					</u-col>
				</u-row>
				<u-row gutter="16" justify="start">
					<u-col @tap="toDetail(item)">
						<view class="" style="">
							<text style="color: grey;font-size: 30rpx;background-color: #f5f5f5;padding: 5rpx 15rpx;border-radius: 10rpx;">{{ item.content }}</text>
						</view>
					</u-col>
				</u-row>
				<u-row gutter="16" justify="between">
					<u-col span="6" @tap="toDetail(item)">
						<view class="">
							<text style="font-size: 25rpx">{{ item.date }}</text>
						</view>
					</u-col>
				</u-row>
			</u-cell-item>
		</u-cell-group>
		<!-- toast -->
		<view><u-toast ref="uToast" /></view>
	</view>
</template>

<script>
import { getMessage, isViewTransfer } from '@/api/modules/message';

export default {
	name: 'MessageList',
	props: {
		messageType: {
			type: String,
			default: ''
		},
		userId: {
			type: String,
			default: ''
		},
		messageList:{
			type:Array,
			default:null
		}
	},
	data() {
		return {
			user: null,
			// messageList: []
		};
	},
	mounted() {
		//获得user
		let that = this;
		uni.getStorage({
			key: 'user',
			success: function(res) {
				if (res.data == null) {
				} else {
					that.user = res.data;
				}
			}
		});
		//获得消息列表
		// this.getMessage1();
	},
	onShow() {},
	methods: {
		// getMessage1() {
		// 	// console.log(this.user)
		// 	getMessage({ userId: this.user.id, messageType: this.messageType })
		// 		.then(resp => {
		// 			if (resp.data.statusCode == '200') {
		// 				this.messageList = resp.data.data;
		// 			}
		// 		})
		// 		.catch(data => {
		// 			this.toast('获取消息列表失败', 'fail', 'center');
		// 		});
		// },
		toDetail(item) {
			console.log(item);
			//是否已读切换
			isViewTransfer({ messageId: item.id })
				.then(resp => {})
				.catch(data => {
					this.toast(data, 'fail', 'center');
				});
			//点击跳转到父讨论的详情页
			this.navigator('../talkings/talking-detail?talkingId=' + item.talkingIdParent);
		}
	}
};
</script>

<style>
.isview {
	color: #bbbdc1;
}

.notview {
	color: #1b3767;
}
</style>
