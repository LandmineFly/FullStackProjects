<script>
import axios from "axios";
export default {
	data: () => ({
		links: [
			["测站总表", "/admin/t0"],
			["测站信息化表", "/admin/t1"],
		],
		snackbar: false,
		snackbarMessage: "",
		isManuallyLogout: false,
		ForceLogoutDialog: false,
		isEggShow: false,
		eggClickCount: 0,
	}),
	methods: {
		adminLogout() {
			this.$store.commit("logout");
			this.$router.push("/login");
		},
		manuallyLogout() {
			this.isManuallyLogout = true;
			this.adminLogout();
		},
		addClickCount() {
			if (this.eggClickCount >= 9) {
				this.isEggShow = true;
				this.snackbarMessage = "";
				this.snackbar = true;
				this.eggClickCount = 0;
			} else {
				this.eggClickCount++;
			}
		},
	},
	watch: {
		"$store.state.isLogged"(newValue) {
			if (newValue == 0 && this.isManuallyLogout == false) {
				this.ForceLogoutDialog = true;
				this.isManuallyLogout = false;
			}
		},
	},
};
</script>

<template>
	<v-app>
		<v-dialog v-model="ForceLogoutDialog" width="auto" persistent>
			<v-card
				max-width="500"
				color="red-darken-3"
				class="rounded-lg"
				style="user-select: none"
			>
				<v-card-title class="ma-2 text-h5 font-weight-bold">
					<v-icon icon="mdi-alert" class="mr-2"></v-icon>
					登录已过期！</v-card-title
				>
				<v-divider class="ml-6 mr-6" thickness="3px" opacity="0.3"></v-divider>
				<v-card-text class="ma-2" style="line-height: 1.5">
					您已有超过30分钟未操作，登录已过期，请重新登录
				</v-card-text>
				<template v-slot:actions>
					<v-btn
						variant="outlined"
						class="mr-4 mb-4"
						size="large"
						text="重新登录"
						@click="adminLogout"
					></v-btn>
				</template>
			</v-card>
		</v-dialog>

		<v-snackbar
			v-model="snackbar"
			:timeout="isEggShow === true ? -1 : 3000"
			location="top"
			class="mt-6"
			variant="elevated"
			color="white"
		>
			<v-card v-if="isEggShow" variant="flat">
				<v-card-title class="text-center"
					>别点了，看你这么无聊，不如来看看三角初华吧</v-card-title
				>
				<v-img
					class="fill-height align-start"
					cover
					width="500px"
					src="/miku.jpg"
				>
				</v-img>
			</v-card>

			<div class="font-weight-bold text-subtitle-1">{{ snackbarMessage }}</div>
			<template v-slot:actions>
				<v-btn
					class="font-weight-bold"
					color="red"
					variant="text"
					@click="
						isEggShow = false;
						snackbar = false;
						snackbarMessage = null;
						eggClickCount = 0;
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
			image="/main-left-bar-img.jpeg"
			elevation="10"
			width="300"
			style="user-select: none"
		>
			<v-list-item style="margin: 30px 0">
				<v-card
					color="rgba(0,0,0,0)"
					variant="flat"
					class="rounded-lg"
					link
					@click="addClickCount"
				>
					<v-card-title
						class="font-weight-bold text-center text-h5 pa-0"
						style="white-space: normal"
					>
						长江测站
						<br />
						信息数据中台系统
					</v-card-title>
					<v-card-subtitle class="text-center text-subtitle-1 pa-0"
						>你好，管理员 {{ this.$store.state.usr }}</v-card-subtitle
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
					<v-icon icon="mdi-database-import"></v-icon>
					导入
				</v-list-subheader>

				<v-list-item
					title="数据导入"
					to="/admin/import"
					color="yellow-lighten-3"
				></v-list-item>

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
					<v-btn block @click="manuallyLogout" color="red-darken-4">
						登出
					</v-btn>
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
