package com.api.gestion.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "usuarios")
@NamedQuery(name = "Usuario.findByEmail", query = "select u from Usuario u where u.email=:email")
@NamedQuery(name = "Usuario.getAllUsers", query = "select new com.api.gestion.wrapper.UsuarioWrapper(" +
        "u.id, u.nombre, u.email, u.numeroContacto, u.status) from Usuario u where u.rol='usuario'")
@NamedQuery(name= "Usuario.updateStatus", query = "update Usuario u set u.status=:status where u.id=:id")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numeroContacto")
    private String numeroContacto;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "rol")
    private String rol;
}
