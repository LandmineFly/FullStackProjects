<script>
import axios from "axios";
export default {
	data: () => ({
		dialog: false,
		dialogDelete: false,
		isFilterShow: false,
		loading: false,
		snackbar: false,
		snackbarMessage: null,
		rules: [
			(value) => {
				if (value) {
					return true;
				}
				return "该项不能为空";
			},
		],
		headers: [
			{ title: "编码", key: "iid" },
			{ title: "测站类型", key: "sttype" },
			{ title: "对象名", key: "iname" },
			{ title: "拼音码", key: "pyk" },
			{ title: "测站号", key: "stid" },
			{ title: "备注", key: "nt" },
			{ title: "操作", key: "actions", sortable: false },
		],
		desserts: [],
		totalCount: 0,
		sttype: ["基础水文", "实时雨水情", "水质", "降水蒸发"],
		editedIndex: -1,
		editedItem: {
			iid: "",
			sttype: null,
			iname: "",
			pyk: "",
			stid: "",
			nt: "",
		},
		defaultItem: {
			iid: "",
			sttype: null,
			iname: "",
			pyk: "",
			stid: "",
			nt: "",
		},
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

	computed: {
		formTitle() {
			return this.editedIndex === -1 ? "新建" : "编辑";
		},
	},

	created() {
		this.load();
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

		editItem(item) {
			this.editedIndex = this.desserts.indexOf(item);
			this.editedItem = Object.assign({}, item);
			this.dialog = true;
		},

		deleteItem(item) {
			this.editedIndex = this.desserts.indexOf(item);
			this.editedItem = Object.assign({}, item);
			this.dialogDelete = true;
		},

		deleteItemConfirm() {
			//删除逻辑
			axios({
				method: "DELETE",
				url: "/api/se/delete",
				data: this.editedItem,
			})
				.then((value) => {
					if (value.data.code === 200) {
						this.snackbarMessage = "删除成功！";
						this.snackbar = true;
					} else {
						this.snackbarMessage = value.data.message;
						this.snackbar = true;
					}
					this.load();
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
			this.closeDelete();
		},

		close() {
			this.dialog = false;
			this.$nextTick(() => {
				this.editedItem = Object.assign({}, this.defaultItem);
				this.editedIndex = -1;
			});
		},

		closeDelete() {
			this.dialogDelete = false;
			this.$nextTick(() => {
				this.editedItem = Object.assign({}, this.defaultItem);
				this.editedIndex = -1;
			});
		},

		save() {
			var isSuccess = 0;
			if (this.editedIndex > -1) {
				//修改逻辑
				axios({
					method: "PUT",
					url: "/api/se/update",
					data: this.editedItem,
				})
					.then((value) => {
						if (value.data.code === 200) {
							this.snackbarMessage = "修改成功！";
							this.snackbar = true;
							isSuccess = 1;
						} else {
							this.snackbarMessage = value.data.message;
							this.snackbar = true;
						}
						this.load();
						if (isSuccess == 1) {
							this.close();
						}
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
			} else {
				//新建逻辑
				axios({
					method: "POST",
					url: "/api/se/insert",
					data: this.editedItem,
				})
					.then((value) => {
						if (value.data.code === 200) {
							this.snackbarMessage = "添加成功！";
							this.snackbar = true;
							isSuccess = 1;
						} else {
							this.snackbarMessage = value.data.message;
							this.snackbar = true;
						}
						this.load();
						if (isSuccess == 1) {
							this.close();
						}
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
			}
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
			<v-card-item>
				<v-card-title>筛选器</v-card-title>
			</v-card-item>

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
						class="ma-2"
						variant="elevated"
						@click="refreshBtn"
					>
						刷新
					</v-btn>
					<v-dialog v-model="dialog" max-width="800px" persistent>
						<template v-slot:activator="{ props }">
							<v-btn
								color="green-darken-1"
								class="ma-4"
								variant="elevated"
								v-bind="props"
							>
								新建
							</v-btn>
						</template>
						<v-card>
							<v-card-title>
								<span class="text-h5">{{ formTitle }}</span>
							</v-card-title>
							<v-card-text>
								<v-container>
									<v-row>
										<v-col cols="12" sm="6" md="4">
											<v-text-field
												v-model="editedItem.iid"
												label="编码"
												:disabled="editedIndex != -1"
												:hint="
													editedIndex != -1
														? '主键不能修改'
														: '输入范例：101.001 / 101.0001'
												"
												persistent-hint
												:rules="rules"
											></v-text-field>
										</v-col>
										<v-col cols="12" sm="6" md="4">
											<v-select
												v-model="editedItem.sttype"
												label="测站类型"
												:items="sttype"
												:disabled="editedIndex != -1"
												:hint="editedIndex != -1 ? '主键不能修改' : ''"
												persistent-hint
												:rules="rules"
											></v-select>
										</v-col>
										<v-col cols="12" sm="6" md="4">
											<v-text-field
												v-model="editedItem.iname"
												label="对象名"
											></v-text-field>
										</v-col>
										<v-col cols="12" sm="6" md="4">
											<v-text-field
												v-model="editedItem.pyk"
												label="拼音码"
											></v-text-field>
										</v-col>
										<v-col cols="12" sm="6" md="4">
											<v-text-field
												v-model="editedItem.stid"
												label="测站号"
											></v-text-field>
										</v-col>
										<v-col cols="12" sm="6" md="4">
											<v-text-field
												v-model="editedItem.nt"
												label="备注"
											></v-text-field>
										</v-col>
									</v-row>
								</v-container>
							</v-card-text>

							<v-card-actions>
								<v-spacer></v-spacer>
								<v-btn color="blue-darken-1" variant="elevated" @click="close">
									取消
								</v-btn>
								<v-btn color="green-darken-1" variant="elevated" @click="save">
									保存
								</v-btn>
							</v-card-actions>
						</v-card>
					</v-dialog>
					<v-dialog v-model="dialogDelete" max-width="300px" persistent>
						<v-card>
							<v-card-title class="text-h9 text-center ma-8"
								>你确定要删除吗？</v-card-title
							>
							<v-card-actions>
								<v-spacer></v-spacer>
								<v-btn
									color="blue-darken-1"
									variant="elevated"
									@click="closeDelete"
									>取消</v-btn
								>
								<v-btn
									color="red-darken-1"
									variant="elevated"
									@click="deleteItemConfirm"
									>确认</v-btn
								>
								<v-spacer></v-spacer>
							</v-card-actions>
						</v-card>
					</v-dialog>
				</v-toolbar>
			</template>
			<template v-slot:item.actions="{ item }">
				<v-btn
					color="blue-darken-1"
					size="small"
					class="me-2"
					variant="elevated"
					@click="editItem(item)"
				>
					编辑
				</v-btn>
				<v-btn
					color="red-darken-1"
					size="small"
					variant="elevated"
					@click="deleteItem(item)"
				>
					删除
				</v-btn>
			</template>
		</v-data-table-server>
	</v-card>
</template>

<style></style>
