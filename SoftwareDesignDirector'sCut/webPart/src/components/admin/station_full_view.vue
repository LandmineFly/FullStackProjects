<script>
import axios from "axios";
export default {
	data: () => ({
		isFilterShow: false,
		loading: false,
		snackbar: false,
		snackbarMessage: null,
		headers: [
			{ title: "编码", key: "编码" },
			{ title: "站名", key: "站名" },
			{ title: "管理机构", key: "管理机构" },
			{ title: "东经", key: "东经" },
			{ title: "北纬", key: "北纬" },
			{ title: "流域水系", key: "流域水系" },
			{ title: "河流名称", key: "河流名称" },
			{ title: "基础水文", key: "基础水文" },
			{ title: "降水蒸发", key: "降水蒸发" },
			{ title: "实时雨水情", key: "实时雨水情" },
			{ title: "水质", key: "水质" },
			{ title: "操作", key: "actions", sortable: false },
		],
		desserts: [],
		totalCount: 0,
		selectOption: {
			page: 1,
			itemsPerPage: 10,
			searchBy: {
				编码: "",
				站名: "",
				管理机构: "",
				东经: "",
				北纬: "",
				流域水系: "",
				河流名称: "",
				基础水文: "",
				降水蒸发: "",
				实时雨水情: "",
				水质: "",
			},
			sortBy: undefined,
		},
	}),

	computed: {
		formTitle() {
			return this.editedIndex === -1 ? "新建" : "编辑";
		},
	},

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
				url: "/api/sv/select",
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
				编码: "",
				站名: "",
				管理机构: "",
				东经: "",
				北纬: "",
				流域水系: "",
				河流名称: "",
				基础水文: "",
				降水蒸发: "",
				实时雨水情: "",
				水质: "",
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
									v-model="selectOption.searchBy.编码"
									label="编码"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.站名"
									label="站名"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.管理机构"
									label="管理机构"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row justify="center" no-gutters>
							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.东经"
									label="东经"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.北纬"
									label="北纬"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.流域水系"
									label="流域水系"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row justify="center" no-gutters>
							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.河流名称"
									label="河流名称"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.基础水文"
									label="基础水文"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.降水蒸发"
									label="降水蒸发"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row justify="center" no-gutters>
							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.实时雨水情"
									label="实时雨水情"
								></v-text-field>
							</v-col>

							<v-col class="ma-2">
								<v-text-field
									v-model="selectOption.searchBy.水质"
									label="水质"
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
					<v-toolbar-title>测站总表</v-toolbar-title>
					<v-spacer></v-spacer>
					<v-btn
						color="blue-darken-1"
						variant="elevated"
						class="ma-2"
						@click="refreshBtn"
					>
						刷新
					</v-btn>
					<div>
						<v-btn
							color="green-darken-1"
							variant="elevated"
							class="ma-4"
							disabled
						>
							新建
						</v-btn>
						<v-tooltip activator="parent" location="start"
							>视图不支持新建</v-tooltip
						>
					</div>
				</v-toolbar>
			</template>
			
			<template v-slot:bottom>
							<v-data-table-footer
								items-per-page-text="每页显示条数"
							></v-data-table-footer>
						</template>
						
			<template v-slot:item.actions="{ item }">
				<span>
					<v-btn
						color="blue-darken-1"
						size="small"
						class="me-2"
						variant="elevated"
						@click="editItem(item)"
						disabled
					>
						编辑
					</v-btn>
					<v-tooltip activator="parent" location="start"
						>视图不支持编辑</v-tooltip
					>
				</span>
				<span>
					<v-btn
						color="red-darken-1"
						size="small"
						variant="elevated"
						@click="deleteItem(item)"
						disabled
					>
						删除
					</v-btn>
					<v-tooltip activator="parent" location="start"
						>视图不支持删除</v-tooltip
					>
				</span>
			</template>
		</v-data-table-server>
	</v-card>
</template>

<style></style>
