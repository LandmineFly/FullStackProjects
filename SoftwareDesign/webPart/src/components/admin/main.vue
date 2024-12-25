<script>
import axios from "axios";
export default {
	data: () => ({
		links: [
			["测站总表", "/admin/t0"],
			["测站信息化表", "/admin/t1"],
		],
		isPasswordShow: false,
		regisInputError: false,
		regisInputErrorMessage: "",
		snackbar: false,
		snackbarMessage: "",
		isEgg: false,
		dialog: false,
		regisInfo: {
			usr: null,
			pwd: null,
		},
		clickCount: 0,
		rules: {
			countRequirement(value) {
				if (value != null) {
					value = value.trim();
				} else {
					return "不能为空";
				}
				if (value == "") {
					return "不能为空";
				} else if (value.length < 4) {
					return "不能少于4个字符";
				} else {
					return true;
				}
			},
		},
	}),
	methods: {
		adminLogout() {
			axios({
				method: "POST",
				url: "/api/usr/setLogout",
				data: { usr: this.$store.state.usr },
			})
				.then((value) => {
					console.log(value);
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
		addClickCount() {
			if (this.clickCount >= 9) {
				this.isEgg = true;
				this.snackbarMessage = "";
				this.snackbar = true;
				this.clickCount = 0;
			} else {
				this.clickCount++;
			}
		},
	},
};
</script>

<template>
	<v-app>
		<v-snackbar
			v-model="snackbar"
			:timeout="isEgg === true ? -1 : 3000"
			location="top"
			class="mt-6"
			variant="elevated"
			color="white"
		>
			<v-img
				v-if="isEgg"
				class="fill-height align-start"
				cover
				width="500px"
				src="/miku.jpg"
			>
			</v-img>
			<div class="font-weight-bold text-subtitle-1">{{ snackbarMessage }}</div>
			<template v-slot:actions>
				<v-btn
					class="font-weight-bold"
					color="red"
					variant="text"
					@click="
						isEgg = false;
						snackbar = false;
						snackbarMessage = null;
						clickCount = 0;
					"
				>
					关闭
				</v-btn>
			</template>
		</v-snackbar>

		<v-navigation-drawer
			class="pa-2"
			disable-resize-watcher
			permanent
			theme="dark"
			image="https://pic.rmb.bdstatic.com/bjh/news/93780347cc4b9325665d5c48115e8462.jpeg"
			elevation="10"
			width="300"
			style="user-select: none"
		>
			<v-list-item style="margin: 30px 0">
				<v-card color="rgba(0,0,0,0)">
					<v-card-title
						class="font-weight-bold text-center text-h5 pa-0"
						style="white-space: normal"
						@click="addClickCount"
					>
						长江测站信息管理系统
					</v-card-title>
					<v-card-subtitle class="text-center text-subtitle-1 pa-0"
						>你好，管理员</v-card-subtitle
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

				<v-divider></v-divider>

				<v-list-subheader
					class="text-subtitle-2 font-weight-bold rounded-lg my-2"
				>
					<v-icon icon="mdi-account-edit"></v-icon>
					用户
				</v-list-subheader>

				<v-list-item
					title="用户管理"
					to="/admin/usr"
					color="yellow-lighten-3"
				></v-list-item>
			</v-list>

			<template v-slot:append>
				<div class="pa-4">
					<v-btn block @click="adminLogout" color="red-darken-4"> 登出 </v-btn>
				</div>
			</template>
		</v-navigation-drawer>

		<v-main style="overflow: auto">
			<v-sheet
				class="pa-3 fill-height"
				elevation="0"
				min-width="1380px"
				style="background-color: #3b5878"
			>
				<router-view></router-view>
			</v-sheet>
		</v-main>
	</v-app>
</template>

<style></style>
