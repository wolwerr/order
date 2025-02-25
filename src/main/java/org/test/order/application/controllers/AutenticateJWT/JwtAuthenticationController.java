package org.test.order.application.controllers.AutenticateJWT;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.infra.dependecy.JWT.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/token")
    public String generateToken(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!jwtTokenUtil.validateAuthorizationHeader(authorizationHeader)) {
            throw new SecurityException("Invalid API Key");
        }
        return jwtTokenUtil.generateToken();
    }

    @PostMapping("/auth/validate-token")
    public boolean validateToken(@RequestBody String token) {
        return jwtTokenUtil.validateToken(token);
    }
}