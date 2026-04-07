package com.api.gestion.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioWrapper {
    private Integer id;
    private String nombre;
    private String email;
    private String numeroContacto;
    private String status;
}
