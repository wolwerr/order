package org.test.order.application.controllers.AutenticateJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.infra.dependecy.JWT.JwtTokenUtil;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/token")
    public String generateToken( ) {
        return jwtTokenUtil.generateToken();
    }

    // Endpoint para validar o token JWT
    @PostMapping("/auth/validate-token")
    public boolean validateToken(@RequestBody String token) {
        return jwtTokenUtil.validateToken(token);
    }
}
