<script>
import axios from "axios";
export default {
	data: () => ({
		links: [
			["测站总表", "/cust/t0"],
			["测站信息化表", "/cust/t1"],
		],
	}),
	methods: {
		custLogout() {
			axios({
				method: "POST",
				url: "/api/usr/setLogout",
				data: {usr: this.$store.state.usr}
			})
				.then((value) => {
					console.log(value)
					this.$store.commit("logout");
					this.$router.push("/login");
				})
				.catch((error) => {
					if (error.message.search("500") != -1) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else {
						this.snackbarMessage = "未知错误";
					}
					this.snackbar = true;
					this.loading = true;
				});
		},
	},
};
</script>

<template>
	<v-app>
		<v-navigation-drawer
			class="pa-2"
			disable-resize-watcher
			permanent
			theme="dark"
			image="https://pic.rmb.bdstatic.com/bjh/news/93780347cc4b9325665d5c48115e8462.jpeg"
			elevation="10"
			width="300"
			style="user-select: none;"
		>
			<v-list-item style="margin: 30px 0">
				<v-card color="rgba(0,0,0,0)">
					<v-card-title
						class="font-weight-bold text-center text-h5 pa-0"
						style="white-space: normal"
					>
						长江测站信息管理系统
					</v-card-title>
					<v-card-subtitle class="text-center text-subtitle-1 pa-0"
						>你好，访客</v-card-subtitle
					>
				</v-card>
			</v-list-item>
			<v-divider></v-divider>
			<v-list
				density="default"
				nav
				class="rounded-lg"
				bg-color="rgba(0,0,0,0.4)"
			>
				<v-list-subheader
					class="text-subtitle-2 font-weight-bold rounded-lg mb-2"
				>
					<v-icon icon="mdi-waterfall"></v-icon>
					测站
				</v-list-subheader>

				<v-list-item
					v-for="[text, link] in links"
					:title="text"
					:to="link"
					color="yellow-lighten-3"
				></v-list-item>
			</v-list>

			<template v-slot:append>
				<div class="pa-4">
					<v-btn block @click="custLogout" color="red-darken-4"> 登出 </v-btn>
				</div>
			</template>
		</v-navigation-drawer>

		<v-main style="overflow: auto">
			<v-sheet
				class="pa-5 fill-height"
				elevation="0"
				min-width="1200px"
				style="background-color: #3b5878"
			>
				<router-view></router-view>
			</v-sheet>
		</v-main>
	</v-app>
</template>

<style></style>
