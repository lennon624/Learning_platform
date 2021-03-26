<template>




  <div class="one">
        <div class="vid" v-bind:style="{width: myWidth}">
          <subvideo :src="Src" style="background-color: black"></subvideo>
        </div>
  </div>


</template>

<script>
import subvideo from '@/components/subvideo.vue'
import { getStore, setStore, removeStore } from "@/libs/localstorage";
export default {
  components: {
    subvideo
  },
  data() {
    return {
      isCollapse: true,
      Src : '',
      myWidth: window.innerWidth - 500 + "px"
    };
  },
 mounted(){

    this.Src =   getStore("videosrc");
 },
  methods: {

  }
};
</script>

<style scoped>
.vid {
  width: 60%;
  height: 40%;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 250px;
}
.one{
  border-style:solid;
  border-width:0.1px;
  border-radius: 10px;
  border-color: #edeeef;
  background-color: white;
  margin-left: 0px;
}
</style>
