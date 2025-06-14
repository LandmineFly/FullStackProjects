import { createApp } from "vue";
import App from "./App.vue";

//导入外部配置文件
import properties from "/properties.json";

//导入axios
import axios from "axios";

//导入vue-router
import { createRouter, createWebHashHistory } from "vue-router";

//导入路由组件
import admin_main from "./components/admin/main.vue";
import admin_station_encoding from "./components/admin/station_encoding.vue";
import admin_station_full_view from "./components/admin/station_full_view.vue";
import admin_user_management from "./components/admin/user_management.vue";
import admin_data_import from "./components/admin/data_import.vue";
import cust_main from "./components/cust/main.vue";
import cust_station_encoding from "./components/cust/station_encoding.vue";
import cust_station_full_view from "./components/cust/station_full_view.vue";
import login from "./components/login/login.vue";

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
			token: null,
			usr: null,
			isLogged: 0, // 0未登录，1访客，2管理员
			inactivityTimer: null,
			keepAliveInterval: null,
		};
	},
	mutations: {
		// 注意state后只能跟一个对象，因此如果要传入多个参数，要使用解构赋值
		loginAsAdmin(state, { token, usr }) {
			state.token = token;
			state.usr = usr;
			state.isLogged = 2;
		},
		loginAsCust(state, { token, usr }) {
			state.token = token;
			state.usr = usr;
			state.isLogged = 1;
		},
		logout(state) {
			state.token = null;
			state.usr = null;
			state.isLogged = 0;
		},
		refreshToken(state, newToken) {
			state.token = newToken;
		},
		setInactivityTimer(state, timer) {
			state.inactivityTimer = timer;
		},
		setKeepAliveInterval(state, interval) {
			state.keepAliveInterval = interval;
		},
	},
	//注册vuex-persist
	plugins: [vuexLocal.plugin],
});

// 设置axios请求拦截器
axios.interceptors.request.use((config) => {
	// 如果存储有token，则默认在请求头中添加token
	const token = store.state.token;
	if (token) {
		config.headers.token = token;
	}
	return config;
});

// 设置axios响应拦截器
axios.interceptors.response.use(
	// 后端认证通过
	(response) => {
		// 如果响应中有新 Token，更新本地存储
		const newToken = response.headers["new-token"];
		if (newToken) {
			console.log("刷新token");
			store.commit("refreshToken", newToken);
		}
		// 前端放行
		return response;
	},
	// 后端认证失败
	(error) => {
		if (error.response.status === 401) {
			store.commit("logout");
			clearTimerIntervalEventListener();
		}
		// 继续向上抛出异常，交由具体请求处理
		return Promise.reject(error);
	}
);

//设置路由
const routes = [
	{
		path: "/login",
		component: login,
		meta: { requiresAuth: false },
	},
	{
		path: "/admin",
		component: admin_main,
		meta: { requiresAuth: true },
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
			{
				path: "import",
				component: admin_data_import,
			},
		],
	},
	{
		path: "/cust",
		component: cust_main,
		meta: { requiresAuth: true },
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
];

const router = createRouter({
	history: createWebHashHistory(), // 使用历史模式
	routes,
});

//路由守卫，同时实现计时器初始化和重定向功能
router.beforeEach((to, from, next) => {
	// 若用户未登录
	if (store.state.isLogged == 0) {
		clearTimerIntervalEventListener();

		if (to.path !== "/login") {
			next("/login");
		} else {
			next();
		}
	}
	// 若用户已登录
	else {
		window.removeEventListener("mousemove", resetTimer);
		window.removeEventListener("keydown", resetTimer);
		window.removeEventListener("click", resetTimer);

		window.addEventListener("mousemove", resetTimer);
		window.addEventListener("keydown", resetTimer);
		window.addEventListener("click", resetTimer);

		// 因为刷新会把定时器刷掉，因此在用户已登录的情况下，
		// 每次经过路由守卫时都要重新设置定时器
		store.commit(
			"setInactivityTimer",
			setTimeout(() => {
				store.commit("logout");
			}, properties.inactivityTimerProp)
		);

		store.commit(
			"setKeepAliveInterval",
			setInterval(() => {
				if (store.state.token != null) {
					axios({
						method: "POST",
						url: "/api/refresh-token",
					}).catch((error) => {
						console.log(error);
					});
				}
			}, properties.keepAliveIntervalProp)
		);

		// 若用户以访客身份登录
		if (store.state.isLogged == 1) {
			if (to.path.startsWith("/cust")) {
				next();
			} else {
				next("/cust/t0");
			}
		}
		// 若用户以管理员身份登录
		if (store.state.isLogged == 2) {
			if (to.path.startsWith("/admin")) {
				next();
			} else {
				next("/admin/t0");
			}
		}
	}
});

// 重置无操作定时器
function resetTimer() {
	if (store.state.inactivityTimer != null) {
		clearTimeout(store.state.inactivityTimer);
	}
	store.commit(
		"setInactivityTimer",
		setTimeout(() => {
			store.commit("logout");
		}, properties.inactivityTimerProp)
	);

	/**
	 * 相较于上面的代码，添加了防抖设计
	 * 在触发resetTimer且300ms内没有再次触发时，才会重置计时器
	 * 相当于在外面又套了一个300ms的计时器
	 * 我的评价是就这个系统的规模而言没什么卵用，反而徒增功耗
	 * 而且如果用户有多动症，一直在触发resetTimer
	 * 那岂不是用户一直在动，计时器反而不会重置，导致计时器结束后直接登出？
	 */
	// let resetTimerId = null;
	// if (resetTimer != null) {
	// 	clearTimeout(resetTimerId);
	// }
	// resetTimerId = setTimeout(() => {
	// 	if (store.state.inactivityTimer != null) {
	// 		clearTimeout(store.state.inactivityTimer);
	// 	}
	// 	store.commit(
	// 		"setInactivityTimer",
	// 		setTimeout(() => {
	// 			store.commit("logout");
	// 		}, properties.inactivityTimerProp)
	// 	);
	// }, 300);
}

// 清除事件监听器、无操作定时器、保活操作定时器
function clearTimerIntervalEventListener() {
	window.removeEventListener("mousemove", resetTimer);
	window.removeEventListener("keydown", resetTimer);
	window.removeEventListener("click", resetTimer);
	//清空计时器
	if (store.state.inactivityTimer != null) {
		clearTimeout(store.state.inactivityTimer);
		store.commit("setInactivityTimer", null);
	}
	if (store.state.keepAliveInterval != null) {
		clearInterval(store.state.keepAliveInterval);
		store.commit("setKeepAliveInterval", null);
	}
}

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
//注册vuex
app.use(store);
//挂载
app.mount("#app");
