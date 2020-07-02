<template>

<div class="main">
  <footer id="footer" role="contentinfo">
    <p>
      遵循
      <a
        href="https://www.gnu.org/licenses/agpl-3.0.txt"
        target="_blank"
        rel="noopener"
      >AGPL-3.0 开源协议</a>
      <br />Copyright &copy; 2020 Team 3
    </p>
  </footer>
</div>
</template>

<script>
export default {
  name: "myfooter",
  data() {
    return {

        Height: 0


    };
  },
  mounted(){

  },
  methods: {}
};
</script>

<style scoped>
.main {
  background-color: #475050;
}
#footer {
  padding: 30px 0;
  color: #fff;
  text-align: center;

}
#footer .social-icon {
  margin: 0 5px;
  margin-bottom: 10px;
}
#footer .social-icon svg {
  width: 18px;
  height: 18px;
}
#footer a {
  font-weight: 700;
  color: #fff;
}

</style>