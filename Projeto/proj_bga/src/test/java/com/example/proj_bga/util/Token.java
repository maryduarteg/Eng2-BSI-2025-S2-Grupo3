package com.example.proj_bga.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDate;
import java.util.Date;

public class Token {
    private static String secretKey = "Dgirg25453ND&$*e6aNKFGfwGdJIjD123";

    public static String gerarToken(String usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String token = JWT.create()
                .withSubject(usuario)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(algorithm);

        return token;
    }
}
