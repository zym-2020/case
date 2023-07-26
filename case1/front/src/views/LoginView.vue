<template>
  <div>
    <el-form label-width="100px" :model="loginForm" style="max-width: 460px">
      <el-form-item label="邮箱">
        <el-input v-model="loginForm.email" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="loginForm.password" />
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="loginClick">登录</el-button>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";
import { login } from "@/api/request";
import { setToken } from "@/utils/common";
import router from "@/router";
export default defineComponent({
  setup() {
    const loginForm = reactive({
      email: "",
      password: "",
    });

    const loginClick = async () => {
      const res = await login(loginForm);
      if (res && res.code === 0) {
        setToken(res.data);
        router.push({ path: "/" });
      }
    };

    return { loginForm, loginClick };
  },
});
</script>

<style lang="scss" scoped>
</style>