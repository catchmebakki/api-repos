package com.ssi.ms.handler;

import com.ssi.service.core.platform.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JWTHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getJWT(final HttpServletRequest request) {

        return jwtTokenUtil.createJWT();
    }
}
