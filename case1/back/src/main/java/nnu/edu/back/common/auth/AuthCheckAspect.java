package nnu.edu.back.common.auth;

import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.JwtTokenUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/23:21
 * @Description:
 */
@Aspect
@Component
public class AuthCheckAspect {
    private String tokenHead = "Bearer ";
    @Autowired
    HttpServletRequest request;

    @Pointcut("@annotation(nnu.edu.back.common.auth.AuthCheck)")
    public void check() {};

    @Around("check()")
    public Object aroundCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String authorization = request.getHeader("authorization");
        if(authorization != null) {
            String token = authorization.substring(tokenHead.length());
            if(JwtTokenUtil.validateToken(token)) {
                if(JwtTokenUtil.tokenStatus(token) == 1) {
                    JsonResult result = (JsonResult) joinPoint.proceed();
                    result.setRefreshToken(JwtTokenUtil.refreshToken(token));
                    return result;
                } else {
                    throw new MyException(ResultEnum.TOKEN_WRONG);
                }
            } else {
                return joinPoint.proceed();
            }
        } else {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
    }
}
