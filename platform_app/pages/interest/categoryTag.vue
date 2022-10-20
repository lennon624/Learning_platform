<template>
	<!-- 标签选项 -->
	<!-- <view style="margin-top: 500rpx; margin-left: 5%; margin-right: 5%;"> -->
		<view style="float: left; margin: 10rpx;">
			<u-tag :text="category.categoryName" shape="circle" :type="type" @click="clickCategoryTag(category)" />
		</view>
	<!-- </view> -->
</template>

<script>
	export default {
		props: ["category"],
		data() {
			return {
				type: "primary"
			}
		},
		
		mounted() {
			this.type = this.category.etc ? "success" : "primary"
		},
		
		methods: {
			clickCategoryTag(category) {
				// console.log("点击了" + category.categoryName)
				this.type = this.type == "primary" ? "success" : "primary";
				if(this.type == "success") {
					// 选中
					this.$emit("increase", category)
				} else {
					// 取消选中
					this.$emit("reduce", category)
				}
			}
		}
	}
</script>

<style>
</style>
