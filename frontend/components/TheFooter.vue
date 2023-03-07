<template>
  <footer class="py-0">
    <hr class="m-0" />
    <div class="text-center text-muted">
      SATA v{{ version }} &copy; {{ year }}
      <b-img
        src="~/assets/img/anid-logo.png"
        class="d-inline-block align-text-bottom"
        height="18"
        alt="ANID" />
      Fondef IDeA I+D.
      <a
        href="https://www.ucn.cl/"
        class="text-decoration-none text-muted">Universidad Católica del Norte</a>.

    </div>
  </footer>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "FooterSection",
  computed: {
    version() {
      return this.$config.version;
    },
    year() {
      return new Date().getFullYear();
    }
  }
};
</script>
