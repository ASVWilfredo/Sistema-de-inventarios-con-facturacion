package com.api.gestion.servicio.implemento;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.UsuarioDAO;
import com.api.gestion.pojo.Usuario;
import com.api.gestion.seguridad.ConsumidorDetallesServicio;
import com.api.gestion.seguridad.jwt.JwtFiltro;
import com.api.gestion.seguridad.jwt.JwtUtil;
import com.api.gestion.servicio.UsuarioServicio;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.UsuarioWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UsuarioServicioImplemento implements UsuarioServicio {
    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ConsumidorDetallesServicio consumidorDetallesServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFiltro jwtFiltro;

    @Override
    public ResponseEntity<String> registrarse(Map<String, String> requestMap) {
        log.info("Registro interno de un usuario {}", requestMap);
        try {
            if(validarRegistroMapa(requestMap)){
                Usuario usuario = usuarioDAO.findByEmail(requestMap.get("email"));
                if(Objects.isNull(usuario)){
                    usuarioDAO.save(getUsuarioDesdeMap(requestMap));
                    return FacturaUtils.getResponseEntity("Usuario registrado con exito", HttpStatus.CREATED);
                } else {
                    return FacturaUtils.getResponseEntity(
                            "El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> iniciarSeccion(Map<String, String> requestMap) {
        log.info("Seccion inicializada");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if(authentication.isAuthenticated()){
                if(consumidorDetallesServicio.getUsuarioDetalle().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil.generarToken(consumidorDetallesServicio.
                            getUsuarioDetalle().getEmail(), consumidorDetallesServicio.getUsuarioDetalle()
                            .getRol()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"mensaje\":\""+" Espere la aprobacion del " +
                            "administrador "+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception exception) {
            log.error("{}", exception);
        }
        return new ResponseEntity<String>("{\"mensaje\":\""+" Credenciales incorrectas "+"\"}",HttpStatus.
                BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UsuarioWrapper>> getAllUsers() {
        try {
            if(jwtFiltro.esAdmin()) {
                return new ResponseEntity<>(usuarioDAO.getAllUsers(), HttpStatus.OK);
            } else  {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> modifcarStatus(Map<String, String> requestMap) {
        try {
            if(jwtFiltro.esAdmin()) {
                Optional<Usuario> opcionalUsuario = usuarioDAO.findById(Integer.parseInt(requestMap.get("id")));
                if(!opcionalUsuario.isEmpty()){
                    usuarioDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return FacturaUtils.getResponseEntity("Status del usuario ACTUALIZADO!", HttpStatus.OK);
                } else {
                    FacturaUtils.getResponseEntity("Este usuario no existe", HttpStatus.NOT_FOUND);
                }
            }  else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarRegistroMapa(Map<String, String> requestMap) {
        if(requestMap.containsKey("nombre") && requestMap.containsKey(
                "numeroContacto") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private Usuario getUsuarioDesdeMap(Map<String, String> requestMap) {
        Usuario usuario = new Usuario();
        usuario.setNombre(requestMap.get("nombre"));
        usuario.setNumeroContacto(requestMap.get("numeroContacto"));
        usuario.setEmail(requestMap.get("email"));
        usuario.setPassword(requestMap.get("password"));
        usuario.setStatus("false");
        usuario.setRol("usuario");
        return usuario;
    }
}
