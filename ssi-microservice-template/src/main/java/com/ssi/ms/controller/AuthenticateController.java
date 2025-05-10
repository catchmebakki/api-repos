package com.ssi.ms.controller;

import com.ssi.ms.handler.JWTHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    @Autowired
    private JWTHandler jWTHandler;

    @GetMapping(path = "/login")
    public ResponseEntity<String> login(final HttpServletRequest request) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new StringJoiner(":", "{", "}")
                .add("\"id\"").add("\"" + jWTHandler.getJWT(request) + "\"")
                .toString());
    }
}
