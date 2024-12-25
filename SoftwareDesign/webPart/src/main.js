import { createApp } from "vue";
import App from "./App.vue";

//导入vue-router
import { createRouter, createWebHashHistory } from "vue-router";

//导入路由组件
import admin_main from "./components/admin/main.vue";
import admin_station_encoding from "./components/admin/station_encoding.vue";
import admin_station_full_view from "./components/admin/station_full_view.vue";
import admin_user_management from "./components/admin/user_management.vue";
import cust_main from "./components/cust/main.vue";
import cust_station_encoding from "./components/cust/station_encoding.vue";
import cust_station_full_view from "./components/cust/station_full_view.vue";
import login from "./components/login/login.vue";
import redirect from "./components/redirect.vue";

// 导入Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
// 导入Material Design图标
import "@mdi/font/css/materialdesignicons.css";

//清除默认样式
import "./assets/reset.css";
//导入主样式
import "./assets/main.css";

//导入vuex
import { createStore } from "vuex";
//导入vuex-persist持久化插件
import VuexPersistence from "vuex-persist";

//设置vuex-persist，必须在设置vuex之前
const vuexLocal = new VuexPersistence({
	storage: window.sessionStorage,
});

//设置vuex
const store = createStore({
	state() {
		return {
			isLogged: 0,
			usr: null,
		};
	},
	mutations: {
		logout(state) {
			state.isLogged = 0;
			state.usr = null;
		},
		loginAsCust(state, usr) {
			state.isLogged = 1;
			state.usr = usr
		},
		loginAsAdmin(state, usr) {
			state.isLogged = 2;
			state.usr = usr
		},
	},
	//注册vuex-persist
	plugins: [vuexLocal.plugin],
});

//设置Vuetify
const vuetify = createVuetify({
	components,
	directives,
	icons: {
		defaultSet: "mdi",
	},
});

//设置路由
const routes = [
	{
		path: "/login",
		component: login,
		beforeEnter: (to, from) => {
			switch (store.state.isLogged) {
				case 0:
					return true;
				case 1:
					alert("您已经以访客身份登录，正在为您跳转");
					return "/cust/t0";
				case 2:
					alert("您已经以管理员身份登录，正在为您跳转");
					return "/admin/t0";
			}
		},
	},
	{
		path: "/admin",
		component: admin_main,
		beforeEnter: (to, from) => {
			switch (store.state.isLogged) {
				case 0:
					alert("您未登录，正在为您跳转");
					return "/login";
				case 1:
					alert("您已经以访客身份登录，正在为您跳转");
					return "/cust/t0";
				case 2:
					return true;
			}
		},
		children: [
			{
				path: "t0",
				component: admin_station_full_view,
			},
			{
				path: "t1",
				component: admin_station_encoding,
			},
			{
				path: "usr",
				component: admin_user_management,
			},
		],
	},
	{
		path: "/cust",
		component: cust_main,
		beforeEnter: (to, from) => {
			switch (store.state.isLogged) {
				case 0:
					alert("您未登录，正在为您跳转");
					return "/login";
				case 1:
					return true;
				case 2:
					alert("您已经以管理员身份登录，正在为您跳转");
					return "/admin/t0";
			}
		},
		children: [
			{
				path: "t0",
				component: cust_station_full_view,
			},
			{
				path: "t1",
				component: cust_station_encoding,
			},
		],
	},
	{
		path: "/",
		component: redirect,
		beforeEnter: (to, from) => {
			return "/login";
		},
	},
];

const router = createRouter({
	history: createWebHashHistory(), //使用历史模式
	routes, // routes: routes 的缩写
});

//注册应用
const app = createApp(App);
//注册Vuetify
app.use(vuetify);
//注册路由
app.use(router);
//注册vuex
app.use(store);
//挂载
app.mount("#app");
