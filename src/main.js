import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from "@/router";
import "./style.css";
// 引入动画
import 'animate.css';
import store from "@/store";

import api from "@/api";
Vue.prototype.$api = api;
import utils from "@/utils";
Vue.prototype.$utils = utils;
Vue.prototype.$eventBus = new Vue();

import waterfall from 'vue-waterfall'
Vue.use(waterfall)
import 'element-ui/lib/theme-chalk/base.css';
import CollapseTransition from 'element-ui/lib/transitions/collapse-transition';
Vue.component(CollapseTransition.name, CollapseTransition)
Vue.config.productionTip = false
Vue.use(ElementUI);
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
