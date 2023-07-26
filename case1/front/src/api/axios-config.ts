import axios, {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";

import { notice, setToken, getToken } from "@/utils/common";
import { usePermissionStore } from "@/store/permission-store";

import { ResponseType } from "@/type";

const requestList = new Set();

const axiosInstance: AxiosInstance = axios.create({
  baseURL: "/case/",
  timeout: 200000,
});

axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    const permissionStore = usePermissionStore();
    if (response.data.code === -4 || response.data.code === -5) {
      permissionStore.logout();
      return response.data;
    }
    if (
      response.data.refreshToken != null &&
      response.data.refreshToken != ""
    ) {
      setToken(response.data.refreshToken);
    }
    setTimeout(() => {
      requestList.delete(response.config.url + response.config.data);
    }, 600); //请求间隔600ms
    return response.data;
  },
  (err: AxiosResponse) => {
    if (axios.isCancel(err)) {
      console.log(err);
      notice("warning", "警告", "操作过于频繁");
      return null;
    } else {
      notice("error", "错误", "请求错误");
      requestList.delete(err.config.url + err.config.data);
      return err.data;
    }
  }
);

axiosInstance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getToken();
  const flag = config.headers["debounce"];
  (config.headers.Authorization = `Bearer ${token}`),
    (config.cancelToken = new axios.CancelToken((e) => {
      const cancelRequest = () => {
        let url: string = (config.baseURL as string) + config.url;
        e(url);
      };

      if (flag === "true") {
        requestList.has(config.url + JSON.stringify(config.data))
          ? cancelRequest()
          : requestList.add(config.url + JSON.stringify(config.data));
      }
    }));
  return config;
});

export const get = (
  url: string,
  debounce: boolean,
  params?: any
): Promise<ResponseType | null> => {
  return axiosInstance.get(url, {
    headers: {
      debounce: debounce ? "true" : "false",
    },
    params: params,
  });
};

export const post = (
  url: string,
  debounce: boolean,
  data?: any
): Promise<ResponseType | null> => {
  return axiosInstance.post(url, data, {
    headers: {
      debounce: debounce ? "true" : "false",
    },
  });
};

export const del = (
  url: string,
  debounce: boolean
): Promise<ResponseType | null> => {
  return axiosInstance.delete(url, {
    headers: {
      debounce: debounce ? "true" : "false",
    },
  });
};

export const patch = (
  url: string,
  debounce: boolean,
  data?: any
): Promise<ResponseType | null> => {
  return axiosInstance.patch(url, data, {
    headers: {
      debounce: debounce ? "true" : "false",
    },
  });
};
