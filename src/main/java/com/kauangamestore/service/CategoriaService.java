package com.kauangamestore.service;

import com.kauangamestore.dto.CategoriaDTO;
import com.kauangamestore.dto.CategoriaResponseDTO;
import java.util.List;

public interface CategoriaService {

    List<CategoriaResponseDTO> getAll(int page, int pageSize);

    CategoriaResponseDTO findById(Long id);

    List<CategoriaResponseDTO> findByNome(String nome, int page, int pageSize);

    long count();

    long countNome(String nome);

    CategoriaResponseDTO create(CategoriaDTO dto);

    CategoriaResponseDTO update(Long id, CategoriaDTO dto);

    void delete(Long id);
}
