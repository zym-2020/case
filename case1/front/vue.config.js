const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8090,
    proxy: {
      "/case": {
        target: "http://localhost:9999/",
        changeOrigin: true,
        pathRewrite: {
          "^/case": "",
        },
      },
    },
  },
});
