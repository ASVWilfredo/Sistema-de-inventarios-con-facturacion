package com.api.gestion.dao;

import com.api.gestion.pojo.Producto;
import com.api.gestion.wrapper.ProductoWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDAO extends JpaRepository<Producto, Integer> {
    List<ProductoWrapper> obtenerTodosProductos();

    @Modifying
    @Transactional
    Integer modificarStatus(@Param("status") String status, @Param("id") Integer id);
    List<ProductoWrapper> getProductosXCategoria(@Param("id") Integer id);
    ProductoWrapper getProductoPorId(@Param("id")  Integer id);
}
