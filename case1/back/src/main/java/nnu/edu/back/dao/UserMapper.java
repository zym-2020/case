package nnu.edu.back.dao;

import nnu.edu.back.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/22:15
 * @Description:
 */
@Repository
public interface UserMapper {
    void insertUser(@Param("user") User user);

    String queryEmailByEmil(@Param("email") String email);

    User queryUserByEmail(@Param("email") String email);
}
