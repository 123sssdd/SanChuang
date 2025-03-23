const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    externals: {
      AMap: "window.AMap", //注意
    },
  },
  devServer: {
    // history模式下的url会请求到服务器端，但是服务器端并没有这一个资源文件，就会返回404，所以需要配置这一项
    historyApiFallback: true, // 解决前端路由刷新404问题
proxy: {
      '/': {
        target: process.env.VUE_APP_BASE_URL,  // 后端 API 地址
        changeOrigin: true,  // 支持跨域
      },
    },
    client: {
      overlay: false,
    },
  },
});
