package nnu.edu.back.service;

import nnu.edu.back.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/23:31
 * @Description:
 */
public interface UserService {
    int register(User user);

    String login(User user);

    Map<String, Object> getUserInfo(String email);
}
