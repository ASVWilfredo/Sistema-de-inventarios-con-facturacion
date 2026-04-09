package com.api.gestion.servicio.implemento;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.CategoriaDAO;
import com.api.gestion.pojo.Categoria;
import com.api.gestion.seguridad.jwt.JwtFiltro;
import com.api.gestion.servicio.CategoriaServicio;
import com.api.gestion.util.FacturaUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoriaServicioImplemento implements CategoriaServicio {
    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private JwtFiltro jwtFiltro;

    @Override
    public ResponseEntity<String> agregarNuevaCategoria(Map<String, String> requestMap) {
        try {
            if (!jwtFiltro.esAdmin()) {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,HttpStatus.UNAUTHORIZED);
            }
            if (validarCategoriaMap(requestMap, false)) {
                categoriaDAO.save(getCategoriaDeMapa(requestMap, false));
                return FacturaUtils.getResponseEntity("Categoria agregada con exito", HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Categoria>> obtenerTodasCategorias(String valorFiltro) {
        try {
            if(!Strings.isNullOrEmpty(valorFiltro) && valorFiltro.equals("true")) {
                log.info("Usando el metodo obtenerTodasCategorias() de Categoria");
                return new ResponseEntity<List<Categoria>>(categoriaDAO.obtenerTodasCategorias(), HttpStatus.OK);
            }
            log.info("Usando el metodo findAll() de JpaRepository");
            return new ResponseEntity<List<Categoria>>(categoriaDAO.findAll(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<Categoria>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizarCategoria(Map<String, String> requestMap) {
        try {
            if (!jwtFiltro.esAdmin()) {
                return FacturaUtils.getResponseEntity(FacturaConstantes.ACCESO_NO_AUTORIZADO,
                        HttpStatus.UNAUTHORIZED);
            }
            if(validarCategoriaMap(requestMap, true)) {
                Optional optional = categoriaDAO.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()) {
                    categoriaDAO.save(getCategoriaDeMapa(requestMap, true));
                    return FacturaUtils.getResponseEntity("Categoria actualizada con exito", HttpStatus.OK);

                } else {
                    return FacturaUtils.getResponseEntity("La categoria con ese ID no existe",HttpStatus.NOT_FOUND);
                }
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.DATOS_INVALIDOS,HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarCategoriaMap(Map<String, String> requestMap, boolean validarId) {
        if(requestMap.containsKey("nombre")) {
            if(requestMap.containsKey("id") && validarId) {
                return true;
            }
            if(!validarId) {
                return true;
            }
        }
        return false;
    }

    private Categoria getCategoriaDeMapa(Map<String,String> requestMap, Boolean estaAgregado) {
        Categoria categoria = new Categoria();
        if(estaAgregado) {
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNombre(requestMap.get("nombre"));
        return  categoria;
    }
}
