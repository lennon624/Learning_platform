<template>
    <view>
        <view class="uni-padding-wrap uni-common-mt">
            <view class="video_body">
                <video style="width: 100%; " id="myVideo" src="https://video.pearvideo.com/mp4/adshort/20200909/cont-1696216-15372651_adpkg-ad_hd.mp4"
                   title="芜湖"  @error="videoErrorCallback" :danmu-list="danmuList" enable-danmu danmu-btn controls></video>
            </view>
           
        </view>
		
		<view>
			<!-- 顶部导航栏 -->
			<view class="horizonal">
				
			
			<view class="horizonal-tab">
				<scroll-view scroll-x="true" scroll-with-animation class="scroll-tab">
					<block v-for="(item,index) in tabBars" :key="index">
						<view class="scroll-tab-item" :class="{'active': tabIndex==index}" 
						@tap="toggleTab(index)">
							{{item.name}}
							<view class="scroll-tab-line"></view>
						</view>
						
						
					</block>
					
					
				</scroll-view>
			</view>
			<u-input v-model="inputValue" @blur="bindInputBlur" type="text"  style="width: 200rpx;  float: left; height: 60rpx;  margin-top: 5rpx; border-radius：30px;" confirm-type="send" placeholder="发送弹幕" border="true"  auto-height="false"/>
			 <u-button @tap="bindSendDanmu" formType="submit" size="mini " type="primary" style="width: 80rpx; margin-top: 10rpx; margin-left: 10rpx;">发送</u-button>
			</view>
			<!-- 内容区 -->
			<view class="content">
				<!-- 滑块视图 -->
				<swiper :current="tabIndex" @change="tabChange" style="height: 1000rpx;"><!-- current:当前所在滑块的index -->
					<swiper-item >
						<view  style="margin-left: 30rpx;">
	<view class="" >
		<u-image src="https://cdn.uviewui.com/uview/example/fade.jpg" shape="circle" style="margin: 30rpx 30rpx 0rpx 0rpx; float: left;" width="100rpx" height="100rpx"></u-image>
		<u-tag text="一丘之貉" mode="plain" style="margin-top: 50rpx;" />
		<u-button size="medium  " type="primary" style="width: 80rpx; margin: 50rpx 70rpx 0rpx 40rpx; float: right;"> asd</u-button>
	</view>
	<view class="" style="width: 100%; float: left; margin-top: 20rpx;">
		<u-collapse>
				<u-collapse-item :title="item.head" v-for="(item, index) in itemList" :key="index" :open="item.open" :disabled="item.disabled" style="width: 100%;">
					{{item.body}}
				</u-collapse-item>
			</u-collapse>
	</view>
	
		</view>
		
		
		
	<view class="" style="float: left; ">
		<u-card :title="title" :sub-title="subTitle" :thumb="thumb" padding="30">
				<view class="" slot="body">
					<view class="u-body-item u-flex u-border-bottom u-col-between u-p-t-0">
						<image src="https://img11.360buyimg.com/n7/jfs/t1/94448/29/2734/524808/5dd4cc16E990dfb6b/59c256f85a8c3757.jpg" mode="aspectFill"></image>
						<view class="u-body-item-title u-line-2">假装自己是推荐视频</view>
						
					</view>
					<view class="u-body-item u-flex u-border-bottom u-col-between u-p-t-0">
						<image src="https://img12.360buyimg.com/n7/jfs/t1/102191/19/9072/330688/5e0af7cfE17698872/c91c00d713bf729a.jpg" mode="aspectFill"></image>
						<view class="u-body-item-title u-line-2">釉色渲染仕女图韵味被私藏</view>
						
					</view>
				</view>
				<view class="" slot="foot"><u-icon name="chat-fill" size="34" color="" label="30评论"></u-icon></view>
			</u-card>
	</view>
							
							
					
					</swiper-item>
					<swiper-item >
						<view class="content">222</view>
					</swiper-item>
				</swiper>
			</view>
			
			
		</view>
		
    </view>
</template>

