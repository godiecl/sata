<template>
  <b-modal
    id="eventos-componente-modal"
    size="lg"
    :title="'Eventos del ' + componente.nombre"
    :no-close-on-backdrop="true"
    :no-close-on-esc="true"
    :hide-header-close="true"
    @show="onShow"
    @ok="onOk"
    @cancel="onCancel"
  >
    <b-table
      id="eventos-componente-table"
      striped hover bordered small head-variant="dark"
      tbody-tr-class="align-middle"
      :items="eventosFiltered"
      :fields="fields"
    >
      <template #thead-top>
        <b-tr>
          <b-th colspan="5" class="text-right">
            <b-button
              variant="success"
              class="btn-sm"
              @click.prevent="addEvento()">➕ Agregar Nuevo Evento
            </b-button>
          </b-th>
        </b-tr>
      </template>

      <template #cell(index)="data">
        {{ data.index + 1 }}
      </template>

      <template #cell(intensidad)="data">
        <b-form-input
          v-model.number="data.item.intensidad"
          type="number"
          min="0"
          max="100"
          step="1"
          placeholder="Ej: 10"
        />
      </template>

      <template #cell(duracion)="data">
        <b-form-input
          v-model.number="data.item.duracion"
          type="number"
          min="1"
          max="3600"
          step="1"
          placeholder="Ej: 5"
        />
      </template>

      <template #cell(descripcion)="data">
        <b-form-input
          v-model.trim="data.item.descripcion"
          type="text"
          placeholder="Ej: Fallo de la fuente de alimentación"
        />
      </template>

      <template #cell(actions)="data">
        <b-button
          variant="danger"
          @click.prevent="removeEvento(data.index)">Eliminar
        </b-button>
      </template>

      <template #custom-foot>
        <b-tr>
          <b-td colspan="4" class="text-right">
            <strong>Duración Total de la Simulación:</strong>
          </b-td>
          <b-td class="text-center text-middle">
            {{ getTotalTime }}
          </b-td>
        </b-tr>
      </template>

    </b-table>
  </b-modal>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  // https://javascript.plainenglish.io/avoid-mutating-a-prop-directly-7b127b9bca5b
  // https://vuejs.org/guide/components/v-model.html
  name: "EventosModel",
  props: {
    componente: {
      type: Object,
      required: true,
      default: () => {
      }
    },
    eventos: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  emits: ["update:visible", "update:eventos"],
  data() {
    return {
      eventosFiltered: [],
      fields: [
        { key: "index", label: "#", tdClass: "text-center text-middle" },
        { key: "intensidad", label: "Intensidad (%)", tdClass: "text-center text-middle" },
        { key: "duracion", label: "Duración (segundos)", tdClass: "text-center text-middle" },
        { key: "descripcion", label: "Descripción", tdClass: "text-center text-middle" },
        { key: "actions", label: "Acción", tdClass: "text-center text-middle"}
      ]
    };
  },
  computed: {
    getTotalTime() {
      let seconds = this.eventosFiltered.reduce((a, b) => a + b.duracion, 0);
      return (seconds - (seconds %= 60)) / 60 + (seconds > 9 ? ":" : ":0") + seconds;
    }
  },
  methods: {
    addEvento() {
      // const eventos = this.eventos;
      this.eventosFiltered.push({
        id_componente: this.componente.id,
        intensidad: 0,
        duracion: 0,
        descripcion: ""
      });
    },
    removeEvento(index) {
      this.eventosFiltered.splice(index, 1);
    },
    onShow() {
      console.debug("EventosModel.onShow");
      this.eventosFiltered = this.eventos.filter(
        evento => evento.id_componente === this.componente.id
      );
    },
    onOk(bvModalEvent) {
      console.debug("onOk", bvModalEvent);

      console.debug("Eventos:", this.eventos);
      console.debug("EventosFiltered:", this.eventosFiltered);

      // join the eventos from the table with the rest of the eventos
      const eventos = this.eventos
        .filter(evento => evento.id_componente !== this.componente.id)
        .concat(this.eventosFiltered);

      this.$emit("update:eventos", eventos);
      // this.eventosFiltered = [];
      // this.$emit("update:visible", false);
    },
    onCancel(bvModalEvent) {
      console.debug("onCancel", bvModalEvent);
      // this.eventosFiltered = [];
    }
  }
};
</script>

<style>
.text-middle {
  vertical-align: middle !important;
}
</style>
