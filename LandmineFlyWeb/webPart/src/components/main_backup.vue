<script>
import { Transition } from "vue";

export default {
	data() {
		return {
			// 加载完毕的动画状态
			loaded: {
				animation: {
					circle: false,
					text: false,
				},
				// 调试用，直接进入主页面
				enterMain: false,
			},
			// main标签的overflow状态，进入主页面前需保持为hidden
			mainOverflow: "hidden",
		};
	},
	methods: {
		// 加载完毕函数，设置动画状态
		loadedFunc() {
			this.loaded.animation.circle = true;
			this.loaded.animation.text = true;
			setTimeout(() => {
				this.loaded.enterMain = true;
				// 进入主页面后设置main标签的overflow为auto
				// 1200为进入主页面过渡动画的总时间
				setTimeout(() => {
					this.mainOverflow = "auto";
				}, 1200);
			}, 1500);
		},
		// 打开备案网站
		open_stupid_bei_an_website(value) {
			if (value === 0) {
				window.open(
					"https://beian.mps.gov.cn/#/query/webSearch?code=44180202000801",
					"_blank"
				);
			} else if (value === 1) {
				window.open("https://beian.miit.gov.cn", "_blank");
			}
		},
	},
	computed: {
		// 根据视口大小动态返回卡片宽度
		cardCols() {
			if (this.$vuetify.display.smAndDown) {
				return 10;
			} else {
				return 6;
			}
		},
		// 根据视口大小动态返回按钮大小
		btnCols() {
			if (this.$vuetify.display.smAndDown) {
				return 6;
			} else {
				return 4;
			}
		},
		// 根据视口大小动态返回字体大小
		titleFontSize() {
			if (this.$vuetify.display.smAndDown) {
				return { "text-h5": true };
			} else {
				return { "text-h3": true };
			}
		},
	},
	mounted() {
		// 模拟加载
		setTimeout(() => {
			this.loadedFunc();
		}, 1000);
	},
	watch: {
		// // 视口大小改变超出阈值时输出视口类型（调试）
		// "$vuetify.display.name"() {
		// 	console.log(this.$vuetify.display.name);
		// },
	},
};
</script>

<template>
	<v-app>
		<v-main class="bg-grey-darken-4" :style="{ overflow: mainOverflow }">
			<Transition mode="out-in" name="loaded">
				<!-- 加载界面 -->
				<v-container
					fluid
					class="bg-grey-darken-4 fill-height"
					v-if="!loaded.enterMain"
					style="align-content: center; justify-content: center"
				>
					<v-row
						:class="{ 'circle-animation': loaded.animation.circle }"
						justify="center"
						align="center"
						style="width: 100%; transform: scale(1.8)"
					>
						<v-col cols="auto">
							<div class="spinner">
								<div class="spinner1"></div>
							</div>
						</v-col>
					</v-row>

					<v-row
						:class="{ 'text-animation': loaded.animation.text }"
						justify="center"
						align="center"
						style="
							width: 100%;
							transform: scale(1);
							margin-top: 80px;
							text-align: center;
						"
					>
						<v-col cols="auto">
							<!-- 注意：父级已经定义了一个Transition，其触发的是animation（动画） -->
							<!-- 这里定义的Transition，其触发的是transition（过渡） -->
							<!-- 此时要显式地传入 type 属性来告诉 Vue 需要关心哪种类型，否则过渡不会生效 -->
							<Transition type="transition" name="text-change" mode="out-in">
								<div class="loader" v-if="!loaded.animation.text">
									加载中...
								</div>
								<div class="loader" v-else-if="loaded.animation.text">
									加载完毕，欢迎
								</div>
							</Transition>
						</v-col>
					</v-row>
				</v-container>

				<!-- 主页面 -->
				<v-container
					fluid
					class="bg-grey-darken-4 fill-height"
					v-else-if="loaded.enterMain"
				>
					<v-row justify="center" align="center">
						<v-col :cols="cardCols">
							<v-card
								class="bg-grey-darken-4 rounded-xl"
								variant="flat"
								style="border: 2px solid #aca7c7"
							>
								<v-img
									class="mt-8"
									height="100"
									src="/LFweb_icon_rounded.webp"
								></v-img>
								<v-card-title
									:class="titleFontSize"
									class="text-center font-weight-black gradient-color-text"
									style="
										font-family: 'SimSun', STSong, 'Microsoft YaHei', serif;
									"
								>
									<p>LandmineFly.top</p>
									<p>锐意建设中!</p>
								</v-card-title>
								<v-card-text class="text-center">
									<v-container>
										<v-row justify="space-around">
											<v-col :cols="btnCols">
												<v-card
													class="rounded-lg"
													variant="tonal"
													height="160px"
													href="https://github.com/LandmineFly"
												>
													<v-img class="mt-5" height="60px" src="/github.webp">
													</v-img>
													<p class="ma-2 mt-4 text-subtitle-1">作者的Github</p>
												</v-card>
											</v-col>
											<v-col :cols="btnCols">
												<v-card
													class="rounded-lg"
													variant="tonal"
													height="160px"
													href="https://space.bilibili.com/219547577"
												>
													<v-img
														class="mt-5"
														height="60px"
														src="/bilibili.webp"
													>
													</v-img>
													<p class="ma-2 mt-4 text-subtitle-1">
														作者的Bilibili
													</p>
												</v-card>
											</v-col>
										</v-row>
									</v-container>
								</v-card-text>
							</v-card>
						</v-col>
					</v-row>
				</v-container>
			</Transition>

			<!-- 底部备案信息 -->
			<div style="width: 100%; position: absolute; bottom: 0; color: grey">
				<v-container>
					<v-row justify="center" no-gutters>
						<v-col cols="auto">
							<v-btn
								@click="open_stupid_bei_an_website(0)"
								variant="text"
								size="small"
							>
								粤公网安备44180202000801
								<template v-slot:prepend>
									<img src="/gong_an_head_icon.webp" width="12" />
								</template>
							</v-btn>
						</v-col>
						<v-col cols="auto">
							<v-btn
								@click="open_stupid_bei_an_website(1)"
								variant="text"
								size="small"
							>
								粤ICP备2025384677号-1
							</v-btn>
						</v-col>
					</v-row>
				</v-container>
			</div>
		</v-main>
	</v-app>
