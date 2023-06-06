package com.dss.realworld.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dss.realworld.common.auth.LoginUser;
import com.dss.realworld.common.error.exception.UserNotFoundException;
import com.dss.realworld.user.domain.User;
import com.dss.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProcess {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public String create(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject(loginUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    public LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        String email = decodedJWT.getSubject();
        User foundUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return new LoginUser(foundUser);
    }
}