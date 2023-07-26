import router, { asyncRouters, resetRouters } from "./router";
import NProgress from "nprogress";
import { usePermissionStore } from "@/store/permission-store";
import { getToken } from "./utils/common";

NProgress.configure({ showSpinner: false });
router.beforeEach(async (to, form, next) => {
  const permissionStore = usePermissionStore();
  NProgress.start();
  if (getToken()) {
    if (permissionStore.role === "") {
      await permissionStore.setUserInfo();
      permissionStore.generateRouters(asyncRouters);
      resetRouters();
      permissionStore.dynamicRouters.forEach((item) => {
        router.addRoute(item);
      });
      next({ ...to, replace: true });
    } else {
      if (to.path === "/login" || to.path === "/register") {
        next({ path: "/" });
      } else next();
    }
  } else {
    if (to.path === "/login" || to.path === "/register" || to.path === "/") {
      next();
    } else {
      next("/login");
    }
  }
  NProgress.done();
});
