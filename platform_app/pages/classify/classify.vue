<template>
	<view>
		<view class="classify-top">
			<u-tabs-swiper ref="uTabs" bar-width="40" font-size="32" bar-height="4" :bold="true" :list="toplist" inactive-color="#666777"
			 name="categoryName" :current="current" :is-scroll="false" wiperWidth="750" @change="tabsChange"></u-tabs-swiper>
		</view>
		<view class="subject">
			<swiper style="height: 100%;width: 100%;" class="swiper-box" :current="swiperCurrent" @transition="transition"
			 @animationfinish="animationfinish">
				<swiper-item class="swiper-home" v-for="(item,index) in toplist">
					<classifyTabOne  :category="item.etc.children"/>
				</swiper-item>
				<!-- <swiper-item class="swiper-home">
					<classifyTabTow />
				</swiper-item>
				<swiper-item class="swiper-home">
					<classifyTabThree />
				</swiper-item>
				<swiper-item class="swiper-home">
					<classifyTabFour />
				</swiper-item> -->
			</swiper>
		</view>


	</view>
</template>
<script>
	import {
		getparentcategory
	} from "@/api/modules/category";
	import classifyTabOne from '../classifyTabOne/classifyTabOne.vue'
	import classifyTabTow from '../classifyTabTow/classifyTabTow.vue'
	import classifyTabThree from '../classifyTabThree/classifyTabThree.vue'
	import classifyTabFour from '../classifyTabFour/classifyTabFour.vue'
	export default {
		components: {
			classifyTabOne,
			classifyTabTow,
			classifyTabThree,
			classifyTabFour
		},
		data() {
			return {
				// 因为内部的滑动机制限制，请将tabs组件和swiper组件的current用不同变量赋值
				current: 0, // tabs组件的current值，表示当前活动的tab选项
				swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
				toplist: [],
			}
		},
		
		mounted() {
		    this.getcategory();
		  },
		methods: {
			//查询父类别
			getcategory() {
				getparentcategory()
					.then(res => {
						this.toplist = res.data.data;
						console.log(this.toplist)
					})
					.catch(err => {
						this.toast(err, 2);
					});
			},
			// tabs通知swiper切换
			tabsChange(index) {
				this.swiperCurrent = index;
				// console.log('你点了第' + index + '个')
			},
			// swiper-item左右移动，通知tabs的滑块跟随移动
			transition(e) {
				let dx = e.detail.dx;
				this.$refs.uTabs.setDx(dx);

			},
			// 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
			// swiper滑动结束，分别设置tabs和swiper的状态
			animationfinish(e) {
				let current = e.detail.current;
				this.$refs.uTabs.setFinishCurrent(current);
				this.swiperCurrent = current;
				this.current = current;
			},
			// scroll-view到底部加载更多
			onreachBottom() {

			},
		}
	}
</script>

<style scoped lang="scss">
	.subject {
		position: absolute;
		top: $classifyTopH;
		left: 0;
		right: 0;
		bottom: 0;
		overflow-y: auto;
	}

	.subject::-webkit-scrollbar {
		display: none !important;
	}

	.classify-top {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: $classifyTopH;
		border-bottom: 1px solid $rippleBg;
		padding: 0 $bleed;
		background-color: #fff;
		z-index: 10;
	}
</style>
