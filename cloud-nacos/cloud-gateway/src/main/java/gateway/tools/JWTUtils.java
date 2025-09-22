package gateway.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtils
 *
 * @author yuez
 * @since 2023/4/14
 */
public class JWTUtils {
    private final static String SING="WISCOMSECREAT";
    public static String createToken(Map<String,String> payload, int expireTime){
        JWTCreator.Builder builder = JWT.create();
        Calendar instance = Calendar.getInstance();
        if(expireTime <= 0){
            instance.add(Calendar.HOUR,1);//默认1小时
        }else{
            instance.add(Calendar.SECOND,expireTime);
        }
        payload.forEach(builder::withClaim);
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
    }

    public static Map<String,Object> getTokenInfo(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        Map<String, Claim> claims = verify.getClaims();
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expired= dateTime.format(verify.getExpiresAt());
        Map<String,Object> m=new HashMap<>();
        claims.forEach((k,v)->m.put(k,v.asString()));
        m.put("exp",expired);
        return m;
    }

    public static void main(String[] args) {

    }
}
