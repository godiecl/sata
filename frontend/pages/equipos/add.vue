<template>
  <section id="equipo-add-page">
    <div id="equipo-title" class="row mx-0">
      <div class="col px-0">
        <h3 class="font-weight-bold border-bottom border-primary">
          🔩 Crear Nuevo Equipo
        </h3>
      </div>
    </div>
  </section>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "EquipoPage",
  data() {
    return this.initialState();
  },
  async fetch() {
    this.resetState();

    // fetch the simulaciones from the API
    const equipo = await this.$axios.$get(`/equipos/${this.$route.params.id}`);
    console.debug("Equipo:", equipo);

    //*
    this.equipo = {
      id: equipo.id,
      nombre: equipo.nombre,
      descripcion: equipo.descripcion,
      simulaciones: equipo.simulaciones.length,
      componentes: equipo.componentes.length
    };
    //* /
  },
  head() {
    return {
      title: "Equipo"
    };
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {};
    },
    /**
     * Reset the state.
     */
    resetState() {
      Object.assign(this.$data, this.initialState());
    }
  }
};
</script>