</template>

<style>
/* 加载界面旋转动画 */
.spinner {
	background-image: linear-gradient(#fc00ff, #00dbde);
	width: 100px;
	height: 100px;
	animation: spinning82341 1.2s linear infinite;
	text-align: center;
	border-radius: 50px;
	filter: blur(1px);
	box-shadow: 0px -5px 20px 0px #fc00ff, 0px 5px 20px 0px #00dbde;
}

.spinner1 {
	background-color: rgb(36, 36, 36);
	width: 100px;
	height: 100px;
	border-radius: 50px;
	filter: blur(10px);
}

@keyframes spinning82341 {
	to {
		transform: rotate(360deg);
	}
}

/* 加载界面字体动画 */
.loader {
	color: rgb(0, 0, 0);
	background: linear-gradient(to right, #2d60ec, #3ccfda);
	font-size: 30px;
	-webkit-text-fill-color: transparent;
	background-clip: text;
	animation: animate8345 9s linear infinite;
	font-weight: bold;
}

@keyframes animate8345 {
	0%,
	100% {
		filter: hue-rotate(0deg);
	}

	50% {
		filter: hue-rotate(360deg);
	}
}

/* 加载完毕圆圈动画 */
.circle-animation {
	animation: circle-animation 0.6s ease-in-out both;
}

@keyframes circle-animation {
	0% {
		opacity: 1;
	}
	100% {
		opacity: 0;
	}
}

/* 加载完毕文字动画 */
.text-animation {
	animation: text-animation 0.6s 0.3s ease-in-out both;
}

@keyframes text-animation {
	0% {
		transform: translateY(0);
	}
	100% {
		transform: translateY(-120px) scale(1.8);
	}
}

/* 加载完毕文字过渡 */
.text-change-enter-active,
.text-change-leave-active {
	transition: opacity 0.3s ease-in-out;
}

.text-change-enter-from,
.text-change-leave-to {
	opacity: 0;
}

/* 进入首页过渡动画 */
.loaded-leave-active {
	animation: puff-out-center 0.6s cubic-bezier(0.55, 0.085, 0.68, 0.53) both;
}

.loaded-enter-active {
	animation: puff-in-center 0.6s cubic-bezier(0.215, 0.61, 0.355, 1) both;
}

@keyframes puff-out-center {
	0% {
		transform: scale(1);
		filter: blur(0px);
		opacity: 1;
	}
	100% {
		transform: scale(2);
		filter: blur(4px);
		opacity: 0;
	}
}

@keyframes puff-in-center {
	0% {
		transform: scale(2);
		filter: blur(12px);
		opacity: 0;
	}
	100% {
		transform: scale(1);
		filter: blur(0px);
		opacity: 1;
	}
}

/* 首页渐变字体 */
.gradient-color-text {
	background: linear-gradient(45deg, #dcffbd, #cc86d1);
	background-clip: text;
	-webkit-background-clip: text;
	color: transparent;
}
</style>
