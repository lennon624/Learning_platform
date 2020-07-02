<template>
  <div id="app">
   
   <router-view :key="key"></router-view>
  </div>
</template>
<script>
    export default{
	data(){
	    return {
 
		}
	},
	computed:{
	    key(){
	        return this.$route.path + Math.random();
	    }
	}
    }
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
.body {
    margin: 0;
    padding:0;
    height: 100%;
  }
</style>
