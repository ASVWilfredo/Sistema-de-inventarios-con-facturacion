package com.api.gestion.seguridad.jwt;

import com.api.gestion.seguridad.ConsumidorDetallesServicio;
import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFiltro extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ConsumidorDetallesServicio consumidorDetallesServicio;

    private String nombreUsuario = null;

    Claims claims = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse
            response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches(
                "/usuario/iniciarSeccion|/usuario/olvidePassword|/usuario/registrar")) {
            filterChain.doFilter(request, response);
        } else {
            String autorizacionCabecero = request.getHeader("Authorization");
            String token = null;
            if(autorizacionCabecero != null && autorizacionCabecero.startsWith("Bearer ")) {
                token = autorizacionCabecero.substring(7);
                nombreUsuario = jwtUtil.extraerNombreUsuario(token);
                claims = jwtUtil.extraerTodosClaims(token);
            }
            if(nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = consumidorDetallesServicio.loadUserByUsername(nombreUsuario);
                if(jwtUtil.validarToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken
                            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public Boolean esAdmin() {
        if (claims == null) {
            return false;
        }
        return "admin".equalsIgnoreCase((String) claims.get("rol"));
    }

    public Boolean esUsuario() {
        if (claims == null) {
            return false;
        }
        return "usuario".equalsIgnoreCase((String) claims.get("rol"));
    }

    public String getUsuarioActual() {
        return nombreUsuario;
    }
}
