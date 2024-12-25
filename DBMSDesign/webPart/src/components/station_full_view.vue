<script>
import axios from 'axios'
export default {
  data: () => ({
    dialog: false,
    dialogDelete: false,
    loading: false,
    snackbar: false,
    snackbarMessage: null,
    headers: [
      { title: '编码', key: '编码' },
      { title: '站名', key: '站名' },
      { title: '管理机构', key: '管理机构' },
      { title: '东经', key: '东经' },
      { title: '北纬', key: '北纬' },
      { title: '流域水系', key: '流域水系' },
      { title: '河流名称', key: '河流名称' },
      { title: '基础水文', key: '基础水文' },
      { title: '降水蒸发', key: '降水蒸发' },
      { title: '实时雨水情', key: '实时雨水情' },
      { title: '水质', key: '水质' },
      { title: '操作', key: 'actions', sortable: false },
    ],
    desserts: [],
    editedIndex: -1,
    editedItem: {
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
    defaultItem: {
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
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? '新建' : '编辑'
    },
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  created() {
    this.refresh()
  },

  methods: {
    refresh() {
      this.loading = true
      //刷新逻辑
      axios({
        method: "GET",
        url: "/api/sv/select",
        data: {}
      }).then(value => {
        if (value.data.code === 200) {
          this.desserts = value.data.data;
          this.loading = false
        } else {
          this.snackbarMessage = value.data.message
          this.snackbar = true
          this.loading = true
        }
      })
    },

    refreshBtn() {
      this.refresh()
      this.snackbarMessage = "刷新成功！"
      this.snackbar = true
    },

    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.desserts.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm() {
      //删除逻辑
      axios({
        method: "DELETE",
        url: "/api/sv/delete",
        data: this.editedItem
      }).then(value => {
        if (value.data.code === 200) {
          this.snackbarMessage = "删除成功！"
          this.snackbar = true
        } else {
          this.snackbarMessage = value.data.message
          this.snackbar = true
        }
        this.refresh();
      });
      this.closeDelete();
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save() {
      var isSuccess = 0;
      if (this.editedIndex > -1) {
        //修改逻辑
        axios({
          method: "PUT",
          url: "/api/sv/update",
          data: this.editedItem
        }).then(value => {
          if (value.data.code === 200) {
            this.snackbarMessage = "修改成功！"
            this.snackbar = true
            isSuccess = 1;
          } else {
            this.snackbarMessage = value.data.message
            this.snackbar = true
          }
          this.refresh();
          if (isSuccess == 1) {
            this.close()
          }
        })

      } else {
        //新建逻辑
        axios({
          method: "POST",
          url: "/api/sv/insert",
          data: this.editedItem
        }).then(value => {
          if (value.data.code === 200) {
            this.snackbarMessage = "添加成功！"
            this.snackbar = true
            isSuccess = 1;
          } else {
            this.snackbarMessage = value.data.message
            this.snackbar = true
          }
          this.refresh();
          if (isSuccess == 1) {
            this.close()
          }
        })
      }
      if (isSuccess === 1) {
        this.close()
      }
    },
  },
}

</script>

<template>
  <v-snackbar v-model="snackbar" :timeout="2000" location="top">
    {{ snackbarMessage }}
    <template v-slot:actions>
      <v-btn color="pink" variant="text" @click="snackbar = false; snackbarMessage = null">
        关闭
      </v-btn>
    </template>
  </v-snackbar>

  <v-data-table-server :headers="headers" :items="desserts" :items-length="desserts.length" :loading="loading"
    style="line-height: 50px;" hover @update:options="refresh" >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>测站总表</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn color="blue-darken-1" variant="flat" class="ma-2" @click="refreshBtn">
          刷新
        </v-btn>
        <div>
          <v-btn color="green-darken-1" variant="flat" class="ma-4" disabled>
            新建
          </v-btn>
          <v-tooltip activator="parent" location="start">视图不支持新建</v-tooltip>
        </div>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <span>
        <v-btn color="blue-darken-1" size="small" class="me-2" @click="editItem(item)" disabled>
          编辑
        </v-btn>
        <v-tooltip activator="parent" location="start">视图不支持编辑</v-tooltip>
      </span>
      <span>
        <v-btn color="red-darken-1" size="small" @click="deleteItem(item)" disabled>
          删除
        </v-btn>
        <v-tooltip activator="parent" location="start">视图不支持删除</v-tooltip>
      </span>
    </template>
  </v-data-table-server>
</template>

<style></style>