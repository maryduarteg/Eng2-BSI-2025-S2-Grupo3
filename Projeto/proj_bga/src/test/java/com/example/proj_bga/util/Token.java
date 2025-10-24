package com.example.proj_bga.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.LocalDate;
import java.util.Date;

public class Token {
    private static String secretKey = "Dgirg25453ND&$*e6aNKFGfwGdJIjD123";

    public static String gerarToken(String usuario, int categariaUsuarioId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String token = JWT.create()
                .withSubject(usuario)
                .withClaim("categoria", categariaUsuarioId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(algorithm);

        return token;
    }

    public static boolean validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(token);

            int categoria = jwt.getClaim("categoria").asInt();

            //String usuario = jwt.getSubject();
            //System.out.println("Token válido para o usuário: " + usuario);

            return true;

        } catch (JWTVerificationException e) {
            //System.out.println("Token inválido ou expirado: " + e.getMessage());
            return false;
        }
    }
}
