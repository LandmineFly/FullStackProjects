<script>
import axios from "axios";
export default {
	data: () => ({
		isFilterShow: false,
		loading: false,
		snackbar: false,
		snackbarMessage: null,
		headers: [
			{ title: "编码", key: "iid" },
			{ title: "测站类型", key: "sttype" },
			{ title: "对象名", key: "iname" },
			{ title: "拼音码", key: "pyk" },
			{ title: "测站号", key: "stid" },
			{ title: "备注", key: "nt" },
		],
		desserts: [],
		totalCount: 0,
		sttype: ["基础水文", "实时雨水情", "水质", "降水蒸发"],
		selectOption: {
			page: 1,
			itemsPerPage: 10,
			searchBy: {
				iid: "",
				sttype: null,
				iname: "",
				pyk: "",
				stid: "",
				nt: "",
			},
			sortBy: undefined,
		},
	}),
	methods: {
		load() {
			this.loading = true;
			var searchBy = this.selectOption.searchBy;
			for (var item in searchBy) {
				if (searchBy[item] != null) {
					searchBy[item] = searchBy[item].trim();
				}
			}
			this.selectOption.searchBy = searchBy;
			//加载逻辑
			axios({
				method: "POST",
				url: "/api/se/select",
				data: this.selectOption,
			})
				.then((value) => {
					if (value.data.code === 200) {
						this.desserts = value.data.data;
						this.totalCount = value.data.totalCount;
						this.loading = false;
					} else {
						this.snackbarMessage = value.data.message;
						this.snackbar = true;
						this.loading = true;
					}
				})
				.catch((error) => {
					if (error.message.search("500") != -1) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else {
						this.snackbarMessage = "未知错误";
						console.log(error);
					}
					this.snackbar = true;
					this.loading = true;
				});
		},

		refresh({ page, itemsPerPage, sortBy }) {
			this.selectOption.page = page;
			this.selectOption.itemsPerPage = itemsPerPage;
			this.selectOption.sortBy = sortBy[0];
			this.load();
		},

		selectWithCondition() {
			this.load();
			this.snackbarMessage = "查询成功！";
			this.snackbar = true;
		},

		clearCondition() {
			this.selectOption.searchBy = {
				iid: "",
				sttype: null,
				iname: "",
				pyk: "",
				stid: "",
				nt: "",
			};
		},

		refreshBtn() {
			this.load();
			this.snackbarMessage = "刷新成功！";
			this.snackbar = true;
		},

		toggleFilter() {
			this.isFilterShow = !this.isFilterShow;
		},
	},
};
</script>

<template>
	<v-snackbar
		v-model="snackbar"
		:timeout="3000"
		location="top"
		class="mt-6"
		variant="elevated"
		color="white"
	>
		<div class="font-weight-bold text-subtitle-1">{{ snackbarMessage }}</div>
		<template v-slot:actions>
			<v-btn
				class="font-weight-bold"
				color="red"
				variant="text"
				@click="
					snackbar = false;
					snackbarMessage = null;
				"
			>
				关闭
			</v-btn>
		</template>
	</v-snackbar>

	<v-btn
		class="position-fixed bottom-0 ma-6"
		color="deep-purple"
		size="large"
		@click="toggleFilter()"
		style="z-index: 999"
		icon
	>
		<v-icon>{{
			this.isFilterShow == true
				? "mdi-filter-remove-outline"
				: "mdi-filter-outline"
		}}</v-icon>
		<v-tooltip activator="parent" location="end">{{
			this.isFilterShow == true ? "关闭筛选器" : "打开筛选器"
		}}</v-tooltip>
	</v-btn>

	<v-expand-transition>
		<v-card class="mb-6" rounded="lg" elevation="5" v-show="isFilterShow">
			<v-card-title class="bg-surface-light pa-4 pl-6">筛选器</v-card-title>

			<v-card-text>
				<v-form>
					<v-container>
						<v-row justify="center" no-gutters>
							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.iid"
									label="编码"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-select
									v-model="selectOption.searchBy.sttype"
									label="测站类型"
									:items="sttype"
								></v-select>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.iname"
									label="对象名"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row justify="center" no-gutters>
							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.pyk"
									label="拼音码"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.stid"
									label="测站号"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.nt"
									label="备注"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row justify="end" no-gutters>
							<v-col cols="auto" class="mx-2">
								<v-btn
									color="red-darken-1"
									variant="elevated"
									@click="clearCondition()"
								>
									清空
								</v-btn>
							</v-col>
							<v-col cols="auto" class="mx-2">
								<v-btn
									color="green-darken-1"
									variant="elevated"
									@click="selectWithCondition()"
								>
									查询
								</v-btn>
							</v-col>
						</v-row>
					</v-container>
				</v-form>
			</v-card-text>
		</v-card>
	</v-expand-transition>

	<v-card rounded="lg" elevation="5">
		<v-data-table-server
			:headers="headers"
			:items="desserts"
			:items-length="totalCount"
			:loading="loading"
			style="line-height: 50px"
			hover
			@update:options="refresh"
		>
			<template v-slot:top>
				<v-toolbar flat>
					<v-toolbar-title>测站信息化表</v-toolbar-title>
					<v-spacer></v-spacer>
					<v-btn
						color="blue-darken-1"
						class="ma-4"
						variant="elevated"
						@click="refreshBtn"
					>
						刷新
					</v-btn>
				</v-toolbar>
			</template>

			<template v-slot:bottom>
							<v-data-table-footer
								items-per-page-text="每页显示条数"
							></v-data-table-footer>
						</template>
		</v-data-table-server>
	</v-card>
</template>

<style></style>
