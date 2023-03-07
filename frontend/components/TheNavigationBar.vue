<template>
  <b-navbar toggleable="lg" type="dark" variant="info" class="py-1 px-3">
    <b-navbar-brand href="/">
      <b-img
        src="~/assets/img/logo.png"
        class="d-inline-block align-top"
        height="30"
        alt="SATA" />
      SATA
    </b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <template v-if="$auth.loggedIn">
          <b-nav-item
            v-for="page in pages"
            :key="page.name"
            :to="page.to"
            href="#">{{ page.name }}
          </b-nav-item>
          <!-- b-nav-item href="#">Link</b-nav-item>
          <b-nav-item href="#" disabled>Disabled</b-nav-item>
          <b-nav-item href="#" active>Disabled</b-nav-item -->
        </template>
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown v-if="$auth.loggedIn" right>
          <!-- Using 'button-content' slot -->
          <template #button-content>
            <em>{{ $auth.user.nombre }} ({{ $auth.user.email }})</em>
          </template>
          <b-dropdown-item
            to="/usuarios">Cerrar Sesión
          </b-dropdown-item>
        </b-nav-item-dropdown>
        <!-- Using 'button-content' slot -->
        <b-nav-item
          v-else
          to="/usuarios"
          href="#">Iniciar Sesión
        </b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "NavigationBar",
  data() {
    return {
      pages: [
        {
          name: "Simulaciones",
          icon: "chart-lines",
          to: "/simulaciones"
        },
        {
          name: "Equipos",
          icon: "bolt",
          to: "/equipos"
        }
      ]
    };
  },
  mounted() {
    console.debug("route.params", this.$route.params);
    console.debug("auth", this.$auth.user);
    console.debug("auth.loggedIn", this.$auth.loggedIn);
  }
};

</script>
