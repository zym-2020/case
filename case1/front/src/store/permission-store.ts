import { defineStore } from "pinia";
import { ref } from "vue";
import { RouteRecordRaw } from "vue-router";
import { getUserInfo } from "@/api/request";

import router, { resetRouters } from "@/router";

export const usePermissionStore = defineStore("permission", () => {
  const role = ref("");
  const email = ref("");
  const name = ref("");
  const dynamicRouters = ref<RouteRecordRaw[]>([]);

  const generateRouters = (asyncRouters: RouteRecordRaw[]) => {
    dynamicRouters.value = filterAsyncRoutes(asyncRouters, role.value);
  };

  const logout = () => {
    role.value = "";
    email.value = "";
    name.value = "";
    dynamicRouters.value = [];
    localStorage.removeItem("zymtoken");
    resetRouters();
    router.push({ path: "/login" });
  };

  const setUserInfo = async () => {
    const res = await getUserInfo();
    if (res && res.code === 0) {
      role.value = res.data.role;
      email.value = res.data.email;
      name.value = res.data.name;
    } else logout();
  };

  return {
    role,
    email,
    name,
    dynamicRouters,
    generateRouters,
    logout,
    setUserInfo,
  };
});

const filterAsyncRoutes = (asyncRouters: RouteRecordRaw[], role: string) => {
  const result: RouteRecordRaw[] = [];
  asyncRouters.forEach((router) => {
    const item = { ...router };
    if (hasPermission(item, role)) {
      if (item.children) {
        item.children = filterAsyncRoutes(item.children, role);
      }
      result.push(item);
    }
  });
  return result;
};

const hasPermission = (router: RouteRecordRaw, role: string) => {
  if (router.meta && router.meta.requiresAuth) {
    const auth: string = router.meta.requiresAuth as string;
    if (auth === "member" && (role === "member" || role === "admin"))
      return true;
    else if (auth === "admin" && auth === role) return true;
    else return false;
  }
  return true;
};
