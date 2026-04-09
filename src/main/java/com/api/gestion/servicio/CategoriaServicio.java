package com.api.gestion.servicio;

import com.api.gestion.pojo.Categoria;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoriaServicio {
    ResponseEntity<String> agregarNuevaCategoria(Map<String, String> requestMap);
    ResponseEntity<List<Categoria>> obtenerTodasCategorias(String valorFiltro);
    ResponseEntity<String> actualizarCategoria(Map<String, String> requestMap);
}
