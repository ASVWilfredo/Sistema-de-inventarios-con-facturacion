package com.api.gestion.servicio;

import com.api.gestion.wrapper.UsuarioWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UsuarioServicio {
    ResponseEntity<String> registrarse(Map<String, String> requestMap);
    ResponseEntity<String> iniciarSeccion(Map<String, String> requestMap);
    ResponseEntity<List<UsuarioWrapper>> getAllUsers();
    ResponseEntity<String> modifcarStatus(Map<String, String> requestMap);
}
