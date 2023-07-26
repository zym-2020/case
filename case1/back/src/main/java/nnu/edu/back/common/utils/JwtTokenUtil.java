package nnu.edu.back.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.pojo.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/22:51
 * @Description:
 */
public class JwtTokenUtil {
    private static String secret = "secret";
    private static long expiration = 60*60*24*4;
    private static long failExpiration = 60*60*24*3;        //超期后再过三天

    /***
    * @Description:负责生成token
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /***
    * @Description:生成token过期时间
    * @Author: Yiming
    * @Date: 2023/7/25
    */

    private static Date generateExpirationDate() {
        Date date = new Date(System.currentTimeMillis() + expiration * 1000);
        return date;
    }

    /***
    * @Description:解析token
    * @Author: Yiming
    * @Date: 2023/7/25
    */

    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }
    /**
    * @Description:判断token刷新还是彻底过期,0为彻底过期，1为刷新token
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    public static int tokenStatus(String token) {
        Claims claims = getClaimsFromToken(token);
        if(claims == null) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
        Date expiration = claims.getExpiration();
        if(new Date(expiration.getTime() + failExpiration  * 1000).before(new Date())) {
            return 0;
        } else {
            return 1;
        }
    }
    /***
    * @Description:验证token是否失效
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    public static boolean validateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if(claims == null) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
        return claims.getExpiration().before(new Date());
    }

    /***
    * @Description:根据用户信息生成token
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    public static String generateTokenByUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return generateToken(claims);
    }

    /***
    * @Description:刷新token
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    public static String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if(claims == null) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", claims.get("id"));
        map.put("name", claims.get("name"));
        map.put("email", claims.get("email"));
        map.put("role", claims.get("role"));
        return generateToken(map);
    }

    /**
    * @Description:根据token获取用户信息
    * @Author: Yiming
    * @Date: 2023/7/25
    */
    public static Object getUserInfoByToken(String key, String token) {
        Claims claims = getClaimsFromToken(token);
        if(claims == null) {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }
        return claims.get(key);
    }
}
