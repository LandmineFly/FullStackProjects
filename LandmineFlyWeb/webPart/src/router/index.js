import { createRouter, createWebHistory } from "vue-router";
import main from "../components/main.vue";

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/",
			name: "main",
			component: main,
			// 以下是延迟加载的写法，只有路由被访问时组件才会被加载
			// component: () => import('../views/HomeView.vue'),
		},
	],
});

export default router;
