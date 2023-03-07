import pkg from "./package";

export default {
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Target: https://go.nuxtjs.dev/config-target
  target: "static",

  server: {
    port: 8082 // default: 3000
  },

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: "Sistema de Alerta Temprana Aluvional",
    titleTemplate: "SATA | %s",
    htmlAttrs: {
      lang: "en"
    },
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1, shrink-to-fit=no" },
      {
        hid: "description",
        name: "description",
        content: "Generación de un prototipo de sistema de  alerta temprana ante amenaza aluvional en quebradas para la ciudad de Antofagasta ante aumento de precipitaciones debido al cambio climático."
      },
      { name: "format-detection", content: "telephone=no" }
    ],
    link: [{ rel: "icon", type: "image/x-icon", href: "/favicon.ico" }]
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    "~/assets/css/main.css"
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/eslint
    ["@nuxtjs/eslint-module", {
      fix: true
    }],
    // https://google-fonts.nuxtjs.org/setup/
    "@nuxtjs/google-fonts"
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/bootstrap
    "bootstrap-vue/nuxt",
    // https://go.nuxtjs.dev/axios
    "@nuxtjs/axios",
    // https://auth.nuxtjs.org/guide/setup
    "@nuxtjs/auth-next",
    // https://go.nuxtjs.dev/content
    "@nuxt/content"
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    baseURL: "http://localhost:8081/api",
    retry: {
      retries: 3
    },
    debug: true,
    common: {
      "Accept": "application/json, text/plain, */*"
    }
  },

  // Auth module configuration: https://auth.nuxtjs.org/guide/setup
  /* @nuxtjs/auth-next
  auth: {
    cookie: {
      options: {
        expires: 365,
        // secure: process.env.APP_ENV !== 'local',
      },
    },
    strategies: {
      local: {
        tokenRequired: false,
        tokenType: false,
        autoFetchUser: false,
        endpoints: {
          login: {
            url: "/usuarios/login",
            method: "post"
          },
          user: false,
          logout: false,
        },
      },
    },
  },
  //*/

  //* @nuxtjs/auth-next
  auth: {
    localStorage: false,
    rewriteRedirects: false,
    fullPathRedirect: true,
    // When enabled user will be redirected on login/logouts
    watchLoggedIn: false,
    redirect: {
      login: "/usuarios",
      logout: "/usuarios",
      home: "/",
      callback: false
    },
    strategies: {
      // https://auth.nuxtjs.org/schemes/local
      cookie: {
        options: {
          secure: false,
          maxAge: 60 * 60 * 24 * 7, // one week
          sameSite: "lax",
        },
        token: {
          required: true,
          type: false
        },
        user: {
          property: false,
          autoFetch: false
        },
        endpoints: {
          user: false,
          login: {
            url: "/usuarios/login",
            method: "post"
          },
          logout: false
        }
      }
    }
  },
  //*/

  // Router module configuration: https://go.nuxtjs.dev/config-router
  router: {
    middleware: ["auth"]
  },

  publicRuntimeConfig: {
    // apiURL: 'http://localhost:8081/api'
    version: pkg.version
  },

  // Content module configuration: https://go.nuxtjs.dev/config-content
  content: {},

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    analyze: true,
    devtools: true,
    parallel: false
    // extractCSS: true,
    // optimizeCSS: true,
  },

  // https://github.com/s3rver/nuxt-modal
  modal: {
    pluginName: "modal",
    layout: "default",
    responsive: false
  },

  // https://google-fonts.nuxtjs.org/options
  googleFonts: {
    download: true,
    prefetch: true,
    preconnect: true,
    preload: true,
    families: {
      Ubuntu: true,
      Oswald: true,
      Raleway: true,
      Roboto: true,
      Poppins: true,
      Lato: true
    }
  },

  bootstrapVue: {
    // Install the `IconsPlugin` plugin (in addition to `BootstrapVue` plugin)
    icons: true
  }

};
