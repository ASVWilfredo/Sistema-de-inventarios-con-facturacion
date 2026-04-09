package com.api.gestion.rest;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.pojo.Categoria;
import com.api.gestion.servicio.CategoriaServicio;
import com.api.gestion.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoria")
public class CategoriaControlador {
    @Autowired
    private CategoriaServicio categoriaServicio;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarNuevaCategoria(@RequestBody(required =true) Map<String,String> requestMap){
        try {
            return categoriaServicio.agregarNuevaCategoria(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/obtener")
    public ResponseEntity<List<Categoria>> listarCategorias(@RequestParam(required=false) String valorFiltro){
        try {
            return categoriaServicio.obtenerTodasCategorias(valorFiltro);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<Categoria>(), HttpStatus.OK);
    }

    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarCategoria(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return categoriaServicio.actualizarCategoria(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.ALGO_SALIO_MAL, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
