<script>
import axios from "axios";
export default {
	data: () => ({
		dialog: false,
		dialogDelete: false,
		isPasswordShow: false,
		inputError: false,
		inputErrorMessage: "",
		isFilterShow: false,
		loading: false,
		snackbar: false,
		snackbarMessage: null,
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
			editPasswordRequirement(value) {
				if (value == null) {
					return true;
				}
				value = value.trim();
				if (value.length == "") {
					return true;
				} else if (value.length < 4) {
					return "不能少于4个字符";
				} else {
					return true;
				}
			},
		},
		headers: [
			{ title: "用户名", key: "usr" },
			{
				title: "账户类型",
				// 必须是 is_admin 而不能是 isAdmin
				// 因为这个key会作为字符串直接拼接到SQL中的，并不能进行自动驼峰转换
				key: "is_admin",
				value(item) {
					if (item.isAdmin === 0) {
						return "访客";
					} else {
						return "管理员";
					}
				},
			},
			{ title: "操作", key: "actions", sortable: false },
		],
		desserts: [],
		totalCount: 0,
		selectType: [
			{
				title: "全部",
				value: null,
			},
			{
				title: "访客",
				value: 0,
			},
			{
				title: "管理员",
				value: 1,
			},
		],
		type: [
			{
				title: "访客",
				value: 0,
			},
			{
				title: "管理员",
				value: 1,
			},
		],
		edit: {
			editedIndex: -1,
			oldItem: {
				usr: "",
				isAdmin: 0,
			},
			editedItem: {
				usr: "",
				psw: "",
				isAdmin: 0,
			},
		},
		defaultItem: {
			usr: "",
			psw: "",
			isAdmin: 0,
		},
		selectOption: {
			page: 1,
			itemsPerPage: 10,
			searchBy: {
				usr: "",
				isAdmin: null,
			},
			sortBy: undefined,
		},
	}),
	computed: {
		passwordRules() {
			if (this.edit.editedIndex === -1) {
				return this.rules.countRequirement;
			} else {
				return this.rules.editPasswordRequirement;
			}
		},
	},

	methods: {
		load() {
			this.loading = true;
			if (this.selectOption.searchBy.usr != null) {
				this.selectOption.searchBy.usr = this.selectOption.searchBy.usr.trim();
			}
			//加载逻辑
			axios({
				method: "POST",
				url: "/api/usr/select",
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
				usr: "",
				isAdmin: null,
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
			this.edit.editedIndex = this.desserts.indexOf(item);
			this.edit.oldItem = Object.assign({}, item);
			this.edit.editedItem = Object.assign({}, item);
			this.dialog = true;
		},

		deleteItem(item) {
			this.edit.editedIndex = this.desserts.indexOf(item);
			this.edit.editedItem = Object.assign({}, item);
			this.dialogDelete = true;
		},

		deleteItemConfirm() {
			//删除逻辑
			if(this.$store.state.usr === this.edit.editedItem.usr){
				this.snackbarMessage = "不能删除自己！";
				this.snackbar = true;
				this.closeDelete();
				return;
			}
			axios({
				method: "DELETE",
				url: "/api/usr/delete",
				data: this.edit.editedItem,
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
						console.log(error);
					}
					this.snackbar = true;
					this.loading = true;
				});
			this.closeDelete();
		},

		close() {
			this.dialog = false;
			this.$nextTick(() => {
				this.edit.editedItem = Object.assign({}, this.defaultItem);
				this.edit.editedIndex = -1;
				this.clearInputError();
			});
		},

		closeDelete() {
			this.dialogDelete = false;
			this.$nextTick(() => {
				this.edit.editedItem = Object.assign({}, this.defaultItem);
				this.edit.editedIndex = -1;
				this.clearInputError();
			});
		},

		save() {
			if (this.edit.editedIndex > -1) {
				//修改逻辑
				if (
					this.rules.countRequirement(this.edit.editedItem.usr) !== true ||
					this.rules.editPasswordRequirement(this.edit.editedItem.pwd) !== true
				) {
					this.snackbar = true;
					this.snackbarMessage = "用户名或密码非法！";
					this.inputError = true;
					this.inputErrorMessage = "请规范输入！";
					return;
				}
				this.edit.editedItem.usr = this.edit.editedItem.usr.trim();
				if (this.edit.editedItem.pwd != null) {
					this.edit.editedItem.pwd = this.edit.editedItem.pwd.trim();
				}

				axios({
					method: "PUT",
					url: "/api/usr/update",
					data: {
						oldUser: this.edit.oldItem,
						newUser: this.edit.editedItem,
					},
				})
					.then((value) => {
						if (value.data.code === 200) {
							this.snackbarMessage = "修改成功！";
							this.snackbar = true;
							this.close();
						} else {
							this.inputError = true;
							this.inputErrorMessage = "请重试";
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
							console.log(error);
						}
						this.snackbar = true;
						this.loading = true;
					});
			} else {
				//新建逻辑
				if (
					this.rules.countRequirement(this.edit.editedItem.usr) !== true ||
					this.rules.countRequirement(this.edit.editedItem.pwd) !== true
				) {
					this.snackbar = true;
					this.snackbarMessage = "用户名或密码非法！";
					this.inputError = true;
					this.inputErrorMessage = "请规范输入！";
					return;
				}
				this.edit.editedItem.usr = this.edit.editedItem.usr.trim();
				this.edit.editedItem.pwd = this.edit.editedItem.pwd.trim();
				axios({
					method: "POST",
					url: "/api/usr/insert",
					data: this.edit.editedItem,
				})
					.then((value) => {
						if (value.data.code === 200) {
							this.snackbarMessage = "添加成功！";
							this.snackbar = true;
							this.close();
						} else {
							this.inputError = true;
							this.inputErrorMessage = "请重试";
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
							console.log(error);
						}
						this.snackbar = true;
						this.loading = true;
					});
			}
		},
		clearInputError() {
			this.inputError = false;
			this.inputErrorMessage = "";
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

	<v-container fluid class="fill-height">
		<v-row justify="center" align="center">
			<v-col cols="6">
				<v-expand-transition>
					<v-card class="mb-6" rounded="lg" elevation="5" v-show="isFilterShow">
						<v-card-title class="bg-surface-light pa-4 pl-6">筛选器</v-card-title>

						<v-card-text>
							<v-form>
								<v-container>
									<v-row justify="center" no-gutters>
										<v-col class="ma-2">
											<v-text-field
												v-model="selectOption.searchBy.usr"
												label="用户名"
											></v-text-field>
										</v-col>

										<v-col class="ma-2">
											<v-select
												v-model="selectOption.searchBy.isAdmin"
												label="账户类型"
												:items="selectType"
											></v-select>
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

				<v-card rounded="lg" elevation="8">
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
								<v-toolbar-title>用户信息</v-toolbar-title>
								<v-spacer></v-spacer>
								<v-btn
									color="blue-darken-1"
									class="ma-2"
									variant="elevated"
									@click="refreshBtn"
								>
									刷新
								</v-btn>
								<v-dialog v-model="dialog" max-width="600px" persistent>
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
										<v-card-title class="ml-4 mt-6 text-h5 font-weight-bold">
											{{
												this.edit.editedIndex === -1
													? "创建新用户"
													: "编辑用户信息"
											}}
										</v-card-title>
										<v-card-text>
											<v-row no-gutters class="pt-2 pb-2">
												<v-col>
													<v-text-field
														v-model="edit.editedItem.usr"
														label="用户名"
														prepend-inner-icon="mdi-account-outline"
														:rules="[rules.countRequirement]"
														maxlength="20"
														counter
														:error="inputError"
														:error-messages="inputErrorMessage"
														@update:modelValue="clearInputError"
													></v-text-field>
												</v-col>
											</v-row>
											<v-row no-gutters class="pt-2 pb-2">
												<v-col>
													<v-text-field
														v-model="edit.editedItem.pwd"
														label="密码"
														hint="(密码若不修改则留空)"
														persistent-hint
														prepend-inner-icon="mdi-lock-outline"
														:type="isPasswordShow ? 'text' : 'password'"
														:append-inner-icon="
															isPasswordShow ? 'mdi-eye' : 'mdi-eye-off'
														"
														@click:append-inner="
															isPasswordShow = !isPasswordShow
														"
														:rules="[passwordRules]"
														maxlength="20"
														counter
														:error="inputError"
														:error-messages="inputErrorMessage"
														@update:modelValue="clearInputError"
													></v-text-field>
												</v-col>
											</v-row>
											<v-row no-gutters class="pt-2 pb-4">
												<v-col>
													<v-select
														v-model="edit.editedItem.isAdmin"
														label="账户类型"
														:items="type"
													></v-select>
												</v-col>
											</v-row>
										</v-card-text>
										<v-card-actions>
											<v-row no-gutters class="pa-4" justify="end">
												<v-col cols="auto" class="mr-3">
													<v-btn
														size="large"
														variant="elevated"
														color="blue"
														@click="close"
														>取消</v-btn
													>
												</v-col>
												<v-col cols="auto" class="ml-3">
													<v-btn
														size="large"
														variant="elevated"
														color="green"
														@click="save"
													>
														{{
															this.edit.editedIndex === -1 ? "注册" : "保存"
														}}</v-btn
													>
												</v-col>
											</v-row>
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
						
						<template v-slot:bottom>
							<v-data-table-footer
								items-per-page-text="每页显示条数"
							></v-data-table-footer>
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
			</v-col>
		</v-row>
	</v-container>
</template>

<style></style>
