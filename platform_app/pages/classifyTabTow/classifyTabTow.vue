<template>
	<view>
		<classifylist :bookData="bookData" @listTabBtn="TabBtn" @labelSortBtn="SortBtn" @classLabelBtn="classBtn" @booksDataBtn="booksBtn"/>
	</view>
</template>

<script>
	import classifylist from '../../components/classifylist/classifylist.vue'

	export default {
		components: {
			classifylist
		},
		data() {
			return {
				bookData: [{ //左边选项卡标签
						list: '小说',
						amount: '1432',
						sortList: ['最热', '最新', '最火'],
						classList: ['全部', '散文随笔', '绘本画册', '纪实文学', '文学理论', '世界名著'],
						books: [{
								url: "https://s1.ax1x.com/2020/08/12/ajvTaD.png",
								head: "只有医生知道",
								text: "开创西医写作的新风格，寓知识于故事，使科普不再乏味。",
								author: "张羽",
								id: "1"
							},
						]
					},
					{
						list: '文学',
						amount: '1489',
						sortList: ['最热', '最新', '最火'],
						classList: ['全部', '职场心理', '面试心理', '谈话心理'],
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajjt6x.png",
							head: "当呼吸化为空气",
							text: "全世界只有 0.12% 的人在 36 岁就患上肺癌，保罗·卡拉尼什（Paul Kalanithi）是其中之一。当你读到这本书时，他已经不在人世。",
							author: "保罗·卡拉尼什",
							id: "2"
						}, ]
					},
					{
						list: '历史',
						amount: '6798',
						sortList: ['最热', '最新', '最火'],
						classList: ['全部', '文学艺术', '行为艺术', '话术艺术'],
						books: [{
							url: "https://s1.ax1x.com/2020/08/12/ajzdBV.png",
							head: "30 岁前的每一天：超实用梦想管理指南（新版）",
							text: "在 30 岁以后，过上你想要的生活，或许你还可以影响别人，改变世界。",
							author: "水湄物语",
							id: "8"
						}, ]
					},
				],
			}
		},
		methods:{
			TabBtn(data){ 
				// 组件左边选项卡切换按钮
				console.log('你点击了' + data + '分类')
			},
			SortBtn(data){
				// 组件排序标签按钮
				console.log('你点击了' + data + '分类')
			},
			classBtn(data){
				// 组件分类标签按钮
				console.log('你点击了' + data + '分类')
			},
			booksBtn(data){
				// 组件图书列表按钮
				console.log('你点击了ID为' + data.id + '的图书')
			}
		}
	}
</script>

<style>
</style>
