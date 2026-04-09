package com.api.gestion.servicio.implemento;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.ProductoDAO;
import com.api.gestion.pojo.Categoria;
import com.api.gestion.pojo.Producto;
import com.api.gestion.seguridad.jwt.JwtFiltro;
import com.api.gestion.servicio.ProductoServicio;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.ProductoWrapper;
import com.google.common.base.Strings;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoServicioImplemento implements ProductoServicio {
    @Autowired
    private ProductoDAO productoDAO;

    @Autowired
    private JwtFiltro jwtFiltro;

    @Override
    public ResponseEntity<String> agregarNuevoProducto(Map<String, String> requestMap) {
        try {
            if (!jwtFiltro.esAdmin()) {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,HttpStatus.UNAUTHORIZED);
            }
            if(validarProductoMap(requestMap, false)) {
                productoDAO.save(getProductoDeMap(requestMap, false));
                return FacturaUtils.getResponseEntity("Producto agregado con exito", HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS,HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> obtenerTodosProductos() {
        try {
            return new ResponseEntity<>(productoDAO.obtenerTodosProductos(),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> modificarProducto(Map<String, String> requestMap) {
        try {
            if (jwtFiltro.esAdmin()) {
                if(validarProductoMap(requestMap, true)) {
                    Optional<Producto> productoOptional = productoDAO.findById(Integer.parseInt(requestMap.get(
                            "productoId")));
                    if(!productoOptional.isEmpty()) {
                        Producto producto = getProductoDeMap(requestMap, true);
                        producto.setStatus(productoOptional.get().getStatus());
                        productoDAO.save(producto);
                        return FacturaUtils.getResponseEntity("Producto actualizado con exito", HttpStatus.OK);
                    } else {
                        return FacturaUtils.getResponseEntity("Ese producto no existe", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminarProducto(Integer id) {
        try {
            if (jwtFiltro.esAdmin()) {
                Optional productoOptional = productoDAO.findById(id);
                if(!productoOptional.isEmpty()) {
                    productoDAO.deleteById(id);
                    return FacturaUtils.getResponseEntity("Producto eliminado con exito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS, HttpStatus.BAD_REQUEST);
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> modificarSatus(Map<String, String> requestMap) {
        try {
            if (jwtFiltro.esAdmin()) {
                Optional productoOptional = productoDAO.findById(Integer.parseInt(requestMap.get("productoId")));
                if(!productoOptional.isEmpty()) {
                    productoDAO.modificarStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("productoId")));
                    return FacturaUtils.getResponseEntity("Status del producto actualizado con exito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity("El producto no exite", HttpStatus.NOT_FOUND);
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> clasificarPorCategoria(Integer id) {
        try {
            return new ResponseEntity<>(productoDAO.getProductosXCategoria(id),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductoWrapper> obtenerProductoPorId(Integer id) {
        try {
            return new  ResponseEntity<>(productoDAO.getProductoPorId(id),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ProductoWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Producto getProductoDeMap(Map<String, String> requestMap, boolean estaAgregado) {
        Categoria categoria = new Categoria();
        categoria.setId(Integer.parseInt(requestMap.get("categoriaId")));

        Producto producto = new Producto();
        if(estaAgregado){
            producto.setId(Integer.parseInt(requestMap.get("productoId")));
        } else {
            producto.setStatus("true");
        }
        producto.setCategoria(categoria);
        producto.setNombre(requestMap.get("nombre"));
        producto.setDescripcion(requestMap.get("descripcion"));
        producto.setPrecio(Integer.parseInt(requestMap.get("precio")));
        return producto;

    }

    private boolean validarProductoMap(Map<String, String> requestMap, boolean validarId) {
        if(requestMap.containsKey("nombre") &&
                requestMap.containsKey("categoriaId") &&
                requestMap.containsKey("descripcion") &&
                requestMap.containsKey("precio")) {
            if(requestMap.containsKey("productoId") && validarId) {
                return true;
            }
            if(!validarId) {
                return true;
            }
        }
        return false;
    }
}
