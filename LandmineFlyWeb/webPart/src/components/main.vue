<script>
export default {
	methods: {
		打开傻逼备案网站(value) {
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
		cardCols() {
			if (this.$vuetify.display.smAndDown) {
				return 10;
			} else {
				return 6;
			}
		},
		btnCols() {
			if (this.$vuetify.display.smAndDown) {
				return 6;
			} else {
				return 4;
			}
		},
		titleFontSize() {
			if (this.$vuetify.display.smAndDown) {
				return { "text-h5": true };
			} else {
				return { "text-h3": true };
			}
		},
	},
	// watch: {
	// 	"$vuetify.display.name"() {
	// 		console.log(this.$vuetify.display.name);
	// 	},
	// },
};
</script>

<template>
	<v-app>
		<v-main>
			<v-container fluid class="bg-grey-darken-4 fill-height">
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
								style="font-family: 'SimSun', STSong, 'Microsoft YaHei', serif"
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
												<v-img class="mt-5" height="60px" src="/bilibili.webp">
												</v-img>
												<p class="ma-2 mt-4 text-subtitle-1">作者的Bilibili</p>
											</v-card>
										</v-col>
									</v-row>
								</v-container>
							</v-card-text>
						</v-card>
					</v-col>
				</v-row>
			</v-container>

			<div style="width: 100%; position: absolute; bottom: 0; color: grey">
				<v-container>
					<v-row justify="center" no-gutters>
						<v-col cols="auto">
							<v-btn @click="打开傻逼备案网站(0)" variant="text" size="small">
								粤公网安备44180202000801
								<template v-slot:prepend>
									<img src="/gong_an_head_icon.png" width="12" />
								</template>
							</v-btn>
						</v-col>
						<v-col cols="auto">
							<v-btn @click="打开傻逼备案网站(1)" variant="text" size="small">
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
.gradient-color-text {
	background: linear-gradient(45deg, #dcffbd, #cc86d1);
	background-clip: text;
	-webkit-background-clip: text;
	color: transparent;
}
</style>
