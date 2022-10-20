<template>
	<view>
		<!-- 讨论列表 -->
		<scroll-view scroll-y="true" >
			<talking-list :talkingList="talkingList" importComponentName="talkingMain"></talking-list>
		</scroll-view>		
		<!-- 发表评论按钮 -->
		<view class="" v-if="notShowRptBtn=='false'">
			<u-icon class="report-btn" name="edit-pen-fill" @click="toReport" size="100" color="#1b3767" ></u-icon>
		</view>
		<!-- toast -->
		<view><u-toast ref="uToast" /></view>
	</view>
</template>

<script>
import { reportTalking, getTalkings, replyTalking, deleteTalking } from '@/api/modules/talkings';
import talkingList from '@/pages/talkings/talking-list.vue';

export default {
	name:"talkingMain",
	components: {
		talkingList
	},
	props:{
		notShowRptBtn:{
			type:String,
			default:"false"
		},
		courseId:{
			type:String,
			default:""
		},
		courseName:{
			type:String,
			default:""
		}
	},
	data() {
		return {
			talkingList: []
		};
	},
	mounted() {
		//获得讨论列表
		this.getTalkings1(this.courseId);
	},
	onShow() {
		//获得讨论列表
		this.getTalkings1(this.courseId);
	},
	methods: {
		//获得讨论列表
		getTalkings1(courseId) {
			getTalkings({ courseId: courseId })
				.then(resp => {
					if (resp.data.statusCode == '200') {
						this.talkingList = resp.data.data;
					}
				})
				.catch(data => {
					this.toast('获取评论列表失败', 'fail', 'center');
				});
		},
		toReport() {
			// 当前路径是引用talking-main的路径 也就是 video.vue
			this.navigator('../talkings/report-talking?courseName='+this.courseName+'&courseId='+this.courseId);
		}
	}
};
</script>

<style>
.single-talking {
	border: solid red 1rpx;
}

.report-btn {
	z-index: 30;
	position: fixed;
	right: 10%;
	bottom: 15%;
}

.report-btn:hover {
	cursor: pointer;
}
</style>
