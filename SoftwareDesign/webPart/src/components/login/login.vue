<script>
import axios from "axios";
export default {
	data: () => ({
		loginInfo: {
			usr: null,
			pwd: null,
		},
		regisInfo: {
			usr: null,
			pwd: null,
		},
		isPasswordShow: false,
		inputError: false,
		inputErrorMessage: "",
		regisInputError: false,
		regisInputErrorMessage: "",
		snackbar: false,
		snackbarMessage: "",
		dialog: false,
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
		login() {
			if (
				this.rules.countRequirement(this.loginInfo.usr) !== true ||
				this.rules.countRequirement(this.loginInfo.pwd) !== true
			) {
				this.snackbar = true;
				this.snackbarMessage = "用户名或密码非法！";
				this.inputError = true;
				this.inputErrorMessage = "请规范输入！";
				return;
			}
			this.loginInfo.isAdmin = 0;
			this.loginInfo.usr = this.loginInfo.usr.trim();
			this.loginInfo.pwd = this.loginInfo.pwd.trim();
			axios({
				method: "POST",
				url: "/api/usr/login",
				data: this.loginInfo, //明文传输，不管了XD
			})
				.then((value) => {
					if (value.data.code == 200) {
						console.log(value);
						if (value.data.data.isAdmin == 1) {
							this.$store.commit("loginAsAdmin", this.loginInfo.usr);
							this.$router.push("/admin/t0");
						} else {
							this.$store.commit("loginAsCust", this.loginInfo.usr);
							this.$router.push("/cust/t0");
						}
					} else {
						this.inputError = true;
						this.inputErrorMessage = "请重试";
						this.snackbar = true;
						this.snackbarMessage = value.data.message;
						return;
					}
				})
				.catch((error) => {
					if (error.message.search("500") != -1) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else {
						this.snackbarMessage = "未知错误";
					}
					this.snackbar = true;
				});
		},
		register() {
			if (
				this.rules.countRequirement(this.regisInfo.usr) !== true ||
				this.rules.countRequirement(this.regisInfo.pwd) !== true
			) {
				this.snackbar = true;
				this.snackbarMessage = "用户名或密码非法！";
				this.regisInputError = true;
				this.regisInputErrorMessage = "请规范输入！";
				return;
			}
			this.regisInfo.isAdmin = 0;
			this.regisInfo.usr = this.regisInfo.usr.trim();
			this.regisInfo.pwd = this.regisInfo.pwd.trim();
			console.log(this.loginInfo);
			axios({
				method: "POST",
				url: "/api/usr/insert",
				data: this.regisInfo, //明文传输，不管了XD
			})
				.then((value) => {
					if (value.data.code == 200) {
						console.log(value);
						this.snackbar = true;
						this.snackbarMessage = "访客注册成功！";
						this.regisInfo = {
							usr: null,
							pwd: null,
						};
						this.dialog = false;
					} else {
						this.regisInputError = true;
						this.regisInputErrorMessage = "请重试";
						this.snackbar = true;
						this.snackbarMessage = value.data.message;
						return;
					}
				})
				.catch((error) => {
					if (error.message.search("500") != -1) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else {
						this.snackbarMessage = "未知错误";
					}
					this.snackbar = true;
				});
		},
		closeDialog() {
			this.clearRegisInputError();
			this.dialog = false;
			this.regisInfo = {
				usr: null,
				pwd: null,
			};
		},
		clearInputError() {
			this.inputError = false;
			this.inputErrorMessage = "";
		},
		clearRegisInputError() {
			this.regisInputError = false;
			this.regisInputErrorMessage = "";
		},
	},
};
</script>

<template>
	<v-app>
		<v-main style="overflow: auto" class="white">
			<v-snackbar
				v-model="snackbar"
				:timeout="3000"
				location="top"
				class="mt-6"
				variant="elevated"
				color="light-blue-lighten-5"
			>
				<div class="font-weight-bold text-subtitle-1">
					{{ snackbarMessage }}
				</div>
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

			<v-dialog v-model="dialog" max-width="600px" persistent>
				<v-card>
					<v-card-title class="ml-4 mt-6 text-h5 font-weight-bold">
						访客注册
					</v-card-title>
					<v-card-text>
						<v-row no-gutters class="pt-2 pb-2">
							<v-col>
								<v-text-field
									v-model="regisInfo.usr"
									label="用户名"
									prepend-inner-icon="mdi-account-outline"
									:rules="[rules.countRequirement]"
									maxlength="20"
									counter
									:error="regisInputError"
									:error-messages="regisInputErrorMessage"
									@update:modelValue="clearRegisInputError"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row no-gutters class="pt-2 pb-4">
							<v-col>
								<v-text-field
									v-model="regisInfo.pwd"
									label="密码"
									prepend-inner-icon="mdi-lock-outline"
									:type="isPasswordShow ? 'text' : 'password'"
									:append-inner-icon="
										isPasswordShow ? 'mdi-eye-off' : 'mdi-eye'
									"
									@click:append-inner="isPasswordShow = !isPasswordShow"
									:rules="[rules.countRequirement]"
									maxlength="20"
									counter
									:error="regisInputError"
									:error-messages="regisInputErrorMessage"
									@update:modelValue="clearRegisInputError"
								></v-text-field>
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
									@click="closeDialog"
									>取消</v-btn
								>
							</v-col>
							<v-col cols="auto" class="ml-3">
								<v-btn
									size="large"
									variant="elevated"
									color="green"
									@click="register"
									>注册</v-btn
								>
							</v-col>
						</v-row>
					</v-card-actions>
				</v-card>
			</v-dialog>

			<div class="fill-height blur">
				<v-container class="fill-height after-blur">
					<v-row justify="center">
						<v-sheet
							height="600px"
							width="1000px"
							class="rounded-lg"
							elevation="8"
							style="overflow: hidden"
						>
							<v-row no-gutters class="fill-height">
								<v-col cols="7">
									<v-sheet class="fill-height">
										<v-img
											class="fill-height align-start"
											cover
											src="https://p3.itc.cn/q_70/images03/20220609/5532fbd39f934832b1ffa4fdda1660f4.jpeg"
										>
											<v-card-title
												class="mt-8 text-center text-h4 text-white font-weight-black"
												v-text="'长江测站信息管理系统'"
											></v-card-title>
										</v-img>
									</v-sheet>
								</v-col>
								<v-col>
									<v-sheet
										class="fill-height pa-10"
										color="light-blue-lighten-5"
									>
										<v-row no-gutters class="pt-16 pb-8">
											<v-col>
												<v-sheet
													class="text-h4 text-left"
													color="light-blue-lighten-5"
													>欢迎登录</v-sheet
												>
											</v-col>
										</v-row>
										<v-row no-gutters class="pt-8 pb-2">
											<v-col>
												<v-text-field
													v-model="loginInfo.usr"
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
										<v-row no-gutters class="pt-2 pb-4">
											<v-col>
												<v-text-field
													v-model="loginInfo.pwd"
													label="密码"
													prepend-inner-icon="mdi-lock-outline"
													:type="isPasswordShow ? 'text' : 'password'"
													:append-inner-icon="
														isPasswordShow ? 'mdi-eye' : 'mdi-eye-off'
													"
													@click:append-inner="isPasswordShow = !isPasswordShow"
													:rules="[rules.countRequirement]"
													maxlength="20"
													counter
													:error="inputError"
													:error-messages="inputErrorMessage"
													@update:modelValue="clearInputError"
												></v-text-field>
											</v-col>
										</v-row>
										<v-row no-gutters class="pt-4" justify="end">
											<v-col cols="auto" class="mr-3">
												<v-btn
													size="large"
													variant="elevated"
													color="blue"
													@click="dialog = true"
													>访客注册</v-btn
												>
											</v-col>
											<v-col cols="auto" class="ml-3">
												<v-btn
													size="large"
													variant="elevated"
													color="green"
													@click="login"
													>登录</v-btn
												>
											</v-col>
										</v-row>
									</v-sheet>
								</v-col>
							</v-row>
						</v-sheet>
					</v-row>
				</v-container>
			</div>
		</v-main>
	</v-app>
</template>

<style>
.blur {
	position: relative;
	width: 100%;
	height: 100%;
	background-image: url("https://5b0988e595225.cdn.sohucs.com/images/20190619/2537e12b066d41f3a22e667f9787ee3d.jpeg");
	background-size: cover;
	background-position: center;
}

.blur::after {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-image: inherit;
	background-size: inherit;
	background-position: inherit;
	filter: blur(5px) saturate(0.6);
	z-index: 1;
}

.after-blur {
	position: relative;
	z-index: 2;
	display: flex;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 100%;
}
</style>
