<template>
	<view class="uni-popup-share">
		<view class="uni-share-title">
			<text class="uni-share-title-text">{{ title }}</text>
		</view>
		<view class="uni-share-content">
			<view class="uni-share-content-box" style="margin:20px auto;"><uni-rate v-model="rateValue" allowHalf="true" @change="onChange" /></view>
		</view>
		<view class="uni-share-button-box">
			<button class="uni-share-button" @click="close" style="margin-right: 50px;">取消</button>
			<button class="uni-share-button" type="primary" @click="confirm">确认</button>
		</view>
	</view>
</template>

<script>
export default {
	name: 'UniPopupShare',
	props: {
		title: {
			type: String,
			default: '给我打分'
		}
	},
	inject: ['popup'],
	data() {
		return {
			rateValue: 5
		};
	},
	created() {},
	methods: {
		/**
		 * 关闭窗口
		 */
		close() {
			this.popup.close();
		},
		confirm() {
			this.popup.close();
			uni.showToast({
				title: '蟹蟹评价~',
				duration: 2000
			});
		},
		onChange(e) {
			console.log('rate发生改变:' + JSON.stringify(e));
		}
	}
};
</script>
<style scoped>
.uni-popup-share {
	background-color: #fff;
}

.uni-share-title {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	align-items: center;
	justify-content: center;
	height: 40px;
}

.uni-share-title-text {
	font-size: 14px;
	color: #666;
}

.uni-share-content {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	justify-content: center;
	padding-top: 10px;
}

.uni-share-content-box {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	flex-wrap: wrap;
	width: 360px;
}

.uni-share-content-item {
	width: 90px;
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: column;
	justify-content: center;
	padding: 10px 0;
	align-items: center;
}

.uni-share-content-item:active {
	background-color: #f5f5f5;
}

.uni-share-image {
	width: 30px;
	height: 30px;
}

.uni-share-text {
	margin-top: 10px;
	font-size: 14px;
	color: #3b4144;
}

.uni-share-button-box {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	padding: 10px 15px;
}

.uni-share-button {
	flex: 1;
	border-radius: 50px;
	color: black;
	font-size: 18px;
}

.uni-share-button::after {
	border-radius: 50px;
}
</style>
