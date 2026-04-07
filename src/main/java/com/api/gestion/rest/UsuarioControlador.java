package com.api.gestion.rest;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.pojo.Usuario;
import com.api.gestion.servicio.UsuarioServicio;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.UsuarioWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return usuarioServicio.registrarse(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/iniciarSeccion")
    public ResponseEntity<String> iniciarSeccion(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return usuarioServicio.iniciarSeccion(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<UsuarioWrapper>> listarUsuarios() {
        try {
            return usuarioServicio.getAllUsers();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<UsuarioWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/modificar")
    public ResponseEntity<String> modificarUsuario(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return usuarioServicio.modifcarStatus(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
