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
		props: ['category'],
		data() {
			return {
			
				bookData: [],
			}
		},
		
		mounted() {
			
		
		this.bookData = this.category
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
