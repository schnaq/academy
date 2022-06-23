const defaultTheme = require('./resources/public/node_modules/tailwindcss/defaultTheme')

module.exports = {
  content: ["./resources/public/js/compiled/cljs-runtime/schnaq.academy.*.js"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Poppins", ...defaultTheme.fontFamily.sans],
      },
      colors: {
        "blue": "#1292ee",
      },
    },
  },
  plugins: [
    require("./resources/public/node_modules/@tailwindcss/forms"),
    require("./resources/public/node_modules/@tailwindcss/aspect-ratio"),
  ],
}
