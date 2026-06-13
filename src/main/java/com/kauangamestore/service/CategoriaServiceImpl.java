package com.kauangamestore.service;

import com.kauangamestore.dto.CategoriaDTO;
import com.kauangamestore.dto.CategoriaResponseDTO;
import com.kauangamestore.model.Categoria;
import com.kauangamestore.repository.CategoriaRepository;
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
public class CategoriaServiceImpl implements CategoriaService {

    @Inject
    CategoriaRepository repository;

    @Inject
    Validator validator;

    @Override
    public List<CategoriaResponseDTO> getAll(int page, int pageSize) {
        return repository.findAll().page(page, pageSize).list().stream()
            .map(CategoriaResponseDTO::valueOf)
            .toList();
    }

    @Override
    public CategoriaResponseDTO findById(Long id) {
        Categoria categoria = repository.findById(id);
        if (categoria == null) {
            throw new NotFoundException("Categoria nao encontrada.");
        }
        return CategoriaResponseDTO.valueOf(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> findByNome(String nome, int page, int pageSize) {
        return repository.findByNome(nome).page(page, pageSize).list().stream()
            .map(CategoriaResponseDTO::valueOf)
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
    public CategoriaResponseDTO create(CategoriaDTO dto) {
        validar(dto);
        Categoria entity = new Categoria();
        apply(dto, entity);
        repository.persist(entity);
        return CategoriaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaDTO dto) {
        validar(dto);
        Categoria entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Categoria nao encontrada.");
        }
        apply(dto, entity);
        entity.persist();
        return CategoriaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Categoria nao encontrada.");
        }
    }

    private void validar(CategoriaDTO dto) {
        Set<ConstraintViolation<CategoriaDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void apply(CategoriaDTO dto, Categoria entity) {
        entity.setNome(safeTrim(dto.nome()));
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
