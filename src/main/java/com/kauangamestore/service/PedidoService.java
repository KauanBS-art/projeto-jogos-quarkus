package com.kauangamestore.service;

import com.kauangamestore.dto.PedidoDTO;
import com.kauangamestore.dto.PedidoResponseDTO;
import java.util.List;

public interface PedidoService {

    List<PedidoResponseDTO> getAll(int page, int pageSize);

    long count();

    PedidoResponseDTO findById(Long id);

    PedidoResponseDTO create(PedidoDTO dto, String emailUsuario);

    List<PedidoResponseDTO> findByUsuario(String emailUsuario);
}
