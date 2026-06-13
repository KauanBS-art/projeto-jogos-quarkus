package com.kauangamestore.service;

import com.kauangamestore.dto.EmpresaDTO;
import com.kauangamestore.dto.EmpresaResponseDTO;
import java.util.List;

public interface EmpresaService {

    List<EmpresaResponseDTO> getAll(int page, int pageSize);

    EmpresaResponseDTO findById(Long id);

    List<EmpresaResponseDTO> findByNome(String nome, int page, int pageSize);

    long count();

    long countNome(String nome);

    EmpresaResponseDTO create(EmpresaDTO dto);

    EmpresaResponseDTO update(Long id, EmpresaDTO dto);

    void delete(Long id);
}
