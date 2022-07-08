const defaultTheme = require('./node_modules/tailwindcss/defaultTheme')

module.exports = {
  content: ["./resources/public/js/compiled/cljs-runtime/schnaq.academy.*.js",
            "./resources/public/js/compiled/main.js"],
  darkMode: "class",
  theme: {
    extend: {
      fontFamily: {
        sans: ["Poppins", ...defaultTheme.fontFamily.sans],
      },
      colors: {
        "blue": {
          DEFAULT: "#1292ee",
          dark: "#052740",
        }
      },
    },
  },
  plugins: [
    require("./node_modules/@tailwindcss/forms"),
    require("./node_modules/@tailwindcss/aspect-ratio"),
  ],
}
