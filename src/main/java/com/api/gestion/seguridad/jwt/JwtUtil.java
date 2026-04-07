package com.api.gestion.seguridad.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String claveSecreta;

    private SecretKey obtenerClave() {
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }

    public String extraerNombreUsuario(String token) {
        return extraerClaims(token, Claims::getSubject);
    }

    public Date fechaExpiracion(String token) {
        return extraerClaims(token, Claims::getExpiration);
    }
    public <T> T extraerClaims(String token, Function<Claims, T> claimsResultas) {
        final Claims claims = extraerTodosClaims(token);
        return claimsResultas.apply(claims);
    }

    public Claims extraerTodosClaims(String token) {
        SecretKey key = obtenerClave();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean esTokenExpirando(String token) {
        return fechaExpiracion(token).before(new Date());
    }

    public String generarToken(String nombreUsuario, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        return crearToken(claims, nombreUsuario);
    }

    private String crearToken(Map<String, Object> claims, String subject) {
        SecretKey key = obtenerClave();
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(
                new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()
                + 100 * 60 * 60 * 10)).signWith(key).compact();
    }

    public Boolean validarToken(String token, UserDetails userDetails) {
        final String nombreUsuario = extraerNombreUsuario(token);
        return (nombreUsuario.equals(userDetails.getUsername()) && !esTokenExpirando(token));
    }
}
