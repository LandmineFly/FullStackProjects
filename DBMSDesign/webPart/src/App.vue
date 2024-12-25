<script>
export default {
  data: () => ({
    links: [
      ['测站总表', '1'],
      ['测站信息化表', '2'],
    ],
  }),
}
</script>

<template>
  <v-app>
    <v-navigation-drawer disable-resize-watcher permanent theme="dark" class="bg-deep-purple">
      <v-list-item title="长江测站信息管理系统" subtitle="Hohai University" style="margin: 30px 0;"></v-list-item>
      <v-divider></v-divider>
      <v-list density="compact" nav>
        <v-list-item v-for="[text, link] in links" :title="text" :to="link"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <div class="pa-5">
        <router-view></router-view>
      </div>
    </v-main>
  </v-app>
</template>

<style></style>
