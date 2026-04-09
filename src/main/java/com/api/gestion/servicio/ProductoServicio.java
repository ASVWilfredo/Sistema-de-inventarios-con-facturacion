package com.api.gestion.servicio;

import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface ProductoServicio {
    ResponseEntity<String> agregarNuevoProducto(Map<String, String> requestMap);
    ResponseEntity<List<ProductoWrapper>> obtenerTodosProductos();
    ResponseEntity<String> modificarProducto(@RequestBody Map<String, String> requestMap);
    ResponseEntity<String> actualizarProductoCompleto(Integer id, Map<String, Object> requestMap);
    ResponseEntity<String> actualizarProductoParcial(Integer id, Map<String, Object> updates);
    ResponseEntity<String> eliminarProducto(Integer id);
    ResponseEntity<String> modificarSatus(Map<String, String> requestMap);
    ResponseEntity<List<ProductoWrapper>> clasificarPorCategoria(Integer id);
    ResponseEntity<ProductoWrapper> obtenerProductoPorId(Integer id);
}
