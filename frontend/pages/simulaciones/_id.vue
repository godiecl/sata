<template>
  <section id="simulacion-page">
    <div id="simulacion-title" class="row mx-0">
      <div class="col px-0">
        <h3 class="font-weight-bold border-bottom border-primary">📈 Simulación
          <button
            v-b-tooltip.hover
            type="button"
            title="Recargar la Simulación y sus Ejecuciones"
            class="btn btn-light btn-sm"
            @click="$fetch">🔃
          </button>
        </h3>
      </div>
    </div>

    <InfoSimulacion :simulacion="simulacion" />

    <div id="simulacion-eventos" class="row mx-0">
      <div class="col px-0">
        <b-table
          id="simulacion-eventos-table"
          caption="Listado de Eventos de la Simulación"
          striped hover bordered small caption-top head-variant="dark"
          tbody-tr-class="align-middle"
          :fields="fields_eventos"
          :items="eventos"
          :busy="$fetchState.pending"
        >

          <template #table-busy>
            <div class="text-center text-danger my-2">
              <b-spinner class="align-middle"></b-spinner>
              <strong>Cargando datos de los eventos, por favor espere ..</strong>
            </div>
          </template>

          <template #cell(index)="data">
            {{ data.index + 1 }}
          </template>

        </b-table>
      </div>
    </div>

    <div id="simulacion-ejecuciones" class="row mx-0">
      <div class="col px-0">
        <b-table
          id="simulacion-ejecuciones-table"
          caption="Listado de Ejecuciones de la Simulación"
          striped hover bordered small caption-top head-variant="dark"
          tbody-tr-class="align-middle"
          :fields="fields_ejecuciones"
          :items="ejecuciones"
          :busy="$fetchState.pending"
        >

          <template #table-busy>
            <div class="text-center text-danger my-2">
              <b-spinner class="align-middle"></b-spinner>
              <strong>Cargando datos las ejecuciones, por favor espere ..</strong>
            </div>
          </template>

          <template #thead-top>
            <b-tr>
              <b-th colspan="3" class="text-right">
                <button class="btn btn-primary btn-sm" @click="addEjecucion()">➕ Agregar Nueva Ejecución</button>
              </b-th>
            </b-tr>
          </template>

          <template #cell(index)="data">
            {{ data.index + 1 }}
          </template>

          <template #cell(ejecucion)="data">
            <NuxtLink
              class="btn btn-primary btn-sm m-0 p-0"
              :class="getClassEstadoEjecucion(data.item.estado_ejecucion)"
              :to="`/simulaciones/${data.item.id_simulacion}/${data.item.id}`">{{ data.item.estado_ejecucion }}
            </NuxtLink>
          </template>

        </b-table>
      </div>
    </div>

  </section>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "SimulacionPage",
  data() {
    return this.initialState();
  },
  async fetch() {
    this.resetState();

    // the id from params
    const idSimulacion = parseInt(this.$route.params.id);

    // fetch the simulaciones from the API
    this.simulacion = await this.$axios.$get(`/simulaciones/${idSimulacion}`);
    console.debug("Simulacion:", this.simulacion);

    this.simulacion.eventos.forEach((evento) => {
      this.eventos.push({
        intensidad: evento.intensidad,
        duracion: evento.duracion,
        componente: evento.componente.nombre,
        descripcion: evento.descripcion
      });
    });
    console.debug("Eventos:", this.eventos);

    this.simulacion.ejecuciones.forEach((ejecucion) => {
      this.ejecuciones.push({
        id: ejecucion.id,
        id_simulacion: this.simulacion.id,
        descripcion: ejecucion.descripcion,
        fecha_inicio: ejecucion.fecha_inicio,
        estado_ejecucion: ejecucion.estado_ejecucion
      });
    });
    console.debug("Ejecuciones:", this.ejecuciones);

    // this.ejecucion = this.simulacion.ejecuciones.find(e => e.id === idEjecucion);
    // this.ejecucion.medidas = await this.fetchMedidasRequest(idSimulacion, idEjecucion).then(res => res.json());
    // console.debug("Ejecucion", this.ejecucion);
  },
  head() {
    return {
      title: "Simulacion"
    };
  },
  mounted() {
    console.info("route.params", this.$route.params);
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {
        // the simulacion
        simulacion: {},
        eventos: [],
        fields_eventos: [
          {
            key: "index",
            label: "#"
          },
          {
            key: "intensidad",
            label: "Intensidad"
          },
          {
            key: "duracion",
            label: "Duración"
          },
          {
            key: "componente",
            label: "Componente"
          },
          {
            key: "descripcion",
            label: "Descripción"
          }
        ],
        ejecuciones: [],
        fields_ejecuciones: [
          {
            key: "descripcion",
            label: "Descripción"
          },
          {
            key: "fecha_inicio",
            label: "Fecha de Inicio",
            formatter: (value) => {
              return new Date(value).toLocaleString(navigator.language, {
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
            label: "Ejecución",
            tdClass: "text-center"
          }
        ]
      };
    },
    /**
     * Reset the state.
     */
    resetState() {
      Object.assign(this.$data, this.initialState());
    },
    /**
     * Add a new Ejecucion.
     */
    async addEjecucion() {
      const ejecucion = await this.$axios.$post(`/simulaciones/${this.simulacion.id}/ejecuciones`);
      console.debug(ejecucion);
      this.$fetch();
    },
    /**
     * Format the date.
     */
    formatDate(date) {
      return new Date(date).toLocaleString(navigator.language, {
        hour12: false,
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        year: "numeric",
        month: "long",
        day: "numeric"
      });
    },
    /**
     * The Class for the Estado.
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
