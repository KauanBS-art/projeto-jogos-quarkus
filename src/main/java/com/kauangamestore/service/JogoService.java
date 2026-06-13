package com.kauangamestore.service;

import com.kauangamestore.dto.JogoDTO;
import com.kauangamestore.dto.JogoResponseDTO;
import java.util.List;

public interface JogoService {

    List<JogoResponseDTO> getAll(int page, int pageSize);

    JogoResponseDTO findById(Long id);

    List<JogoResponseDTO> findByNome(String nome, int page, int pageSize);

    long count();

    long countNome(String nome);

    JogoResponseDTO create(JogoDTO dto);

    JogoResponseDTO update(Long id, JogoDTO dto);

    void delete(Long id);

    List<JogoResponseDTO> findPromocoes(String nome, int page, int pageSize);

    long countPromocoes(String nome);
}
