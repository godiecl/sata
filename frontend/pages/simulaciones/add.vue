<template>
  <section id="simulacion-add-page">
    <div id="simulacion-title" class="row mx-0">
      <div class="col px-0">
        <h3 class="font-weight-bold border-bottom border-primary">
          📈 Crear Nueva Simulación
        </h3>
      </div>
    </div>

    <EventosModel
      :visible="isModalEventosVisible"
      :eventos="simulacion.eventos"
      :componente="componente"
      @update:eventos="onUpdateEventos"
    />

    <div id="simulacion-body" class="row mx-0">
      <div class="col px-0">
        <!-- Simulacion: Equipo spbre el cual se aplicara -->
        <div class="input-group flex-nowrap mb-1">
          <div class="input-group-prepend">
            <span class="input-group-text">Seleccione el Equipo al cual se agregará la Simulación:</span>
          </div>
          <select
            id="equipo"
            class="form-select form-control"
            name="equipo"
            @change="onChangeEquipo($event)">
            <option selected disabled value="This is Equipo">Seleccione un Equipo de la lista ..</option>
            <option
              v-for="equipo in equipos"
              :key="equipo.id"
              :value="equipo.id"
            >{{ equipo.nombre }}
            </option>
          </select>
        </div>

        <!-- Datos de la Simulacion -->
        <template v-if="Object.keys(equipo).length !== 0">
          <!-- Simulacion: Nombre -->
          <div class="input-group flex-nowrap mb-1">
            <div class="input-group-prepend">
              <span class="input-group-text">Nombre de la Simulación:</span>
            </div>
            <input
              id="nombre"
              v-model.trim="simulacion.nombre"
              type="text"
              class="form-control"
              name="nombre"
              placeholder="Ej: Simulación Lluvia Norte-Sur nivel: bajo"
              required>
          </div>

          <!-- Simulacion: Descripcion -->
          <div class="input-group flex-nowrap mb-1">
            <div class="input-group-prepend">
              <span class="input-group-text">Descripción de la Simulación:</span>
            </div>
            <textarea
              id="descripcion"
              v-model.trim="simulacion.descripcion"
              class="form-control"
              name="descripcion"
              cols="30"
              rows="2"
              placeholder="Ej: Simulación Lluvia Leve Norte a Sur en la Quebrada de la Chimba"
              required />
          </div>

          <!-- Simulacion: Componentes del Equipo y sus eventos -->
          <h4 class="pb-2 fw-bolder">🔧 Componentes del Equipo seleccionado:</h4>
          <div
            v-for="componente in equipo.componentes"
            :key="componente.id"
            class="input-group mb-1">
            <input
              type="text"
              class="form-control text-nowrap"
              :placeholder="componente.nombre"
              :value="`${componente.nombre} (${componente.descripcion})`"
              disabled
              readonly>
            <div class="input-group-append">
              <b-button
                :disabled="isSensor(componente)"
                variant="primary"
                @click="showModalEventosComponente(componente)">Editar Eventos
              </b-button>
            </div>
          </div>

          <div class="d-flex justify-content-center mb-3">
            <b-button
              variant="primary"
              @click.prevent="addSimulacion">Crear Simulación
            </b-button>
          </div>

        </template>

      </div>
    </div>

  </section>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "AddSimulacionPage",
  data() {
    return this.initialState();
  },
  async fetch() {
    // fetch the simulaciones from the API
    // this.equipos = await this.fetchEquiposRequest().then(res => res.json());
    // console.log("Equipos", this.equipos);
    const equipos = await this.$axios.get("/equipos/");
    this.equipos = equipos.data;
    console.debug("Equipos:", this.equipos);
  },
  head() {
    return {
      title: "Agregar Nueva Simulacion"
    };
  },
  mounted() {
    console.debug("route.params", this.$route.params);
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {
        // the equipos
        equipos: [],
        // equipo selected
        equipo: {},

        // modal eventos visible
        isModalEventosVisible: false,

        // the componentes
        componente: {},

        // the simulacion
        simulacion: {
          id_equipo: "",
          nombre: "",
          descripcion: "",
          eventos: [] // {id_componente, intensidad, duracion, descripcion}
        }
      };
    },
    /**
     * Reset the state.
     */
    resetState() {
      Object.assign(this.$data, this.initialState());
    },
    /**
     * The name of componente
     */
    isSensor(componente) {
      return componente.tipo_componente === "TIPO_SENSOR";
    },
    /**
     * OnChange del select de equipos.
     * @param event del select.
     */
    onChangeEquipo(event) {
      // componente.tipo_componente = event.target.value;
      console.debug("onChangeEquipo, value:", event.target.value);
      // equipos availables
      console.debug("Equipos:", this.equipos);
      // equipo selected
      this.equipo = this.equipos.find(eq => eq.id === parseInt(event.target.value));
      // set the id_equipo
      this.simulacion.id_equipo = this.equipo.id;
      console.debug("Equipo Selected", this.equipo);
    },
    /**
     * Show the modal to admin the list of Eventos.
     */
    showModalEventosComponente(componente) {
      console.debug("Componente:", componente);
      this.componente = componente;
      this.$bvModal.show("eventos-componente-modal");
    },
    /**
     * OnHide del modal de eventos.
     */
    onUpdateEventos(eventos) {
      console.debug("update", eventos);
      this.simulacion.eventos = eventos;
    },
    /**
     * OnClick del boton de agregar simulacion.
     * @param event
     * @returns {Promise<void>}
     */
    async addSimulacion(event) {
      const simulacion = await this.$axios.$post("/simulaciones/", this.simulacion);
      console.debug("Simulacion Creada:", simulacion);
      // TODO: need to reset the state?
      this.resetState();

      await this.$router.push("/simulaciones");
    }
  }
};
</script>
