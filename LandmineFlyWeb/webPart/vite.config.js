import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vite.dev/config/
export default defineConfig({
	plugins: [
		vue()
	],
	resolve: {
		alias: {
			"@": fileURLToPath(new URL("./src", import.meta.url)),
		},
	},
	//服务器代理，开发环境使用
	// server: {
	// 	host: true,
	// 	proxy: {
	// 		"/api": {
	// 			target: "http://localhost:8080",
	// 			rewrite: (path) => path.replace(/^\/api/, ""),
	// 			changeOrigin: true,
	// 		},
	// 	},
	// },
});
