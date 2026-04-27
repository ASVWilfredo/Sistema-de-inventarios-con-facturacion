package com.api.gestion.rest;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.pojo.Producto;
import com.api.gestion.servicio.ProductoServicio;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductoControlador {
    @Autowired
    private ProductoServicio productoServicio;

    @PostMapping("/add")
    public ResponseEntity<String> agregarNuevoProducto(@RequestBody Map<String,String> requestMap) {
        try {
            return productoServicio.agregarNuevoProducto(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductoWrapper>> listarProductos(){
        try {
            return productoServicio.obtenerTodosProductos();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> actualizarProducto(@RequestBody Map<String, String> requestMap) {
        try {
            return productoServicio.modificarProducto(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/fullUpdate/{id}")
    public ResponseEntity<String> actualizarProductoCompleto(@PathVariable Integer id, @RequestBody Map<String, Object> requestMap) {
        try {
            return productoServicio.actualizarProductoCompleto(id, requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<String> actualizarProductoParcial(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        try {
            return productoServicio.actualizarProductoParcial(id, updates);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        try {
            return productoServicio.eliminarProducto(id);
        }  catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<String> modificarStatusProducto(@RequestBody Map<String,String> requestMap) {
        try {
            return productoServicio.modificarSatus(requestMap);
        }  catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/orderByCategory/{id}")
    public ResponseEntity<List<ProductoWrapper>> listarProductosXCategoria(@PathVariable Integer id) {
        try {
            return productoServicio.clasificarPorCategoria(id);
        }  catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductoWrapper> buscarProductoPorId(@PathVariable Integer id) {
        try {
            return productoServicio.obtenerProductoPorId(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ProductoWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
