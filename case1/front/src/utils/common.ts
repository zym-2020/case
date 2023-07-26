import { ElNotification } from "element-plus";

export function notice(
  type: "success" | "warning" | "info" | "error",
  title: string,
  msg: string
) {
  ElNotification({
    type: type,
    title: title,
    message: msg,
    offset: 100,
  });
}

export function getToken(): string | null {
  return localStorage.getItem("zymtoken");
}

export function setToken(token: string): void {
  localStorage.setItem("zymtoken", token);
}
