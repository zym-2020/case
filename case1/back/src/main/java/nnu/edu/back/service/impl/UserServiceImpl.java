package nnu.edu.back.service.impl;

import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.Encrypt;
import nnu.edu.back.dao.UserMapper;
import nnu.edu.back.pojo.User;
import nnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
