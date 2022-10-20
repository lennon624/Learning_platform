<template>
	<view>
		<u-navbar back-text="返回" ></u-navbar>
		<view class="input-btn">
			<view class="input-box">
				<view class="input-left">
					<input class="inputStyle" maxlength="150" confirm-type="go" v-model="searchcourse" ref='searchref' placeholder="请输入搜索内容" />
				</view>
				<view class="input-right"  @click="search()">
					<i class="iconfont search" />
				</view>
			</view>
		</view>
		<view class="recommend">
			<p class="head">搜索历史
				<span class="">
					<i class="iconfont delete">清空</i>
				</span>
			</p>
			<view class="label-box">
				<p class="label" v-for="(labelData,labelListKey) in labelList" :key="labelListKey">
					{{labelData.text}}
				</p>
			</view>
		</view>
		<view class="recommend">
			<p class="head">大家都在搜</p>
			<!-- <view class="label-list">
				<view class="list-left" v-for="(labelList,listKey) in listData" :key="listKey">
					<view class="list-ul">
						<view class="chunk" :class="{'chunk-one':listKey == 0,'chunk-tow':listKey == 1,'chunk-three':listKey == 2}">{{listKey + 1}}</view>
						{{labelList.text}}
					</view>
				</view>
			</view> -->
		</view>
	</view>
</template>
<script>
	import { getCourseByCategory ,getpcategorybycourse,getCourseByName} from "@/api/modules/course";
	export default {
		data() {
			return {
				searchText: '',
				inputOff: '',
				searchcourse:'',
				Scourses:'',
				labelList: [{
						text: '修仙',
					},
					{
						text: '你是一本书',
					},
				],
				listData: [{
						text: '都市极品医神',
					},
					{
						text: '最强兵王',
					},
					{
						text: '斗罗大陆',
					},
					{
						text: '元尊',
					},
					{
						text: '七界传说',
					},
					{
						text: '绝世武神',
					},
					{
						text: '终极小医仙',
					},
					{
						text: '纵横天下',
					},
					{
						text: '庆余年',
					},
					{
						text: '魔帝',
					},
				]
			}
		},
		methods: {
			//通过课程名查询课程
			    search(){
			       getCourseByName({course_name : this.searchcourse})
			        .then(res => {
			          this.Scourses = res.data.data;
			         uni.setStorage({
			             key: 'Scourse',
			             data:  res.data.data,
			             success: function () {
			                uni.navigateTo({
			                	url: "../searchcourse/searchcourse"
			                })
			             }
			         });
			        
			        })
			        .catch(err => {
			          this.toast(err, "error");
			        });
			    },
			searchBtn() {
				this.$refs.searchref.focus();
			}
		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.inputOff = option.id
		},

	}
</script>

<style scoped lang="scss">
	.input-btn {
		padding: 40upx $bleed 0 $bleed;

		.input-box {
			height: 104upx;
			padding: 0 10upx 0 30upx;
			border-radius: 8upx;
			background-color: $lightBlue;

			.input-left {
				float: left;
				width: calc(100% - 60upx);
				padding: 16upx 0;

				.inputStyle {
					height: 74upx;
					color: $dominantHue;
					font-size: 28upx;
				}
			}

			.input-right {
				float: right;
				width: 60upx;
				text-align: center;
				line-height: 104upx;

				.search {
					font-size: 32upx;
					font-weight: bold;
					color: $dominantHue;
				}
			}
		}
	}

	.recommend {
		padding: 60upx $bleed 10upx $bleed;

		.head {
			font-size: 32upx;
			line-height: 36upx;
			font-weight: bold;

			.delete {
				font-size: 26upx;
				line-height: 36upx;
				font-weight: 400;
				color: $mediumGrey;
				float: right;
			}
		}

		.label-list {
			padding-top: 40upx;
			overflow: auto;

			.list-left {
				width: 50%;
				float: left;
				margin-bottom: 40upx;
			}

			.list-ul {
				height: 34upx;
				line-height: 34upx;
				font-size: 26upx;

				.chunk {
					display: inline-block;
					height: 34upx;
					line-height: 34upx;
					width: 38upx;
					font-size: 24upx;
					text-align: center;
					background-color: $lightGray;
					color: #fff;
					border-radius: 8upx;
					margin-right: 18upx;
				}

				.chunk-one {
					background-color: rgba(0, 134, 231, 1);
				}

				.chunk-tow {
					background-color: rgba(0, 134, 231, 0.8);
				}

				.chunk-three {
					background-color: rgba(0, 134, 231, 0.6);
				}

			}

		}

		.label-box {
			padding: 40upx 0 0upx 0;

			.label {
				height: 56upx;
				line-height: 56upx;
				text-align: center;
				padding: 0 30upx;
				font-size: 26upx;
				border-radius: 56upx;
				background-color: #F4F4F4;
				display: inline-block;
				margin-right: 20upx;
			}
		}
	}
</style>
