<template>
	<view>
		<!-- 顶栏 -->
		<u-navbar height="44" :is-back="true" back-icon-color="white" title="回复讨论" title-color="white" :background="barBackground">
			<view slot="right" @tap="replyHandler(replyItem)" style="color: white;font-size: 35rpx;margin-right: 20rpx;">发送</view>
		</u-navbar>
		<!-- 显示回复的讨论内容的通知栏 -->
		<uni-notice-bar :text="replyItem.talkingContent"></uni-notice-bar>
		<!-- input框 -->
		<view class="">
			<u-row gutter="16" justify="center">
				<u-col style="height: 100%;"><u-input v-model="replyTxt" type="textarea" height="1000" :placeholder="replyUser" /></u-col>
			</u-row>
		</view>
		<!-- toast -->
		<u-toast ref="uToast" />
	</view>
</template>

<script>
import { replyTalking } from '@/api/modules/talkings';
import uniNoticeBar from '@/components/uni-notice-bar/uni-notice-bar.vue';
import talkingList from '@/pages/talkings/talking-list.vue';

export default {
	name: 'reply-talking',
	components: { uniNoticeBar },
	data() {
		return {
			user:"",
			replyTxt: '',
			replyUser: '',
			replyItem: null,
			barBackground: {
				// backgroundColor: '#001f3f'
				// 渐变色
				backgroundImage: 'linear-gradient(45deg, #22a54c, #244f98)'
			}
		};
	},
	onLoad: function(option) {
		this.replyItem = option.replyItem;
		this.replyItem = JSON.parse(this.replyItem);
		// console.log(this.replyItem)
		this.replyUser = '回复' + this.replyItem.username;
	},
	onShow() {
		let that = this;
		uni.getStorage({
			key: "user",
			success: function(res) {
				if (res.data == null) {
				} else {
					that.user = res.data
				}
			}
		})
	},
	methods: {
		reBack() {
			uni.navigateBack({
				delta: 1
			});
		},
		//提交回复评论
		replyHandler(item) {
			console.log(item)
			if (this.replyTxt != '') {
				if (item.talkingIdParent == undefined) {
					//回复自己
					var talkingIdParent = item.id;
				} else {
					var talkingIdParent = item.talkingIdParent;
				}
				replyTalking({
					userId: this.user.id,
					replyUserId: item.replyUserId, //回复的用户id，即将变成被回复的用户id
					talkingIdParent: talkingIdParent,
					replyTxt: this.replyTxt
				})
					.then(resp => {
						// console.log(resp);
						if (resp.data.statusCode == '200') {
							this.toast(resp.data.message,'success', 'center')
							this.replyTxt = '', //清空
							this.goBack();
						}
					})
					.catch(data => {
						this.toast(data, 2);
					});
			}
		}
	}
};
</script>

<style>
.top-bar {
	width: 100%;
	padding-bottom: 20rpx;
	border-bottom: solid 0.1rpx #dadada;
}
</style>
