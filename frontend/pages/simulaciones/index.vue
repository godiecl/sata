<template>
  <div id="simulaciones-page" class="row mx-0">
    <div class="col px-0">
      <h3 class="font-weight-bold border-bottom border-primary">📈 Listado de Simulaciones y Ejecuciones
        <NuxtLink
          v-b-tooltip.hover
          title="Agregar una nueva Simulación"
          type="button"
          class="btn btn-light btn-sm"
          to="/simulaciones/add">➕
        </NuxtLink>
        <button
          v-b-tooltip.hover
          type="button"
          title="Recargar el listado de Simulaciones y Ejecuciones"
          class="btn btn-light btn-sm"
          @click="$fetch">🔃
        </button>
      </h3>
      <b-table
        id="simulaciones-table"
        striped hover bordered small fixed head-variant="dark"
        :fields="fields"
        :items="items"
        :busy="$fetchState.pending"
      >
        <template #table-busy>
          <div class="text-center text-danger my-2">
            <b-spinner class="align-middle"></b-spinner>
            <strong>Cargando datos de las simulaciones y ejecuciones, por favor espere ..</strong>
          </div>
        </template>

        <template #cell(simulacion)="data">
          <NuxtLink
            :to="`/simulaciones/${data.value.id}`">{{ data.value.nombre }}
          </NuxtLink>
        </template>

        <template #cell(ejecucion)="data">
          <NuxtLink
            class="btn btn-primary btn-sm"
            :class="getClassEstadoEjecucion(data.value.estado_ejecucion)"
            :to="`/simulaciones/${data.value.id_simulacion}/${data.value.id}`">{{ data.value.estado_ejecucion }}
          </NuxtLink>
        </template>
      </b-table>
    </div>
  </div>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "SimulacionesPage",
  data() {
    return this.initialState();
  },
  async fetch() {
    this.resetState();

    // fetch the simulaciones from the API
    const equipos = await this.$axios.$get("/equipos/");
    // console.log("Equipos:", this.equipos);

    equipos.forEach((equipo) => {
      equipo.simulaciones.forEach((simulacion) => {
        simulacion.ejecuciones.forEach((ejecucion) => {
          this.items.push({
            equipo: {
              id: equipo.id,
              nombre: equipo.nombre,
              descripcion: equipo.descripcion
            },
            simulacion: {
              id: simulacion.id,
              nombre: simulacion.nombre,
              descripcion: simulacion.descripcion
            },
            ejecucion: {
              id: ejecucion.id,
              id_simulacion: simulacion.id,
              nombre: ejecucion.description,
              fecha: ejecucion.fecha_inicio,
              estado_ejecucion: ejecucion.estado_ejecucion
            }
          });
        });
      });
    });

    // console.log("Items:", this.items);
  },
  head() {
    return {
      title: "Simulaciones"
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
        // table fields
        fields: [
          {
            key: "equipo",
            label: "Nombre del Equipo",
            tdClass: "text-center text-middle",
            formatter: (value, key, item) => {
              // console.log("Equipo:", item.equipo);
              return item.equipo.nombre;
            }
          },
          {
            key: "simulacion",
            label: "Nombre de la Simulación",
            tdClass: "text-center text-middle"
          },
          {
            key: "fecha",
            label: "Fecha de Ejecución",
            tdClass: "text-center text-middle",
            formatter: (value, key, item) => {
              // console.log("Ejecucion:", item.ejecucion);
              return new Date(item.ejecucion.fecha).toLocaleString(navigator.language, {
                hour12: false,
                hour: "2-digit",
                minute: "2-digit",
                second: "2-digit",
                year: "numeric",
                month: "long",
                day: "numeric"
              });
            }
          },
          {
            key: "ejecucion",
            label: "Estado de la Ejecución",
            tdClass: "text-center text-middle"
          }
        ],
        // dtable data
        items: []
      };
    },
    /**
     * Reset the state.
     */
    resetState() {
      Object.assign(this.$data, this.initialState());
    },
    /**
     * Get the class for the estado.
     */
    getClassEstadoEjecucion(estado) {
      switch (estado) {
        case "ESTADO_INICIALIZADA":
          return "btn-secondary";
        case "ESTADO_EN_EJECUCION":
          return "btn-success";
        case "ESTADO_DETENIDA":
          return "btn-danger";
        case "ESTADO_TERMINADA":
          return "btn-info";
        case "ESTADO_ERROR":
          return "btn-danger";
        default:
          return "btn-secondary";
      }
    }
  }
};
</script>

<style>
.text-middle {
  vertical-align: middle !important;
}
</style>
