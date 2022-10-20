<template>
	<view class="container">
		<!-- 没有消息提示 -->
		<view v-if="talkingList == null" class="" style="margin: 150rpx auto;text-align: center;">
			<u-image src="../../pic/nomessage.jpg" style="margin: 15rpx auto;" width="200rpx" height="200rpx" shape="circle"></u-image>
			<text style="font-size: 30rpx;color: grey;">当前还没有讨论哦~赶紧发起第一条讨论吧！</text>
		</view>
		<u-cell-group>
			<u-cell-item v-for="item in talkingList" :key="item.id" :arrow="false" :bg-color="isParentTalking == 'true' ? '#f3f3f3' : 'white'">
				<u-row gutter="16" justify="space-between">
					<u-col span="8" @click="toDetailOrReply(item)" style="">
						<view class="">
							<text v-if="isParentTalking == 'false' && importComponentName == 'TalkingDetail'" style="color: #1b3767;">
								{{ item.username }} @ {{ item.username1 }}
							</text>
							<text v-else style="color: #1b3767;">{{ item.username }}</text>
						</view>
					</u-col>
					<u-col span="3" v-if="talkingList.indexOf(item)==0 && importComponentName == 'TalkingDetail' && item.isRootTalking != '1'" style="text-align: right;">
						<text class="newly-reply">最新回复</text>
					</u-col>
				</u-row>
				<u-row gutter="16" justify="start">
					<u-col @click="toDetailOrReply(item)">
						<view class="">
							<text style="color: grey;font-size: 30rpx">{{ item.talkingContent }}</text>
						</view>
					</u-col>
				</u-row>
				<u-row gutter="16" justify="between">
					<u-col span="6" @click="toDetailOrReply(item)">
						<view class="">
							<text style="font-size: 25rpx">{{ item.date }}</text>
						</view>
					</u-col>
					<u-col span="6" @click="toDetailOrReply(item)" v-if="isParentTalking == 'true' || importComponentName != 'TalkingDetail'">
						<view class="" style="text-align: right;">
							<u-icon name="chat" style="margin-right: 5rpx;"></u-icon>
							<text>{{ item.replyCnt }}</text>
						</view>
					</u-col>
				</u-row>
			</u-cell-item>
		</u-cell-group>
		<!-- <view class="">{{ importComponentName }}</view> -->
	</view>
</template>

<script>
export default {
	name: 'TalkingList',
	props: {
		talkingList: {
			type: [Array, String],
			default: function() {
				return [];
			}
		},
		importComponentName: {
			type: String,
			default: ''
		},
		isParentTalking: {
			type: String,
			default: 'false'
		}
	},
	onLoad(){
	},
	onShow() {
	},
	data() {
		return {};
	},
	methods: {
		toDetailOrReply(item) {
			var talkingId = item.id; //父讨论id
			if (this.importComponentName == 'talkingMain') {
				// 当前路径是引用talking-main的路径 也就是 video.vue
				this.navigator('../talkings/talking-detail?talkingId=' + talkingId);
			} else if (this.importComponentName == 'TalkingDetail') {
				// this.navigator('./reply-talking?talkingContent=' + item.talkingContent +'&replyUser='+item.username);
				var replyItem = JSON.stringify(item);
				this.navigator('./reply-talking?replyItem=' + replyItem);
			}
		}
	}
};
</script>

<style>
.newly-reply {
	padding: 5rpx;
	background-color: #246386;
	border-radius: 10rpx;
	color: white;
}
</style>
