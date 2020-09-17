import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

// router setup

import router from './routes/router'
import BootstrapVue from "bootstrap-vue";
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)


import BaseInput from '@/components/Inputs/BaseInput.vue';
import Card from '@/components/Cards/Card.vue';
import { ValidationProvider, ValidationObserver } from 'vee-validate';


Vue.component(Card.name, Card);
Vue.component(BaseInput.name, BaseInput);
Vue.component('validation-provider', ValidationProvider)
Vue.component('validation-observer', ValidationObserver)

new Vue({

  el:'#app',
  router:router,
  render: h => h(App),
}).$mount('#app')
