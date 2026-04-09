package com.api.gestion.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "productos")
@NamedQuery(name = "Producto.obtenerTodosProductos", query = "select new com.api.gestion.wrapper.ProductoWrapper(" +
        "p.id, p.nombre, p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre)from Producto p")
@NamedQuery(name = "Producto.modificarStatus", query = "update Producto p set p.status=:status where p.id=:id")
@NamedQuery(name = "Producto.getProductosXCategoria", query = "select new com.api.gestion.wrapper.ProductoWrapper(" +
        "p.id, p.nombre) from Producto p where p.categoria.id=:id and p.status='true'")
@NamedQuery(name = "Producto.getProductoPorId", query = "select new com.api.gestion.wrapper.ProductoWrapper(p.id, " +
        "p.nombre, p.descripcion, p.precio) from Producto p where p.id=:id")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_lf", nullable = false) // categoria_lf es llave foranea
    private Categoria categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "status")
    private String status;

}
