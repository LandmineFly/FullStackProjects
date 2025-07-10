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

//导入vuex
import { createStore } from "vuex";
//导入vuex-persist持久化插件
import VuexPersistence from "vuex-persist";

// 导入Material Design图标
import "@mdi/font/css/materialdesignicons.css";


//设置vuex-persist，必须在设置vuex之前
const vuexLocal = new VuexPersistence({
	storage: window.sessionStorage,
});

//设置vuex
const store = createStore({
	state() {
		return {
			theme: "dark",
			mode: "standard",
		};
	},
	mutations: {
		// 注意state后只能跟一个对象，因此如果要传入多个参数，要使用解构赋值
		getTheme(state) {
			return state.theme;
		},
		setTheme(state, theme) {
			state.theme = theme;
		},
		getMode(state) {
			return state.mode;
		},
		setMode(state, mode) {
			state.mode = mode;
		},
	},
	//注册vuex-persist
	plugins: [vuexLocal.plugin],
});

// 设置自定义主题
const acgTheme = {
	// 模板见 https://vuetifyjs.com/zh-Hans/features/theme/
	dark: false,
	colors: {
		background: "#ee827c",
	},
};

//设置Vuetify
const vuetify = createVuetify({
	components,
	directives,
	icons: {
		defaultSet: "mdi",
	},
	// 提供三种主题：light、dark、acgTheme
	// 前两种为默认主题，acgTheme为自定义主题
	theme: {
		// 默认为dark主题
		defaultTheme: "dark",
		themes: {
			acgTheme,
		},
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
