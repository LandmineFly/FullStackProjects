<script>
import { Transition } from "vue";
import axios from "axios";
// 导入 assets 资源(@为项目根路径的默认别名，见vite.config.js)
// public下的资源无需导入，直接暴露给浏览器，适合静态资源
// assets 下的资源需要导入，且由构建工具(vite)进行处理，适合需经过构建处理的资源(如动态图片、CSS、字体)
import main_background_url from "@/assets/images/ksm_AI2X.webp";
import logo_url from "@/assets/images/icon_1_light_AI2X.webp";

import { timeMessage } from "@/data/timeMessage.js";

/*
 * 调试：要想跳过加载页面直接进入主页面，需要以下操作：
 * 1. 将data()的enterMain设置为true
 * 2. mounted()的then()中调用loadedFunc_bebug()
 */
export default {
	data() {
		return {
			// 加载完毕动画的状态
			loaded: {
				animation: {
					circle: false,
					text: false,
				},
				// 进入主界面
				enterMain: false,
				// // 调试：设置为true，直接进入主界面
				// enterMain: true,
			},
			// 需要在mounted()中提前加载的图像资源
			images: {
				mainBackground: "https://bing.ee123.net/img?imgtype=webp",
				logo: logo_url,
			},
			// main标签的overflow状态，进入主页面前需保持为hidden
			mainOverflow: "hidden",
			// 设置备案信息显示
			// TODO: 与vuex结合
			isShowBeiAnMessage: true,
			// 设置特殊组件的主题颜色（占位）
			theme: {
				light: {},
				dark: {},
				acg: {},
			},
			// 界面模式 & 模式切换(包括进入主界面)时的组件过渡动画设置
			// TODO: 与vuex结合
			mode: {
				name: "standard",
				transition: {
					// 默认都为move-out
					standard: "move-out-element",
					light: "move-out-element",
				},
			},
			time: {
				timer: null,
				currentDate: null,
				currentTime: null,
				currentHour: null,
				currentMessage: null,
				timeMessage,
			},
			weather: {
				data: null,
			},
		};
	},
	methods: {
		// 加载完毕函数
		loadedFunc() {
			// 设置圆圈和文字动画
			this.loaded.animation.circle = true;
			this.loaded.animation.text = true;

			// 1500为圆圈和文字动画的时间
			setTimeout(() => {
				// 设置进入主页面过渡动画
				this.loaded.enterMain = true;

				// 1400(600+800)为进入主页面过渡动画的总时间
				setTimeout(() => {
					// 进入主页面后设置main标签的overflow为auto
					this.mainOverflow = "auto";
					// 由于火狐的限制，主界面组件在进入主页面动画时既不渲染更没有动画
					// 因此必须在进入主页面后才设置过渡动画，否则会闪现
					var modeName = this.mode.name;
					this.mode.transition[modeName] = "move-in-element";
				}, 1400);
			}, 1500);
		},
		// 调试：加载完毕函数，直接进入主界面
		loadedFunc_debug() {
			// 设置圆圈和文字动画
			this.loaded.animation.circle = true;
			this.loaded.animation.text = true;

			this.loaded.enterMain = true;

			// 进入主页面后设置main标签的overflow为auto
			this.mainOverflow = "auto";
			// 由于火狐的限制，主界面组件在进入主页面动画时既不渲染更没有动画
			// 因此必须在进入主页面后才设置过渡动画，否则会闪现
			var modeName = this.mode.name;
			this.mode.transition[modeName] = "move-in-element";
		},
		updateTime() {
			const now = new Date();

			// 获取问候语，只在小时变化时更新
			const h = now.getHours();
			if (this.time.currentHour == null || this.time.currentHour != h) {
				// 每个阶段的问候语数量应该都是一致的，因此这里直接算morning的就好
				const len = this.time.timeMessage.morning.length;
				const randomIndex = Math.floor(Math.random() * len);

				if (h < 6) {
					this.time.currentMessage =
						this.time.timeMessage.midnight[randomIndex];
				} else if (h < 11) {
					this.time.currentMessage = this.time.timeMessage.morning[randomIndex];
				} else if (h < 13) {
					this.time.currentMessage = this.time.timeMessage.noon[randomIndex];
				} else if (h < 18) {
					this.time.currentMessage =
						this.time.timeMessage.afternoon[randomIndex];
				} else {
					this.time.currentMessage = this.time.timeMessage.night[randomIndex];
				}

				this.time.currentHour = h;
			}

			// 获取日期
			const weekMap = ["日", "一", "二", "三", "四", "五", "六"];
			const y = now.getFullYear();
			const m = String(now.getMonth() + 1).padStart(2, "0");
			const d = String(now.getDate()).padStart(2, "0");
			const w = `星期${weekMap[now.getDay()]}`;
			this.time.currentDate = `${y} 年 ${m} 月 ${d} 日 ${w}`;

			// 获取时间
			const hStr = String(h).padStart(2, "0");
			const min = String(now.getMinutes()).padStart(2, "0");
			const s = String(now.getSeconds()).padStart(2, "0");
			this.time.currentTime = `${hStr}:${min}:${s}`;
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
	mounted() {
		// 开始更新时间
		this.updateTime();
		this.timer = setInterval(() => {
			this.updateTime();
		}, 1000);

		const imageLoadPromises = Object.keys(this.images).map((key) => {
			return new Promise((resolve, reject) => {
				const image = new Image();
				// onload 和 onerror 都是事件处理器
				// 分别在图片加载成功和加载失败时触发
				image.onload = resolve;
				image.onerror = reject;
				// 设置图片的src，设置后图片就会开始加载
				image.src = this.images[key];
			}).catch((err) => {
				console.log("failed to load images: ", err);
			});
		});

		const weatherRequestPromise = axios
			.get(
				"http://api.weatherapi.com/v1/forecast.json?key=2b3ec309ac404d889a395235250407&q=Nanjing&days=3&aqi=yes&lang=zh"
			)
			.then((res) => {
				this.weather.data = res.data;
			})
			.catch((err) => {
				console.log(err);
			});

		// 在加载页面提前加载图片资源
		Promise.all([...imageLoadPromises, weatherRequestPromise])
			.then(() => {
				// 加载完毕
				this.loadedFunc();

				// // 调试：直接进入主界面
				// this.loadedFunc_debug();
			})
			.catch((err) => console.log("error: ", err));
	},
	beforeDestroy() {
		// 离开页面时停止更新时间，防止内存泄漏
		clearInterval(this.timer);
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
					style="align-content: center; justify-content: center"
					v-if="!loaded.enterMain"
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
							margin-top: 100px;
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
				<!-- TODO: 添加视口适配（大工程），现在先将 min-width 设置为与 container 相同的 width -->
				<!-- 设想：视口过小时锁定为精简模式，可以减小适配工作量 -->
				<v-sheet
					style="
						width: 100%;
						height: 100%;
						background-size: cover;
						min-width: 1200px;
					"
					:style="'background-image: url(' + images.mainBackground + ');'"
					v-else-if="loaded.enterMain"
					class="d-flex align-center justify-center"
				>
					<!-- 标准模式 -->
					<v-container
						style="height: 1000px; width: 1200px"
						fluid
						class="pa-3"
						v-show="mode.name === 'standard'"
					>
						<!-- 上半部分 -->
						<!-- no-gutters去掉v-row的负外边距，v-col的内边距 -->
						<!-- byd为什么会默认有负外边距啊，铸币吧 -->
						<v-row
							class="flex-column"
							no-gutters
							justify="center"
							align="center"
							style="width: 100%; height: 50%"
						>
							<!-- 所有需要过渡动画的组件都需要添加transition-element类 -->
							<!-- mode.transition.standard 为组件设置进入或退出过渡动画 -->
							<!-- 当前所有组件都放完退出过渡动画后，外层的containner才能v-if=false，反之同理 -->

							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 50%"
							>
								<v-sheet
									style="width: 95%; height: 90%"
									class="pa-3 rounded-xl glass-style d-flex align-center justify-start"
								>
									<!-- 传说中的布局地狱 -->
									<v-container>
										<v-row no-gutters>
											<v-col cols="4">
												<v-img :src="images.logo" class="pr-3"></v-img>
											</v-col>
											<v-col cols="8" align-self="center">
												<v-row>
													<v-col cols="12">
														<p
															style="
																font-family: 'Pacifico' !important;
																font-size: 55px;
																text-align: center;
															"
														>
															LandmineFly
														</p>
													</v-col>
												</v-row>
												<v-row justify="space-between">
													<v-col cols="auto">
														<p
															style="
																font-weight: 100;
																font-size: 30px;
																text-align: start;
															"
															class="pl-3"
														>
															云履循光
														</p>
													</v-col>
													<v-col cols="auto">
														<p
															style="
																font-family: 'Pacifico' !important;
																font-size: 35px;
																text-align: end;
															"
															class="pr-6"
														>
															.top
														</p>
													</v-col>
												</v-row>
											</v-col>
										</v-row>
									</v-container>
								</v-sheet>
							</v-col>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 50%"
							>
								<v-sheet
									class="rounded-xl glass-style d-flex flex-column align-center justify-center"
									style="width: 95%; height: 90%"
								>
									<p class="pt-3" style="font-size: 25px; font-weight: 300">
										{{ time.currentDate }}
									</p>
									<p
										class="pt-3"
										style="
											font-family: 'DS-Digital' !important;
											font-size: 90px;
											font-weight: 300;
										"
									>
										{{ time.currentTime }}
									</p>
									<p class="pt-3" style="font-size: 18px; font-weight: 300">
										{{ time.currentMessage }}
									</p>
								</v-sheet>
							</v-col>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 100%"
							>
								<v-sheet
									class="rounded-xl glass-style"
									style="width: 95%; height: 95%"
								></v-sheet>
							</v-col>
						</v-row>
						<v-row
							class="flex-column"
							no-gutters
							justify="center"
							align="center"
							style="width: 100%; height: 50%"
						>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 100%"
							>
								<v-sheet
									class="rounded-xl glass-style"
									style="width: 95%; height: 95%"
								></v-sheet>
							</v-col>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 40%"
							>
								<v-sheet
									class="rounded-xl glass-style"
									style="width: 95%; height: 90%"
								></v-sheet>
							</v-col>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 40%"
							>
								<v-sheet
									class="rounded-xl glass-style"
									style="width: 95%; height: 90%"
								></v-sheet>
							</v-col>
							<v-col
								cols="auto"
								class="transition-element d-flex align-center justify-center"
								:class="[mode.transition.standard]"
								style="width: 50%; height: 17%"
							>
								<v-sheet
									class="rounded-xl glass-style"
									style="width: 95%; height: 90%"
								></v-sheet>
							</v-col>
						</v-row>
					</v-container>
				</v-sheet>
			</Transition>

			<!-- 底部备案信息 -->
			<div
				style="
					width: 100%;
					position: absolute;
					bottom: 0px;
					color: white;
					opacity: 0.6;
					background-color: rgba(0, 0, 0, 0.6);
				"
				v-if="isShowBeiAnMessage"
			>
				<!-- 把container自带的padding去掉 -->
				<v-container style="padding: 0px">
					<v-row justify="center" align="center" no-gutters>
						<v-col cols="auto">
							<v-btn
								@click="open_stupid_bei_an_website(0)"
								variant="text"
								size="x-small"
							>
								粤公网安备44180202000801
								<template v-slot:prepend>
									<img src="/gong_an_head_icon.webp" width="10" />
								</template>
							</v-btn>
						</v-col>
						<v-col cols="auto">
							<v-btn
								@click="open_stupid_bei_an_website(1)"
								variant="text"
								size="x-small"
							>
								粤ICP备2025384677号-1
							</v-btn>
						</v-col>
						<v-col cols="auto">
							<v-btn
								@click="isShowBeiAnMessage = !isShowBeiAnMessage"
								variant="text"
								size="x-small"
							>
								关闭
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

/* 以下为加载完毕后的动画过程，共1.5s（--代表0.1s）
   圆圈动画
   ------------| 0.6s
					   ------------| 等待0.6s后，执行进入主页动画
   ------|------------| 共0.9s
   文字过渡 文字动画
*/

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

/* 进入首页过渡动画，共1.4s */
.loaded-leave-active {
	animation: puff-out-center 0.6s cubic-bezier(0.55, 0.085, 0.68, 0.53) both;
}

.loaded-enter-active {
	animation: scale-in-center 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}

@keyframes puff-out-center {
	0% {
		transform: scale(1);
		filter: blur(0px);
		opacity: 1;
	}
	100% {
		transform: scale(2);
		filter: blur(5px);
		opacity: 0;
	}
}

@keyframes scale-in-center {
	0% {
		transform: scale(0);
		filter: blur(10px);
		opacity: 0;
		border-radius: 50%;
	}
	100% {
		transform: scale(1);
		filter: blur(0px);
		opacity: 1;
	}
}

.glass-style {
	background: rgba(0, 0, 0, 0.3) !important;
	backdrop-filter: blur(10px) !important;
	box-shadow: 0 8px 32px rgba(0, 0, 0, 0.6) !important;
	border: 1px solid rgba(255, 255, 255, 0.1) !important;
}

/* 所有需要过渡动画的组件都需要添加这个类 */
.transition-element {
	transition: all 0.5s ease;
}

/* 组件过渡进入动画 */
.move-in-element {
	opacity: 1;
	transform: scale(1);
}

/* 组件过渡退出动画 */
.move-out-element {
	opacity: 0;
	transform: scale(0.6);
}
</style>
