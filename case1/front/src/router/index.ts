import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";

const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/register",
    name: "register",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "@/views/RegisterView.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue"),
  },
  {
    path: "/404",
    name: "404",
    component: () => import("@/views/404.vue"),
  },
];

export const asyncRouters: Array<RouteRecordRaw> = [
  {
    path: "/userSpace",
    name: "userSpace",
    component: () => import("@/views/UserSpaceView.vue"),
    meta: {
      requiresAuth: "member",
    },
  },
  {
    path: "/admin",
    name: "admin",
    component: () => import("@/views/AdminView.vue"),
    meta: {
      requiresAuth: "admin",
    },
  },
  {
    path: "/:catchAll(.*)",
    name: "Redirect404",
    redirect: "/404",
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
});

export const resetRouters = () => {
  const newRouter = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes,
  });
  const removeList: string[] = [];
  router.getRoutes().forEach((item) => {
    for (let i = 0; i < newRouter.getRoutes().length; i++) {
      if (item.name === newRouter.getRoutes()[i].name) {
        return;
      }
    }
    removeList.push(item.name as string);
  });
  removeList.forEach((item) => {
    router.removeRoute(item);
  });
};

export default router;
