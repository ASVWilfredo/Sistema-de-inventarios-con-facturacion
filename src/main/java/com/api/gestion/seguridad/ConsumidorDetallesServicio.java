package com.api.gestion.seguridad;

import com.api.gestion.dao.UsuarioDAO;
import com.api.gestion.pojo.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class ConsumidorDetallesServicio implements UserDetailsService {
    @Autowired
    private UsuarioDAO  usuarioDAO;
    private Usuario usuarioDetalle;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loadUserByUsername {}", username);
        usuarioDetalle = usuarioDAO.findByEmail(username);
        if (!Objects.isNull(usuarioDetalle)) {
            return new org.springframework.security.core.userdetails.User(
                    usuarioDetalle.getEmail(), usuarioDetalle.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    public Usuario getUsuarioDetalle() {
        return usuarioDetalle;
    }
}
