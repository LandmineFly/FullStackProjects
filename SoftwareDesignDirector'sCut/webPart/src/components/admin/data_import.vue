<script>
import axios from "axios";
import moment from "moment";
export default {
	data: () => ({
		loading: false,
		snackbar: false,
		snackbarMessage: null,
		importDialog: false,
		headers: [
			{ title: "导入类型", key: "importType" },
			{ title: "导入地址", key: "importUrl" },
			{ title: "导入时间", key: "importTime" },
			{ title: "导入条数", key: "importCount" },
		],
		desserts: [],
		totalCount: 0,
		selectOption: {
			page: 1,
			itemsPerPage: 10,
			sortBy: undefined,
		},
		importSteps: ["输入数据源地址", "选择要导入的表", "检查并导入数据"],
		currentStep: 0,
		isPasswordShow: false,
		importInput: [
			{
				connectType: null,
				url: null,
				username: null,
				password: null,
			},
			{
				tableSelected: null,
			},
		],
		importInputError: [
			{
				connectType: null,
				url: null,
				username: null,
				password: null,
			},
			{
				tableSelected: null,
			},
		],
		tables: [],
		importHeaders: [
			{ title: "编码", key: "iid" },
			{ title: "测站类型", key: "sttype" },
			{ title: "对象名", key: "iname" },
			{ title: "拼音码", key: "pyk" },
			{ title: "测站号", key: "stid" },
			{ title: "备注", key: "nt" },
			{ title: "问题", key: "notSatisfiedReason", sortable: false },
			{ title: "编辑", key: "actions", sortable: false },
		],
		importDesserts: [],
		importSelected: [],
		importEditDataDialog: false,
		importEditSelected: null,
		sttype: ["基础水文", "实时雨水情", "水质", "降水蒸发"],
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
				url: "/api/import/select",
				data: this.selectOption,
			})
				.then((value) => {
					if (value.data.code === 200) {
						this.desserts = value.data.data;
						this.desserts.forEach((item) => {
							// 使用 moment 日期处理库
							// 解析日期字符串，转化为东八区，并自定义格式化输出
							// 日期字符串 belike: "2025-02-21T04:00:00.000+00:00"
							item.importTime = moment
								.utc(item.importTime)
								.utcOffset(8)
								.format("YYYY-MM-DD HH:mm:ss");
						});
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

		refreshBtn() {
			this.load();
			this.snackbarMessage = "刷新成功！";
			this.snackbar = true;
		},

		showImportDialog() {
			this.importDialog = true;
		},

		closeImportDialog() {
			// 清空各步的错误提示
			this.importInputError.forEach((item) => {
				for (var props in item) {
					item[props] = null;
				}
			});
			// 清空各步的输入
			this.importInput.forEach((item) => {
				for (var props in item) {
					item[props] = null;
				}
			});
			// 清空获取的数据
			this.tables = [];
			this.importDesserts = [];
			this.importSelected = [];
			this.importEditSelected = null;
			this.importDialog = false;
			this.currentStep = 0;
		},

		preStep(VuetifyPreFunc) {
			if (this.currentStep != 0) {
				VuetifyPreFunc();
				this.currentStep -= 1;
			}
		},

		testConnectBtn() {
			if (this.currentStep != this.importSteps.length - 1) {
				// 因为要等待axios连接结果，所以要使用异步函数
				this.importInputCheck().then((checkMessage) => {
					if (checkMessage !== true) {
						this.snackbar = true;
						this.snackbarMessage = checkMessage;
					}
				});
			}
		},

		nextStep(VuetifyNextFunc) {
			if (this.currentStep != this.importSteps.length - 1) {
				// 因为要等待axios连接结果，所以要使用异步函数
				this.importInputCheck().then((checkMessage) => {
					if (checkMessage === true) {
						VuetifyNextFunc();
						this.currentStep += 1;
					} else {
						this.snackbar = true;
						this.snackbarMessage = checkMessage;
					}
				});
			}
		},

		// 用于检查各步的前端输入是否合法，后端连接是否成功
		async importInputCheck() {
			if (this.currentStep == 0) {
				// 先清空错误信息
				this.importInputError[0].connectType = null;
				this.importInputError[0].url = null;
				this.importInputError[0].username = null;
				this.importInputError[0].password = null;

				// 前端规则判断
				if (this.importInput[0].connectType == null) {
					this.importInputError[0].connectType = "不能为空";
					return "数据源类型不能为空！";
				}
				this.importInput[0].url = this.importInput[0].url?.trim();
				if (this.importInput[0].url == null || this.importInput[0].url == "") {
					this.importInputError[0].url = "不能为空";
					return "数据源地址不能为空！";
				}
				this.importInput[0].username = this.importInput[0].username?.trim();
				if (
					this.importInput[0].username == null ||
					this.importInput[0].username == ""
				) {
					this.importInputError[0].username = "不能为空";
					return "用户名不能为空！";
				}
				this.importInput[0].password = this.importInput[0].password?.trim();
				if (
					this.importInput[0].password == null ||
					this.importInput[0].password == ""
				) {
					this.importInputError[0].password = "不能为空";
					return "密码不能为空！";
				}

				// 后端连接检测
				return this.testConnect();
			} else if (this.currentStep == 1) {
				// 先清空错误信息
				this.importInputError[1].tableSelected = null;

				// 前端规则判断
				if (this.importInput[1].tableSelected == null) {
					this.importInputError[1].tableSelected = "不能为空";
					return "表名不能为空！";
				}

				// 后端连接检测
				return this.getImportData();
			}
		},

		testConnect() {
			this.snackbar = true;
			this.snackbarMessage = "正在测试连接，请稍等...";
			return axios({
				url: "/api/import/get-tables",
				method: "POST",
				data: {
					url: this.importInput[0].url,
					username: this.importInput[0].username,
					password: this.importInput[0].password,
				},
			})
				.then((data) => {
					if (data.data.code !== 200) {
						return "连接失败！请检查地址、用户名、密码是否正确";
					}
					this.snackbar = true;
					this.snackbarMessage = "连接成功！";
					return true;
				})
				.catch((error) => {
					return "服务器异常，连接失败";
				});
		},

		refreshTablesBtn() {
			this.getTables().then((data) => {
				if (data === true) {
					this.snackbar = true;
					this.snackbarMessage = "获取表成功！";
				} else {
					this.snackbar = true;
					this.snackbarMessage = data;
				}
			});
		},

		getTables() {
			return axios({
				url: "/api/import/get-tables",
				method: "POST",
				data: {
					url: this.importInput[0].url,
					username: this.importInput[0].username,
					password: this.importInput[0].password,
				},
			})
				.then((data) => {
					this.tables = data.data.data;
					return true;
				})
				.catch((error) => {
					console.log(error);
					return "服务器异常，获取表失败！";
				});
		},

		refreshImportDataBtn() {
			this.getImportData().then((data) => {
				if (data === true) {
					this.snackbar = true;
					this.snackbarMessage = "刷新成功！";
				} else {
					this.snackbar = true;
					this.snackbarMessage = data;
				}
			});
		},

		// 为了实现全选逻辑，后端将返回所有数据，分页排序等功能在前端实现
		getImportData() {
			return axios({
				url: "/api/import/start-import",
				method: "POST",
				data: {
					url: this.importInput[0].url,
					username: this.importInput[0].username,
					password: this.importInput[0].password,
					table: this.importInput[1].tableSelected,
				},
			})
				.then((data) => {
					if (data.data.code === 200) {
						this.importDesserts = data.data.data;
						this.importDesserts.forEach((item, index) => {
							// 数据原本没有id属性，前端临时加上以便辨识
							item.id = index;
							// 还要临时加上selectable属性，以便标记不能被选择的列（即有问题的列）
							if (item.notSatisfiedReason.length != 0) {
								item.selectable = false;
							} else {
								item.selectable = true;
							}
						});
						return true;
					}
					// data.data.message 是后端返回的错误信息
					return data.data.message;
				})
				.catch((error) => {
					console.log(error);
					return "服务器异常，获取数据失败！";
				});
		},

		editImportItem(id) {
			this.importEditSelected = Object.assign({}, this.importDesserts[id]);
			this.importEditDataDialog = true;
		},

		closeImportEditDataDialog() {
			this.importEditDataDialog = false;
		},

		recheckImportItem() {
			this.recheck().then((data) => {
				if (data === true) {
					this.snackbar = true;
					this.snackbarMessage = "检查操作完成！";
				} else {
					this.snackbar = true;
					this.snackbarMessage = "服务器错误，检查操作失败！";
				}
			});
		},

		saveImportItem() {
			this.recheck().then((data) => {
				if (data === true) {
					this.importDesserts[this.importEditSelected.id] =
						this.importEditSelected;
					this.snackbar = true;
					this.snackbarMessage = "保存成功！";
					this.importEditDataDialog = false;
				} else {
					this.snackbar = true;
					this.snackbarMessage = "服务器错误，修改不会被保存！";
					this.importEditDataDialog = false;
				}
			});
		},

		recheck() {
			return axios({
				method: "POST",
				data: [this.importEditSelected],
				url: "/api/import/recheck",
			})
				.then((data) => {
					var index = this.importEditSelected.id;
					var item = data.data.data[0];
					// 设置临时属性id
					item.id = index;
					// 设置临时属性selectable
					item.selectable = item.notSatisfiedReason.length == 0;
					this.importEditSelected = item;
					return true;
				})
				.catch((error) => {
					return false;
				});
		},

		formalImportBtn() {
			if (this.importSelected == null || this.importSelected.length == 0) {
				this.snackbar = true;
				this.snackbarMessage = "请选择至少一条数据！";
				return;
			}

			this.formalImport().then((data) => {
				if (data !== false) {
					this.snackbar = true;
					this.snackbarMessage = "成功导入" + data + "条数据！";
				} else {
					this.snackbar = true;
					this.snackbarMessage = "导入失败！";
				}
			});
		},

		formalImport() {
			return axios({
				method: "POST",
				data: this.importSelected,
				url: "/api/import/formal-import",
				params: {
					importUrl: this.importInput[0].url,
				},
			})
				.then((data) => {
					if (data.data.code !== 200) {
						return false;
					}
					// 将刚刚导入的数据在前端临时删除，以提升用户直观体验
					var indexNeedToDelete = [];
					var newImportDesserts = [];
					var deletePointer = 0;
					var pushPointer = 0;
					this.importSelected.forEach((item, index) => {
						indexNeedToDelete.push(item.id);
					});
					indexNeedToDelete.sort((a, b) => a - b);
					this.importDesserts.forEach((item, index) => {
						if (index == indexNeedToDelete[deletePointer]) {
							deletePointer++;
						} else {
							item.id = pushPointer++;
							newImportDesserts.push(item);
						}
					});
					this.importSelected = [];
					this.importDesserts = newImportDesserts;
					// data.data.data 是后端返回的导入数据条数
					return data.data.data;
				})
				.catch((error) => {
					return false;
				});
		},
	},
	watch: {
		currentStep(newValue, oldValue) {
			if (newValue == 1) {
				this.getTables();
			} else if (newValue == 2) {
				this.getImportData().then((data) => {
					if (data === true) {
						this.snackbar = true;
						this.snackbarMessage = "获取数据成功！";
					} else {
						this.snackbar = true;
						this.snackbarMessage = data;
					}
				});
			}
		},
		// 关闭导入界面后自动刷新导入历史
		importDialog(newValue, oldValue) {
			if (newValue == false && oldValue == true) {
				this.load();
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

	<v-dialog
		v-model="importDialog"
		persistent
		transition="dialog-bottom-transition"
		fullscreen
		theme="light"
	>
		<v-card>
			<v-card-title class="bg-grey-lighten-3">
				<p class="float-left text-h5 ma-2 mt-4" style="">导入数据</p>
				<v-btn class="float-right ma-2 rounded-lg" variant="tonal" color="red">
					<v-icon icon="mdi-close" />
					<!-- 注：对于v-menu内莫名出现滚动条的问题，
					 将v-menu的内容包在v-list中即可有效解决 -->
					<v-menu activator="parent">
						<v-card>
							<v-list>
								<v-list-item style="line-height: 1.5">
									<p>您确定要关闭吗？</p>
									<p>所有未保存的数据将丢失！</p>
								</v-list-item>
								<v-list-item>
									<v-card-actions>
										<v-btn
											variant="elevated"
											color="red"
											block
											@click="closeImportDialog"
											>确认关闭</v-btn
										>
									</v-card-actions>
								</v-list-item>
							</v-list>
						</v-card>
					</v-menu>
				</v-btn>
			</v-card-title>
			<v-card-text>
				<v-stepper :items="importSteps" class="fill-height position-relative">
					<template v-slot:actions="{ prev, next }">
						<v-btn
							:disabled="currentStep == 0"
							@click="preStep(prev)"
							color="blue"
							class="ma-5 position-absolute float-left bottom-0 left-0"
							size="x-large"
						>
							上一步
						</v-btn>
						<v-btn
							@click="nextStep(next)"
							color="green"
							class="ma-5 position-absolute float-right bottom-0 right-0"
							size="x-large"
						>
							{{ currentStep == importSteps.length - 1 ? "关闭" : "下一步" }}
							<v-menu
								activator="parent"
								v-if="currentStep == importSteps.length - 1"
							>
								<v-card>
									<v-list>
										<v-list-item style="line-height: 1.5">
											<p>您确定要关闭吗？</p>
											<p>所有未保存的数据将丢失！</p>
										</v-list-item>
										<v-list-item>
											<v-card-actions>
												<v-btn
													variant="elevated"
													color="red"
													block
													@click="closeImportDialog"
													>确认关闭</v-btn
												>
											</v-card-actions>
										</v-list-item>
									</v-list>
								</v-card>
							</v-menu>
						</v-btn>
					</template>

					<template v-slot:item.1>
						<v-card flat>
							<v-container class="ml-8 mb-10">
								<v-row>
									<v-col>
										<p class="text-h5">请输入数据源地址</p>
									</v-col>
								</v-row>
								<v-row>
									<v-col cols="6">
										<v-select
											label="数据源类型"
											:items="['MySQL']"
											v-model="importInput[0].connectType"
											:error-messages="importInputError[0].connectType"
										></v-select>
										<v-text-field
											label="数据源地址"
											v-model="importInput[0].url"
											:error-messages="importInputError[0].url"
										></v-text-field>
										<v-text-field
											label="用户名"
											v-model="importInput[0].username"
											:error-messages="importInputError[0].username"
										></v-text-field>
										<v-text-field
											label="密码"
											v-model="importInput[0].password"
											:error-messages="importInputError[0].password"
											:type="isPasswordShow ? 'text' : 'password'"
											:append-inner-icon="
												isPasswordShow ? 'mdi-eye' : 'mdi-eye-off'
											"
											@click:append-inner="isPasswordShow = !isPasswordShow"
										></v-text-field>
										<v-btn @click="testConnectBtn" color="blue" size="large">
											测试连接
										</v-btn>
									</v-col>
								</v-row>
							</v-container>
						</v-card>
					</template>

					<template v-slot:item.2>
						<v-card flat>
							<v-container class="ml-8 mb-10">
								<v-row>
									<v-col>
										<p class="text-h5">请选择要导入的表</p>
									</v-col>
								</v-row>
								<v-row>
									<v-col cols="6">
										<v-select
											label="表"
											:items="tables"
											v-model="importInput[1].tableSelected"
											:error-messages="importInputError[1].tableSelected"
										></v-select>
										<v-btn @click="refreshTablesBtn" color="blue" size="large">
											刷新
										</v-btn>
									</v-col>
								</v-row>
							</v-container>
						</v-card>
					</template>

					<template v-slot:item.3>
						<v-card flat class="rounded-lg" >
							<!-- show-select 表示显示选择框，item-value="id" 表示vuetify将以行数据对象的id属性来标识某行 -->
							<!-- item-selectable="selectable" 表示vuetify将以行数据对象的selectable属性来标识某行是否可选择-->
							<!-- v-model="importSelected" 表示将已选择的行绑定到importSelected上 -->
							<!-- return-object 表示不再返回已选择的行的标识，而是返回行数据对象本身 -->
							<v-data-table
								:headers="importHeaders"
								:items="importDesserts"
								hover
								class="mb-12"
								style="line-height: 50px"
								show-select
								item-value="id"
								item-selectable="selectable"
								v-model="importSelected"
								return-object
							>
								<template v-slot:top>
									<v-toolbar flat>
										<v-toolbar-title>
											<span> 请选择数据以进行导入 </span>
											<v-btn
												class="ml-5"
												variant="tonal"
												prepend-icon="mdi-help-circle"
												rounded="lg"
											>
												<p>帮助</p>
												<v-menu activator="parent">
													<v-list class="rounded-xl">
														<v-list-item>
															<p style="line-height: 1.5">
																如果提示"数据获取成功"但数据显示为空，
																<br />是因为您所选表的数据格式完全不合法。
																<br />这可能意味着您选择的表有误，请尝试选择其他表。
															</p>
														</v-list-item>
													</v-list>
												</v-menu>
											</v-btn>
										</v-toolbar-title>
										<v-btn
											class="mr-5"
											color="blue"
											variant="tonal"
											prepend-icon="mdi-refresh"
											rounded="lg"
										>
											<p>刷新</p>
											<v-menu activator="parent">
												<v-card class="rounded-xl">
													<v-list>
														<v-list-item style="line-height: 1.5">
															<p>您确定要刷新数据吗？</p>
															<p>这个操作会从服务器重新获取数据</p>
															<p>并覆盖您当前对数据做出的所有修改！</p>
														</v-list-item>
														<v-list-item>
															<v-card-actions>
																<v-btn
																	variant="elevated"
																	color="blue"
																	block
																	@click="refreshImportDataBtn"
																	>确认刷新</v-btn
																>
															</v-card-actions>
														</v-list-item>
													</v-list>
												</v-card>
											</v-menu>
										</v-btn>
										<v-btn
											class="mr-5"
											color="success"
											variant="tonal"
											prepend-icon="mdi-plus"
											rounded="lg"
											@click="formalImportBtn"
										>
											导入
										</v-btn>
									</v-toolbar>
								</template>

								<template v-slot:item.notSatisfiedReason="{ value }">
									<!-- icon 属性本来是让指定图标作为按钮的主要内容的，可是如果v-btn有子标签，图标就会消失
									 （应该是bug），因此只能在子标签中指定图标；但是如果直接去掉 icon 属性，按钮就又不是圆形的了，
									 因此就算已经在子标签中指定了图标，icon 属性也不能删 -->
									<v-btn
										:color="value.length != 0 ? 'error' : 'success'"
										variant="tonal"
										size="small"
										icon=""
									>
										<v-icon
											:icon="
												value.length != 0
													? 'mdi-alert-circle'
													: 'mdi-check-circle'
											"
											:color="value.length != 0 ? 'error' : 'success'"
										></v-icon>
										<v-menu
											activator="parent"
											open-on-hover
											v-if="value.length != 0"
										>
											<v-list class="rounded-xl">
												<v-list-item v-for="item in value">
													<v-chip
														:text="item"
														prepend-icon="mdi-alert-circle"
														color="error"
													>
													</v-chip>
												</v-list-item>
											</v-list>
										</v-menu>
										<v-menu activator="parent" open-on-hover v-else>
											<v-list class="rounded-xl">
												<v-list-item>很好，满足导入要求！</v-list-item>
											</v-list>
										</v-menu>
									</v-btn>
								</template>

								<template v-slot:item.actions="{ item }">
									<v-btn
										icon="mdi-pencil"
										variant="tonal"
										color="blue"
										size="small"
										class="mr-3"
										@click="editImportItem(item.id)"
									></v-btn>
								</template>
							</v-data-table>

							<v-dialog
								v-model="importEditDataDialog"
								max-width="800"
								persistent
							>
								<v-card class="pa-3 rounded-lg">
									<v-card-title>
										编辑数据
										<v-btn
											:color="
												this.importEditSelected.selectable ? 'success' : 'error'
											"
											variant="tonal"
											class="ml-3 rounded-xl"
										>
											<v-icon
												:icon="
													this.importEditSelected.selectable
														? 'mdi-check-circle'
														: 'mdi-alert-circle'
												"
											>
											</v-icon>
											<p>
												{{
													this.importEditSelected.selectable ? "" : "不"
												}}满足导入要求
											</p>
											<v-menu
												activator="parent"
												open-on-hover
												v-if="!this.importEditSelected.selectable"
											>
												<v-list class="rounded-xl">
													<p class="ml-5 mt-2">问题列表：</p>
													<v-list-item
														v-for="item in this.importEditSelected
															.notSatisfiedReason"
													>
														<v-chip
															:text="item"
															prepend-icon="mdi-alert-circle"
															color="error"
														>
														</v-chip>
													</v-list-item>
												</v-list>
											</v-menu>
											<v-menu activator="parent" open-on-hover v-else>
												<v-list class="rounded-xl">
													<v-list-item>很好，满足导入要求！</v-list-item>
												</v-list>
											</v-menu>
										</v-btn>
									</v-card-title>
									<v-card-text>
										<v-row>
											<v-col cols="12" sm="6" md="4">
												<v-text-field
													v-model="importEditSelected.iid"
													label="编码"
													:hint="'输入范例：101.001 / 101.0001'"
													persistent-hint
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="4">
												<v-select
													v-model="importEditSelected.sttype"
													label="测站类型"
													:items="sttype"
													persistent-hint
												></v-select>
											</v-col>
											<v-col cols="12" sm="6" md="4">
												<v-text-field
													v-model="importEditSelected.iname"
													label="对象名"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="4">
												<v-text-field
													v-model="importEditSelected.pyk"
													label="拼音码"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="4">
												<v-text-field
													v-model="importEditSelected.stid"
													label="测站号"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="4">
												<v-text-field
													v-model="importEditSelected.nt"
													label="备注"
												></v-text-field>
											</v-col>
										</v-row>
									</v-card-text>

									<v-card-actions class="position-relative">
										<v-btn
											text="取消"
											variant="elevated"
											class="position-absolute left-0 ml-5"
											@click="closeImportEditDataDialog"
										></v-btn>

										<v-btn
											text="检查"
											variant="elevated"
											color="blue"
											class="mr-3"
											@click="recheckImportItem"
										></v-btn>

										<v-btn
											text="保存"
											variant="elevated"
											color="green"
											class="mr-3"
											@click="saveImportItem"
										></v-btn>
									</v-card-actions>
								</v-card>
							</v-dialog>
						</v-card>
					</template>
				</v-stepper>
			</v-card-text>
		</v-card>
	</v-dialog>

	<v-container fluid class="fill-height">
		<v-row justify="center" align="center">
			<v-col cols="8">
				<v-card class="rounded-lg">
					<v-card-title class="bg-grey-lighten-3">
						<p class="mt-2 mb-2">导入数据</p>
					</v-card-title>
					<v-card-text>
						<v-card
							class="ma-3 mt-5 rounded-xl"
							variant="flat"
							style="height: 200px; border: 2px dashed grey"
							link
							@click="showImportDialog"
						>
							<v-container fluid class="fill-height">
								<v-row justify="center" align="center">
									<v-col cols="auto">
										<p
											class="text-center text-h6"
											style="color: grey; user-select: none"
										>
											<v-icon icon="mdi-plus" style="font-size: 60px" />
											<br />
											点击开始导入新数据
										</p>
									</v-col>
								</v-row>
							</v-container>
						</v-card>
					</v-card-text>
				</v-card>
			</v-col>

			<v-col cols="8">
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
								<v-toolbar-title>导入历史</v-toolbar-title>
								<v-spacer></v-spacer>
								<v-btn
									color="blue-darken-1"
									variant="elevated"
									class="ma-2 mr-6"
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
			</v-col>
		</v-row>
	</v-container>
</template>

<style>
/* 开启了表格的选择框后，表格的横向排版会莫名其妙歪掉，文字弹道偏下，选择框弹道偏上 */
/* 因此要采用强制措施纠正排版 */
.v-data-table td {
	vertical-align: middle !important;
}
.v-data-table th {
	vertical-align: middle !important;
}
</style>