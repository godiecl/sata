<template>
  <div id="equipos-page" class="row mx-0">
    <div class="col px-0">
      <h3 class="font-weight-bold border-bottom border-primary">
        🔩 Listado de Equipos
        <button type="button" class="btn btn-light btn-sm" @click="$fetch">🔃</button>
      </h3>
    </div>

    <b-table
      id="equipos-table"
      striped hover bordered small head-variant="dark"
      :fields="fields"
      :items="equipos"
      :busy="$fetchState.pending"
    >
      <template #table-busy>
        <div class="text-center text-danger my-2">
          <b-spinner class="align-middle"></b-spinner>
          <strong>Cargando datos de los equipos, por favor espere ..</strong>
        </div>
      </template>

      <template #cell(actions)="data">
        <NuxtLink
          class="btn btn-primary btn-sm"
          :to="`/equipos/${data.item.id}`">Visualizar
        </NuxtLink>
      </template>
    </b-table>

  </div>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "EquiposPage",
  data() {
    return this.initialState();
  },
  async fetch() {
    this.resetState();

    // TODO: add the try catch to detect problems in API calls

    // fetch the simulaciones from the API
    const equipos = await this.$axios.$get("/equipos/");
    console.debug("Equipos:", equipos);

    console.debug("Equipos local:", this.equipos);

    //*
    equipos.forEach((equipo) => {
      console.debug("Equipo:", equipo);
      this.equipos.push({
        id: equipo.id,
        nombre: equipo.nombre,
        descripcion: equipo.descripcion,
        simulaciones: equipo.simulaciones.length,
        componentes: equipo.componentes.length
      });
    });
    //* /

  },
  head() {
    return {
      title: "Equipos"
    };
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {
        // the simulaciones
        equipos: [],
        fields: [
          {
            key: "id",
            label: "ID",
            tdClass: "text-center text-middle",
            thClass: "text-center text-middle"
          },
          {
            key: "nombre",
            label: "Nombre",
            tdClass: "text-center text-middle",
            thClass: "text-center text-middle"
          },
          {
            key: "descripcion",
            label: "Descripción",
            tdClass: "text-center text-middle",
            thClass: "text-center text-middle"
          },
          {
            key: "simulaciones",
            label: "N° Simulaciones",
            tdClass: "text-center text-middle",
            thClass: "text-center text-middle"
          },
          {
            key: "componentes",
            label: "N° Componentes",
            tdClass: "text-center text-middle"
          },
          {
            key: "actions",
            label: "Acciones",
            tdClass: "text-center text-middle",
            thClass: "text-center text-middle"
          }
        ]
      };
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

<style>
.text-middle {
  vertical-align: middle !important;
}
</style>
