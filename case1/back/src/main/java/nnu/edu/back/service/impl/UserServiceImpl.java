package nnu.edu.back.service.impl;

import io.jsonwebtoken.Claims;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.Encrypt;
import nnu.edu.back.common.utils.JwtTokenUtil;
import nnu.edu.back.dao.UserMapper;
import nnu.edu.back.pojo.User;
import nnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/23:32
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public int register(User user) {
        if (user.getEmail() == null || user.getName() == null || user.getPassword() == null) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        String email = userMapper.queryEmailByEmil(user.getEmail());
        if (email != null) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        user.setRole("member");
        user.setPassword(Encrypt.md5(user.getPassword()));
        userMapper.insertUser(user);
        return 1;
    }

    @Override
    public String login(User user) {
        if (user.getEmail() == null) throw new MyException(ResultEnum.NO_OBJECT);
        User resultUser = userMapper.queryUserByEmail(user.getEmail());
        if (resultUser.getPassword().equals(Encrypt.md5(user.getPassword()))) {
            return JwtTokenUtil.generateTokenByUser(resultUser);
        } else throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
    }

    @Override
    public Map<String, Object> getUserInfo(String email) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.queryUserByEmail(email);
        if (user == null) throw new MyException(ResultEnum.TOKEN_WRONG);
        map.put("email", user.getEmail());
        map.put("name", user.getName());
        map.put("role", user.getRole());
        return map;
    }
}
