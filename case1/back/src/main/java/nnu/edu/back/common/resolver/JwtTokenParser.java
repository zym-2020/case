package nnu.edu.back.common.resolver;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/22:46
 * @Description:
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTokenParser {
    String value();
}
