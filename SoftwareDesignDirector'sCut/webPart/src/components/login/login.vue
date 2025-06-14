<script>
import axios from "axios";
export default {
	data: () => ({
		loginInfo: {
			usr: null,
			pwd: null,
			isAdmin: null,
		},
		regisInfo: {
			usr: null,
			pwd: null,
			isAdmin: 0,
		},
		isPasswordShow: false,
		inputError: false,
		inputErrorMessage: "",
		regisInputError: false,
		regisInputErrorMessage: "",
		snackbar: false,
		snackbarMessage: "",
		registerDialog: false,
		eggCount: 0,
		eggDialog: false,
		isEggDialogCloseBtnDisabled: true,
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
			this.loginInfo.usr = this.loginInfo.usr.trim();
			this.loginInfo.pwd = this.loginInfo.pwd.trim();
			axios({
				method: "POST",
				url: "/api/usr/login",
				data: this.loginInfo, //明文传输，不管了XD
			})
				.then((value) => {
					if (value.data.code == 200) {
						if (value.data.data.isAdmin == 1) {
							this.$store.commit("loginAsAdmin", {
								token: value.headers["token"],
								usr: value.data.data.usr,
							});
							this.$router.push("/admin/t0");
						} else {
							this.$store.commit("loginAsCust", {
								token: value.headers["token"],
								usr: value.data.data.usr,
							});
							this.$router.push("/cust/t0");
						}
					} else {
						console.log(value);
					}
				})
				.catch((error) => {
					if (error.response.status == 500) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else if (error.response.status == 403) {
						this.snackbarMessage = "用户名或密码错误";
					} else {
						this.snackbarMessage = "未知错误";
					}
					this.snackbar = true;
					this.inputError = true;
					this.inputErrorMessage = "请重试";
					return;
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
			axios({
				method: "POST",
				url: "/api/usr/insert-in-login-page",
				data: this.regisInfo, //明文传输，不管了XD
			})
				.then((value) => {
					if (value.data.code == 200) {
						this.snackbar = true;
						this.snackbarMessage = "访客注册成功！";
						this.regisInfo = {
							usr: null,
							pwd: null,
						};
						this.registerDialog = false;
					} else {
						this.regisInputError = true;
						this.regisInputErrorMessage = "请重试";
						this.snackbar = true;
						this.snackbarMessage = value.data.message;
						return;
					}
				})
				.catch((error) => {
					if (error.response.status == 500) {
						this.snackbarMessage = "错误代码500：服务器错误";
					} else {
						this.snackbarMessage = "未知错误";
					}
					this.snackbar = true;
					this.inputError = true;
					this.inputErrorMessage = "请重试";
				});
		},
		closeRegisterDialog() {
			this.clearRegisInputError();
			this.registerDialog = false;
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
		egg() {
			this.eggCount++;
			if (this.eggCount == 1) {
				this.snackbar = true;
				this.snackbarMessage = "好像出问题了...再戳一次试试？";
			} else if (this.eggCount == 2) {
				this.snackbar = false;
				setTimeout(() => {
					this.snackbar = true;
					this.snackbarMessage = "还是不行，要不再戳一次吧";
				}, 100);
			} else if (this.eggCount == 3) {
				this.snackbar = false;
				setTimeout(() => {
					this.snackbar = true;
					this.snackbarMessage = "嗯...多戳几次吧，一定会成功的";
				}, 100);
			} else if (this.eggCount == 10) {
				this.eggDialog = true;
				setTimeout(() => {
					this.isEggDialogCloseBtnDisabled = false;
				}, 36000);
			}
		},
		closeEggDialog() {
			this.eggDialog = false;
			this.eggCount = 0;
			this.isEggDialogCloseBtnDisabled = true;
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

			<v-dialog v-model="registerDialog" max-width="500px" persistent>
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
						<v-row no-gutters class="pr-4 pb-4" justify="end">
							<v-col cols="auto" class="mr-3">
								<v-btn
									size="large"
									variant="elevated"
									color="blue"
									@click="closeRegisterDialog"
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

			<v-dialog
				v-model="eggDialog"
				persistent
				transition="dialog-bottom-transition"
				fullscreen
				theme="dark"
				style="user-select: none"
			>
				<v-card>
					<v-card-title class="ml-4 mt-6 text-h3 font-weight-bold text-center">
						🐧🐧🐧高松灯来咯🐧🐧🐧高松灯来咯🐧🐧🐧
					</v-card-title>
					<v-card-text class="text-h6 text-center" style="line-height: 1.8">
						<p>
							遇到问题？不准遇到问题！😡😡😡我写的网页我说了算，你就算有问题也得没问题！😡😡😡
						</p>
						<p>罚你看凑捡石头的唱的灵感菇😋😋😋</p>
						<video
							src="/tomorin.mp4"
							autoplay
							height="600px"
							style="border-radius: 30px"
						></video>
					</v-card-text>
					<v-card-actions>
						<v-row no-gutters class="mr-8 mb-8" justify="end">
							<v-col cols="auto">
								<div>
									<v-btn
										size="x-large"
										variant="elevated"
										color="red"
										@click="closeEggDialog"
										:disabled="isEggDialogCloseBtnDisabled"
										>关闭
									</v-btn>
									<v-tooltip activator="parent" location="start"
										>没看完不准关掉😋😋😋</v-tooltip
									>
								</div>
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
											src="/login-wrapper-img.jpeg"
										>
											<v-card-title
												class="mt-8 text-center text-h4 text-white font-weight-black"
												v-text="'长江测站信息数据中台系统'"
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
													@click="registerDialog = true"
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
										<v-row
											justify="end"
											style="
												position: relative;
												bottom: -80px;
												color: #2196f3;
												text-decoration: underline;
											"
										>
											<v-btn variant="text" @click="egg">遇到问题？戳我</v-btn>
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
	background-image: url("/login-back-img.jpeg");
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
