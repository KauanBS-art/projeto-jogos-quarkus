package com.kauangamestore.service;

import com.kauangamestore.dto.PlataformaDTO;
import com.kauangamestore.dto.PlataformaResponseDTO;
import com.kauangamestore.model.Plataforma;
import com.kauangamestore.repository.PlataformaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class PlataformaServiceImpl implements PlataformaService {

    @Inject
    PlataformaRepository repository;

    @Inject
    Validator validator;

    @Override
    public List<PlataformaResponseDTO> getAll(int page, int pageSize) {
        return repository.findAll().page(page, pageSize).list().stream()
            .map(PlataformaResponseDTO::valueOf)
            .toList();
    }

    @Override
    public PlataformaResponseDTO findById(Long id) {
        Plataforma plataforma = repository.findById(id);
        if (plataforma == null) {
            throw new NotFoundException("Plataforma nao encontrada.");
        }
        return PlataformaResponseDTO.valueOf(plataforma);
    }

    @Override
    public List<PlataformaResponseDTO> findByNome(String nome, int page, int pageSize) {
        return repository.findByNome(nome).page(page, pageSize).list().stream()
            .map(PlataformaResponseDTO::valueOf)
            .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countNome(String nome) {
        return repository.countNome(nome);
    }

    @Override
    @Transactional
    public PlataformaResponseDTO create(PlataformaDTO dto) {
        validar(dto);
        Plataforma entity = new Plataforma();
        apply(dto, entity);
        repository.persist(entity);
        return PlataformaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public PlataformaResponseDTO update(Long id, PlataformaDTO dto) {
        validar(dto);
        Plataforma entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Plataforma nao encontrada.");
        }
        apply(dto, entity);
        entity.persist();
        return PlataformaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Plataforma nao encontrada.");
        }
    }

    private void validar(PlataformaDTO dto) {
        Set<ConstraintViolation<PlataformaDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void apply(PlataformaDTO dto, Plataforma entity) {
        entity.setNome(safeTrim(dto.nome()));
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
