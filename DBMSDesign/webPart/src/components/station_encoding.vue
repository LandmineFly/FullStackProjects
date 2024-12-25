<script>
import axios from 'axios'
export default {
    data: () => ({
        dialog: false,
        dialogDelete: false,
        loading: false,
        snackbar: false,
        snackbarMessage: null,
        rules: [
            value => {
                if (value) {
                    return true;
                }
                return '该项不能为空'
            }
        ],
        headers: [
            { title: '编码', key: 'iid' },
            { title: '测站类型', key: 'sttype' },
            { title: '对象名', key: 'iname' },
            { title: '拼音码', key: 'pyk' },
            { title: '测站号', key: 'stid' },
            { title: '备注', key: 'nt' },
            { title: '操作', key: 'actions', sortable: false },
        ],
        desserts: [],
        sttype: ['基础水文', '实时雨水情', '水质', '降水蒸发'],
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
                url: "/api/se/select",
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
                url: "/api/se/delete",
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
                    url: "/api/se/update",
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
                    url: "/api/se/insert",
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
        style="line-height: 50px;" hover @update:options="refresh">
        <template v-slot:top>
            <v-toolbar flat>
                <v-toolbar-title>测站信息化表</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn color="blue-darken-1" variant="flat" class="ma-2" @click="refreshBtn">
                    刷新
                </v-btn>
                <v-dialog v-model="dialog" max-width="800px" persistent>
                    <template v-slot:activator="{ props }">
                        <v-btn color="green-darken-1" variant="flat" class="ma-4" v-bind="props">
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
                                        <v-text-field v-model="editedItem.iid" label="编码" :disabled="editedIndex != -1"
                                            :hint="editedIndex != -1 ? '主键不能修改' : '输入范例：101.001 / 101.0001'"
                                            persistent-hint :rules="rules"></v-text-field>
                                    </v-col>
                                    <v-col cols="12" sm="6" md="4">
                                        <v-select v-model="editedItem.sttype" label="测站类型" :items="sttype"
                                            :disabled="editedIndex != -1" :hint="editedIndex != -1 ? '主键不能修改' : ''"
                                            persistent-hint :rules="rules"></v-select>
                                    </v-col>
                                    <v-col cols="12" sm="6" md="4">
                                        <v-text-field v-model="editedItem.iname" label="对象名"></v-text-field>
                                    </v-col>
                                    <v-col cols="12" sm="6" md="4">
                                        <v-text-field v-model="editedItem.pyk" label="拼音码"></v-text-field>
                                    </v-col>
                                    <v-col cols="12" sm="6" md="4">
                                        <v-text-field v-model="editedItem.stid" label="测站号"></v-text-field>
                                    </v-col>
                                    <v-col cols="12" sm="6" md="4">
                                        <v-text-field v-model="editedItem.nt" label="备注"></v-text-field>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-card-text>

                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue-darken-1" variant="flat" @click="close">
                                取消
                            </v-btn>
                            <v-btn color="green-darken-1" variant="flat" @click="save">
                                保存
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
                <v-dialog v-model="dialogDelete" max-width="300px" persistent>
                    <v-card>
                        <v-card-title class="text-h9 text-center">你确定要删除吗？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue-darken-1" variant="flat" @click="closeDelete">取消</v-btn>
                            <v-btn color="red-darken-1" variant="flat" @click="deleteItemConfirm">确认</v-btn>
                            <v-spacer></v-spacer>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-toolbar>
        </template>
        <template v-slot:item.actions="{ item }">
            <v-btn color="blue-darken-1" size="small" class="me-2" @click="editItem(item)">
                编辑
            </v-btn>
            <v-btn color="red-darken-1" size="small" @click="deleteItem(item)">
                删除
            </v-btn>
        </template>
    </v-data-table-server>
</template>

<style></style>