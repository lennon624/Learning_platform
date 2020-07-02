<template>
  <div class="video">
    <video ref="myvideo" @canplay="getTotal" @timeupdate="timeUpdate" @click="showControls">
      <source :src="src" />
    </video>
    <transition
      enter-active-class="animated fadeIn slow"
      leave-active-class="animated fadeOut slow"
    >
    
      <div class="controls" v-show="isShow">
        
      
         
        <div class="con_left">
          <!-- 播放与暂停 -->
          <span :class="{'icon-play3':!isPaused,'icon-pause2':isPaused}" @click="togglePlay"></span>
          <!-- 停止 -->
          <span class="icon-stop2" @click="stopPlay"></span>
          <!-- 播放时间/总时长 -->
          <span>{{currentTime}}/{{totalTime}}</span>
        </div>

         <!-- 播放条 -->
          <div class="controller__progress-wrapper">
            <div class="controller__progress" ref="p" @click="handleProgressClick($event)">
                <div class="controller__progress controller__progress--passed"
                        :style="{width: videoProgressPercent}"></div>
                <div class="controller__dot"
                        :style="{left: videoProgressPercent}"
                        @pointerdown="startDragging($event)">
                    <div class="controller__inner-dot"></div>
                </div>
            </div>
        </div>

        <div class="con_right">
          <!-- 声音 -->
          <span
            :class="{'icon-volume-low':!isMuted,'icon-volume-mute2':isMuted}"
            @click="toggleMuted"
          ></span>
          <!-- 全屏 -->
          <span class="icon-loop" @click="toggleFullScreen"></span>
        </div>
         
      </div>

    </transition>
  </div>
</template>

