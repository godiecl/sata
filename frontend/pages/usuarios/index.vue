<template>
  <section id="usuarios-page">
    <b-form
      class="mt-5 mx-auto" style="width: 450px;">

      <b-form-group
        label="Dirección de Correo Electrónico:"
        label-for="email-input"
        description="Por favor ingrese el correo electrónico.">
        <b-form-input
          id="email-input"
          v-model="form.rut_email"
          type="email"
          autocomplete="on"
          placeholder="Ej: usuario@ucn.cl"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group
        label="Contraseña:"
        label-for="password-input"
        description="Por favor ingrese la contraseña.">
        <b-form-input
          id="password-input"
          v-model="form.password"
          type="password"
          autocomplete="on"
          placeholder="Ej: este es mi contraseña ultrasecreta"
          required
        ></b-form-input>
      </b-form-group>

      <div class="d-flex justify-content-center mb-3">
        <b-button
          variant="primary"
          @click.prevent="onSubmit">Ingresar al Sistema
        </b-button>
      </div>
    </b-form>
  </section>
</template>

<script lang="js">
// noinspection JSUnusedGlobalSymbols
export default {
  name: "LoginPage",
  auth: false,
  data() {
    return this.initialState();
  },
  head() {
    return {
      title: "Acceso a SATA"
    };
  },
  mounted() {
    this.$auth.logout();
  },
  methods: {
    /**
     * The initial state.
     */
    initialState() {
      return {
        form: {
          rut_email: "",
          password: ""
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
     * OnSubmit the form.
     */
    async onSubmit(event) {

      this.$bvToast.toast("Por favor espere ..", {
        title: "Ingresando al Sistema",
        toaster: "b-toaster-top-center",
        variant: "warning",
        solid: true,
        noCloseButton: true
      });

      try {
        const usuario = await this.$auth.loginWith("cookie", {
          data: this.form
        });
        console.debug("UsuarioResponse:", usuario);
        this.$auth.setUser(usuario.data);

        console.debug("UsuarioLogged:", this.$auth.user);
        // FIXME: this login redirect need to be automatic
        await this.$router.push("/simulaciones");
      } catch (errors) {
        console.error("Error:", errors);
        this.$bvToast.toast("Revise los datos ingresados!", {
          title: "Error",
          toaster: "b-toaster-top-center",
          variant: "danger",
          solid: true
        });
      }
    }
  }
};
</script>