<script>
	export default {
	    data() {
	        return {
				
				itemList: [{
									head: "简介",
									body: "只要我们正确择取一个合适的参照物乃至稍降一格去看待他人，值得赏识的东西便会扑面而来",
									open: false,
									
								}],
				
				title: '素胚勾勒出青花，笔锋浓转淡',
							subTitle: '2020-05-15',
							thumb: 'http://pic2.sc.chinaz.com/Files/pic/pic9/202002/hpic2119_s.jpg',
				
				value:'',
	            src: '',
	            danmuList: [{
	                    text: '第 1s 出现的弹幕',
	                    color: '#ff0000',
	                    time: 1
	                },
	                {
	                    text: '第 3s 出现的弹幕',
	                    color: '#ff00ff',
	                    time: 3
	                }
	            ],
				inputValue:'',
	            danmuValue: '',
				tabIndex: 0, /* 选中标签栏的序列,默认显示第一个 */
				contentList: [
					"简介",
					"评论区",
					
				],
				tabBars:[
					{
						name: '简介',
						id: 'guanzhu'
					},
					{
						name: '评论区',
						id: 'tuijian'
					},
					
				]
	        }
	    },
	    onReady: function(res) {
	        // #ifndef MP-ALIPAY
	        this.videoContext = uni.createVideoContext('myVideo')
	        // #endif
	    },
	    methods: {
			
			 bindInputBlur: function (e) {
			            this.inputValue = e.target.value
					
			        },
			        bindButtonTap: function () {
			            var that = this
			            uni.chooseVideo({
			                sourceType: ['album', 'camera'],
			                maxDuration: 60,
			                camera: ['front', 'back'],
			                success: function (res) {
			                    this.src = res.tempFilePath
			                }
			            })
			        },
			        bindSendDanmu: function () {
			            this.videoContext.sendDanmu({
			                text: this.inputValue,
			                color: this.getRandomColor()
			            })
						this.inputValue=''
			        },
			        videoErrorCallback: function (e) {
			            console.log('视频错误信息:')
			            console.log(e.target.errMsg)
			        },
			        getRandomColor: function () {
			            const rgb = []
			            for (let i = 0; i < 3; ++i) {
			                let color = Math.floor(Math.random() * 256).toString(16)
			                color = color.length == 1 ? '0' + color : color
			                rgb.push(color)
			            }
			            return '#' + rgb.join('')
			        },
			
	        sendDanmu: function() {
	            this.videoContext.sendDanmu({
	                text: this.danmuValue,
	                color: this.getRandomColor()
	            });
	            this.danmuValue = '';
	        },
	        videoErrorCallback: function(e) {
	            uni.showModal({
	                content: e.target.errMsg,
	                showCancel: false
	            })
	        },
	        getRandomColor: function() {
	            const rgb = []
	            for (let i = 0; i < 3; ++i) {
	                let color = Math.floor(Math.random() * 256).toString(16)
	                color = color.length == 1 ? '0' + color : color
	                rgb.push(color)
	            }
	            return '#' + rgb.join('')
	        },
			
			//切换选项卡
			toggleTab (index) { 
				this.tabIndex=index;
			},
			//滑动切换swiper
			tabChange (e) { 
				console.log(e);
				this.tabIndex=e.detail.current;
			}
	    }
	}
</script>

<style>
	
.video_body{
	width: 100%;
}

.horizonal-tab{
		width: 60%;
	}
	.horizonal-tab .active{
		color: red;
	}
	.scroll-tab{
		float: left;
		white-space: nowrap; /* 必要，导航栏才能横向*/
		border-bottom: 1rpx solid #eee;
		text-align: center;
	}
	.scroll-tab-item{
		display: inline-block; /* 必要，导航栏才能横向*/
		margin: 20rpx 30rpx 0 30rpx;
	}
	.active .scroll-tab-line{
		border-bottom: 5rpx solid red;
		border-top: 5rpx solid red;
		border-radius: 20rpx;
		width: 70rpx;
		
	}
	
	.u-card-wrap { 
			background-color: $u-bg-color;
			padding: 1px;
		}
		
		.u-body-item {
			font-size: 32rpx;
			color: #333;
			padding: 20rpx 10rpx;
		}
			
		.u-body-item image {
			width: 120rpx;
			flex: 0 0 120rpx;
			height: 120rpx;
			border-radius: 8rpx;
			margin-left: 12rpx;
		}
</style>
