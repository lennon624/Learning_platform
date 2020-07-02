<template>
  <div class="about">
    <h1>视频播放测试</h1>
    <subvideo :src="Src"></subvideo>
  </div>
</template>

<script>
import subvideo from '@/components/subvideo.vue'
import { getStore, setStore, removeStore } from "@/libs/localstorage";
export default {
  components:{
    subvideo
  },
  data(){
    return{
     Src: "",
    };
  },
  mounted(){
    this.Src =   getStore("videosrc");
 },
}
</script>