package com.kauangamestore.service;

import com.kauangamestore.dto.PlataformaDTO;
import com.kauangamestore.dto.PlataformaResponseDTO;
import java.util.List;

public interface PlataformaService {

    List<PlataformaResponseDTO> getAll(int page, int pageSize);

    PlataformaResponseDTO findById(Long id);

    List<PlataformaResponseDTO> findByNome(String nome, int page, int pageSize);

    long count();

    long countNome(String nome);

    PlataformaResponseDTO create(PlataformaDTO dto);

    PlataformaResponseDTO update(Long id, PlataformaDTO dto);

    void delete(Long id);
}
