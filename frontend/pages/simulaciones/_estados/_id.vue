<template>
  <section id="ejecucion-page">

    <div id="simulacion-title" class="row mx-0">
      <div class="col px-0">
        <h3 class="font-weight-bold border-bottom border-primary">📈 Visualizar Ejecución de Simulación
          <button
            v-b-tooltip.hover
            type="button"
            title="Recargar la Ejecución"
            class="btn btn-light btn-sm"
            @click="$fetch">🔃
          </button>
        </h3>
      </div>
    </div>

    <InfoSimulacion :simulacion="simulacion" />

    <div id="simulacion-description" class="row mx-0">
      <div class="col px-0">
        <div class="input-group flex-nowrap mb-1">
          <div class="input-group-prepend">
            <span class="input-group-text">Estado de la Ejecución:</span>
          </div>
          <input
            id="nombre"
            :value="ejecucion.estado_ejecucion"
            type="text"
            style="background-color: transparent;"
            class="form-control"
            name="nombre"
            placeholder="Cargando Estado de la Ejecución .."
            readonly
            disabled
            required>
          <div class="input-group-append">
            <button
              v-if="canStart"
              id="ejecucion-button-iniciar"
              class="btn btn-primary btn-sm"
              type="button"
              @click="startEjecucion">Iniciar Simulación
            </button>
            <button
              v-if="isRunning"
              id="ejecucion-button-terminar"
              class="btn btn-danger btn-sm"
              type="button"
              @click="stopEjecucion">Terminar Simulación
            </button>
          </div>
        </div>
      </div>
    </div>

    <template v-if="!canStart">
      <div
        v-for="(medida, index) in medidas"
        id="ejecucion-componentes"
        :key="index"
        class="row mx-0 justify-content-center"
      >
        <div class="col px-0">
          <h4>{{ medida.componente.nombre }}</h4>
          <div class="grafico">
            <h5>{{ medida.componente.descripcion }}</h5>
            <LineChart :chart-data="buildChartData(medida)" :chart-options="buildChartOptions()" />
          </div>
        </div>
      </div>
    </template>

  </section>
</template>

<script lang="js">
import { Line as LineChart } from "vue-chartjs";
import {
  CategoryScale,
  Chart as ChartJS,
  Filler,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, LineElement, LinearScale, CategoryScale, PointElement, Filler);

export default {
  name: "EjecucionPage",
  components: { LineChart },
  data() {
    return this.initialState();
  },
  async fetch() {
    this.resetState();

    // fetch the simulaciones from the API
    const idSimulacion = parseInt(this.$route.params.estados);
    const idEjecucion = parseInt(this.$route.params.id);
    console.debug(`from params: idSimulacion: ${idSimulacion}, idEjecucion: ${idEjecucion}`);

    // FIXME: lazy load, exists any other option to do this?
    // if (Object.keys(this.simulacion).length === 0) {
    this.simulacion = await this.$axios.$get(`/simulaciones/${idSimulacion}`);
    console.debug("Simulacion:", this.simulacion);

    this.ejecucion = this.simulacion.ejecuciones.find(e => e.id === idEjecucion);
    console.debug("Ejecucion:", this.ejecucion);
    // }

    const medidas = await this.$axios.get(`/simulaciones/${idSimulacion}/ejecuciones/${idEjecucion}/medidas/`);
    // TODO: what to do about the .data?
    this.medidas = medidas.data;
    console.debug("Medidas:", this.medidas);
  },
  head() {
    return {
      title: "Ejecucion"
    };
  },
  computed: {
    canStart() {
      return this.ejecucion.estado_ejecucion === "ESTADO_INICIALIZADA";
    },
    isRunning() {
      return this.ejecucion.estado_ejecucion === "ESTADO_EN_EJECUCION";
    }
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {
        // the objects
        simulacion: {},
        ejecucion: {},
        medidas: []
      };
    },
    /**
     * Reset the state.
     */
    resetState() {
      Object.assign(this.$data, this.initialState());
    },
    /**
     * Start the execution.
     */
    async startEjecucion() {
      const estado = await this.$axios.$post(
        `/simulaciones/${this.simulacion.id}/ejecuciones/${this.ejecucion.id}/ESTADO_EN_EJECUCION`
      );
      console.log(estado);
      alert("Ejecucion de simulacion iniciada, estado ID:" + estado.id + " (" + estado.estado_ejecucion + ")");
      this.$fetch();
    },
    /**
     * Stop the execution.
     */
    async stopEjecucion() {
      const estado = await this.$axios.$post(
        `/simulaciones/${this.simulacion.id}/ejecuciones/${this.ejecucion.id}/ESTADO_TERMINADA`
      );
      console.log(estado);
      alert("Ejecucion de simulacion iniciada, estado ID:" + estado.id + " (" + estado.estado_ejecucion + ")");
      this.$fetch();
    },
    /**
     * Build the chart data.
     */
    buildChartData(medida) {
      const data = {
        labels: [],
        datasets: [
          {
            label: "valor",
            data: [],
            fill: true,
            borderColor: "rgba(0,0,128,0.4)",
            tension: 0.1
          },
          {
            label: "señal",
            data: [],
            fill: true,
            borderColor: "rgb(205,92,92,0.4)",
            tension: 0.1
          }
        ]
      };
      medida.datos.forEach(d => {
        data.labels.push(this.formatDateLabel(new Date(d.fecha)));
        data.datasets[0].data.push(d.valor);
        data.datasets[1].data.push(d.senial);
      });
      return data;
    },
    /**
     * Build the chart options.
     */
    buildChartOptions() {
      return {
        // maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true
          },
          x: {
            title: {
              display: true,
              text: "Tiempo"
            }
          }
        }
      };
    },
    /**
     * Format the date label.
     */
    formatDateLabel(date) {
      return new Date(date).toLocaleString(navigator.language, {
        hour12: false,
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
      });
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
    }
  }
};

</script>

<style>
.grafico {
  display: flex;
  flex-direction: column;
  margin: 0 0 2em 0;
  min-width: 95%;
  max-width: 95%;
  min-height: 18em;
  max-height: 100%;
  overflow: hidden;
  border-radius: .5em;
  text-decoration: none;
  background: #ecebeb;
  border: 1px solid #2162ad;
  padding: 0.5em 0.5em;
  box-shadow: 0 1.5em 2.5em -.5em rgba(0, 0, 0, .15);
  transition: transform .45s ease, background .45s ease;
}

</style>