<script>
import "animate.css";
// 引入字体样式文件
import '@/styles/videostyle.css'
export default {
  // 定义props属性，用于接收父组件所传递过来的数据
  props: ["src"],
  data() {
    return {
      myvideo: null,
      // 标记当前的播放状态
      isPaused: false,
      // 标记当前视频是否是静音
      isMuted: false,
      // 当前播放时间
      currentTime: "00:00",
      // 总时长
      totalTime: "00:00",
      //拖动进度条
      isDragging: false,
      // 标记控制面板是否可见
      isShow: true,
      // startTime:开始时间,以毫秒做为单位
      startTime: 0,
      //播放条进度
      videoProgress: 0,
      
       draggingStartX: 0,
        dotOffsetX: 0,
        progress: null,
    };
  },

  mounted() {

        this.progress = this.$refs.p;
     console.log( 'progress'+this.progress)
    // 获取播放器元素
    this.myvideo = this.$refs.myvideo;                                         
    // 记住起始的时间,这个时间可以做为时间间隔的参照
    this.startTime = Date.now()
    // 开户一个定时器
    setInterval(() => {
        if(Date.now() - this.startTime > 5000){
            this.isShow = false
        }
    }, 1000);
  },
  computed: {
    videoProgressPercent() {
        return `${this.videoProgress * 100}%`;
    }
  },
  methods: {
    // 播放与暂停
    togglePlay() {
      // 在视频标签(video)原生方法中
      // play():让视频播放
      // pause():让视频暂停
      // 修改当前的播放状态
      this.isPaused = !this.isPaused;
      if (this.isPaused) {
        this.myvideo.play();
      } else {
        this.myvideo.pause();
      }
    },
    // 停止 播放
    stopPlay() {
      // 没有提供原生的stop方法
      // this.myvideo.stop()
      this.myvideo.pause(); 
      // currentTime这个属性就是用来标记当前视频播放到的时间，以秒做为单位
      this.myvideo.currentTime = 0;
      // 重置播放状态为初始值
      this.isPaused = false;
    },
    // 时间格式化处理
    timeFormat(time) {
      let minute = Math.floor((time % 3600) / 60);
      let second = Math.floor(time % 60);
      minute = minute < 10 ? "0" + minute : minute;
      second = second < 10 ? "0" + second : second;
      return `${minute}:${second}`;
    },
    // 获取总时长
    getTotal() {
      this.totalTime = this.timeFormat(this.myvideo.duration);
    },
    // 获取当前视频播放到的时间
    timeUpdate() {
      this.currentTime = this.timeFormat(this.myvideo.currentTime);
      this.videoProgress = this.myvideo.currentTime / this.myvideo.duration;
    },

    //播放条触发点击事件
    handleProgressClick(event) {
    const clickX = event.clientX;
     console.log(clickX)
    this.setProgress(clickX);  
     
},
    setProgress(x) {   //传递点击位置  
          

console.log("Progress："+this.progress)
        const progressRect = this.progress.getBoundingClientRect();   //获取位于窗口的位置

      let progressPercent = (x - progressRect.left) / progressRect.width;
    
        if (progressPercent < 0) {
            progressPercent = 0;
        } else if (progressPercent > 1) {
            progressPercent = 1;
        }
        this.myvideo.currentTime = this.myvideo.duration * progressPercent;
    },

    startDragging(event) {  //开始拖动
        
        this.isDragging = true;
        this.draggingStartX = event.clientX;
        console.log( this.draggingStartX )
    },
    moveDragging(event) {    //拖动过程
        if (this.isDragging) {
            const offsetX = event.clientX - this.draggingStartX;
            this.dotOffsetX = offsetX < 0 ? 0 : offsetX;
            this.setProgress(event.clientX);
        }
    },

    stopDragging() {   //结束拖动
        this.isDragging = false;
        this.dotOffsetX = 0;
    },


    // 静音操作
    toggleMuted() {
      this.isMuted = !this.isMuted;
      this.myvideo.muted = !this.myvideo.muted;
    },
    // 全屏切换
    // 切换到全屏
    toggleFullScreen(event) {
      const myvideo = this.$refs.myvideo;
      // 如果当前是全屏状态，就退出全屏，否则进入全屏状态
      // 获取当前的全屏状态
      let isFullscreen = document.webkitIsFullScreen || document.fullscreen;
      if (!isFullscreen) {
        const inFun =
          myvideo.requestFullscreen || myvideo.webkitRequestFullScreen;
        // 让当前整个播放器进入到全屏状态
        inFun.call(myvideo);
      } else {
        const exitFun =
          document.exitFullscreen || document.webkitExitFullscreen;
        // 退出全屏状态需要使用document
        exitFun.call(document);
      }
    },
    // 显示控制面板
    showControls(){
        this.isShow = true
        // 一定要记得更新起始时间的参照
        this.startTime = Date.now()
    },
  },
  
}


</script>

<style lang='less' scoped>
.video {
  position: relative;

  video {
    width: 100%;
    height: 100%;
  }
   .controller__btn-wrapper {
        position: relative;
        height: calc(100% - 5px);
        display: flex;
        align-items: center;
        color: #fff;
        padding: 0 18px;
    }

    .controller__btn {
        cursor: pointer;
        transition: 0.5s;
        margin: 0 20px;
    }

    .controller__btn:hover {
         color: #409eff;
    }

    .controller__timer {
        margin-left: 15px;
    }

    .controller__btn--fullscreen {
        position: absolute;
        right: 15px;
    }

  .controller__progress-wrapper {
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
}
 .controller__progress {
        height: 5px;
        position: relative;
        width: calc(100% - 30px);
        border-radius: 100px;
        background: #dcdcdc;
        cursor: pointer;
    }

    .controller__progress--passed {
        position: absolute;
        top: 0;
        left: 0;
        background: #409EFF;
    }

    .controller__dot {
        position: absolute;
        z-index: 50;
        left: 0;
        top: -5px;
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background-color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .controller__inner-dot {
        width: 5px;
        height: 5px;
        border-radius: 50%;
        background-color: #409EFF;
    }
  .controls {
    width: 100%;
    height: 40px;
    position: absolute;
    bottom: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      padding: 0 5px;
      color: #fff;
    }
  }
}
</style>