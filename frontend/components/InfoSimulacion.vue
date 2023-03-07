<template>
  <div id="simulacion-description" class="row mx-0">
    <div class="col px-0">
      <!-- Simulacion: Nombre -->
      <div class="input-group flex-nowrap mb-1">
        <div class="input-group-prepend">
          <span class="input-group-text">Nombre:</span>
        </div>
        <input
          id="nombre"
          :value="simulacion.nombre"
          type="text"
          style="background-color: transparent;"
          class="form-control"
          name="nombre"
          placeholder="Cargando Nombre de la Simulación .."
          readonly
          disabled
          required>
      </div>

      <!-- Simulacion: Descripcion -->
      <div class="input-group flex-nowrap mb-1">
        <div class="input-group-prepend">
          <span class="input-group-text">Descripción:</span>
        </div>
        <input
          id="nombre"
          :value="simulacion.descripcion"
          type="text"
          style="background-color: transparent;"
          class="form-control"
          name="nombre"
          placeholder="Cargando Descripción de la Simulación .."
          readonly
          disabled
          required>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TheSimulacion",
  props: {
    simulacion: {
      type: Object,
      default: () => ({})
    }
  }
};
</script>

<style scoped>

</style>
