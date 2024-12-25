import { createApp } from "vue";
import App from "./App.vue";

//导入vue-router
import { createRouter, createWebHashHistory } from "vue-router";

//导入路由组件
import station_encoding from "./components/station_encoding.vue";
import station_full_view from "./components/station_full_view.vue";
import redirect from "./components/redirect.vue";

// 导入Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
// 导入Vuetify所需的Material Design图标
import { aliases, mdi } from 'vuetify/iconsets/mdi-svg'

//清除默认样式
import "./assets/reset.css";
//导入主样式
import "./assets/main.css";

const vuetify = createVuetify({
	components,
	directives,
	icons: {
		defaultSet: 'mdi',
		aliases,
		sets: {
		  mdi,
		},
	  },
});

//设置路由
const routes = [
	{
		path: "/1",
		component: station_full_view,
	},
	{
		path: "/2",
		component: station_encoding,
	},
	{
		path: "/",
		component: redirect,
	}
];

const router = createRouter({
	history: createWebHashHistory(), //使用历史模式
	routes, // routes: routes 的缩写
});

//注册应用
const app = createApp(App);
//注册路由
app.use(router);
//注册Vuetify
app.use(vuetify);
//挂载
app.mount("#app");
