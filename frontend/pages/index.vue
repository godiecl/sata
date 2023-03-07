<template>
  <section id="home-page">
    <div class="jumbotron">
      <h1>S.A.T.A.</h1>
      <p class="lead">Sistema de Alerta Temprana Aluvional</p>
      <hr class="my-4">
      <img
        class="rounded img-fluid"
        src="~/assets/img/maqueta.jpg" alt="maqueta sata">
    </div>
  </section>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "HomePage",
  auth: false,
  head() {
    return {
      title: "Home"
    };
  }
};
</script>
