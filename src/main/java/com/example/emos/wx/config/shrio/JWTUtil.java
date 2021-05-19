package com.example.emos.wx.config.shrio;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 齐春晖
 * @date Created in 18:53 2021/5/5
 * @description
 */
@Component
@Slf4j
public class JWTUtil {
    @Value("${emos.jwt.secret}")
    private String secret;
    @Value("${emos.jwt.expire}")
    private int expire;

    /**
     * 通过UserId，创建一个token
     * @param userId
     * @return
     */
    public String createToken(int userId){

        DateTime offset = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire);
        Algorithm algorithm = Algorithm.HMAC256(secret);//使用算法加密secret密钥
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId", userId)
                              .withExpiresAt(offset)
                              .sign(algorithm);
       return token;

    }

    /**
     * 通过token获取userId
     * @param token
     * @return
     */
    public int getUserId(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        int userId = decodedJWT.getClaim("userId").asInt();
        return userId;
    }

    /**
     *验证密钥
     * @param token
     */
    public void verifierToken(String token){
        Algorithm algorithm =Algorithm.HMAC256(secret);//对密钥进行加密算法处理
        JWTVerifier verifier = JWT.require(algorithm).build();//创建一个jwt的verifier对象
        verifier.verify(token);//验证密钥和token（加密后的密钥部分）是否一致---sign
    }



}
