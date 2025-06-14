// 导入主样式
import "./assets/main.css";

// 导入Vue
import { createApp } from "vue";
import App from "./App.vue";

// 导入vue-router以及所有路由组件
import router from "./router/index.js";

// 导入Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

// 导入Material Design图标
import "@mdi/font/css/materialdesignicons.css";

//设置Vuetify
const vuetify = createVuetify({
	components,
	directives,
	icons: {
		defaultSet: "mdi",
	},
});

//注册应用
const app = createApp(App);
//注册Vuetify
app.use(vuetify);
//注册路由
app.use(router);
//挂载
app.mount("#app");
