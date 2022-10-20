<template>
	<view>
		<u-collapse>
			<u-collapse-item :title="item.chapter" v-for="item in chapterList" :key="item.id">
				<u-cell-group>
					<u-cell-item 
					@click="changeCell(item)" 
					icon="play-circle" 
					icon-size="45" 
					:title="item.chapter" 
					:arrow="false" 
					:index="item.id"
					:title-style="{ 'font-size': '32rpx' }"
					:class="{native:item.id==currentCell}">
					</u-cell-item>
				</u-cell-group>
				<!-- slot -->
				<view class="" slot="title-all" style="font-size: 35rpx;">
					<text style="margin-left: 20rpx;">{{item.chapter}}</text>
					<i style="margin-left: 500rpx;" class="iconfont catalogue"/>
				</view>
			</u-collapse-item>
		</u-collapse>
	</view>
</template>

<script>
import { getChapterByCourseId } from '@/api/modules/chapter';

export default {
		
	props: ["courseID"],

	data() {
		return {
			itemList: [
				{
					head: '目录',
					body: '只要我们正确择取一个合适的参照物乃至稍降一格去看待他人，值得赏识的东西便会扑面而来',
					open: true,
					disabled: true
				}
			],
			chapterList: [],
			currentCell: 0
		};
	},
	mounted() {
		
		this.getChapterByCourseId1();
	},
	methods: {
		getChapterByCourseId1() {
			
			getChapterByCourseId({ courseId: this.courseID })
				.then(resp => {
					
					if (resp.data.statusCode == '200') {
						console.log(resp);
						this.chapterList = resp.data.data;
						console.log(this.chapterList);
					}
				})
				.catch(data => {
					
					this.toast('获取章节列表失败', 'fail', 'center');
				});
		},
		changeCell(item) {
			 console.log(item);
			let data = item.etc.video_link
			console.log(item.etc.video_link);
			uni.navigateTo({
				url: "../../pages/video/video?SRC="+data
			})
			
		}
	}
};
</script>

<style>
.native {
	color:#23806d
}
</style>
