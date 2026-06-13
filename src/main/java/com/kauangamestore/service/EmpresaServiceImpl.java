package com.kauangamestore.service;

import com.kauangamestore.dto.EmpresaDTO;
import com.kauangamestore.dto.EmpresaResponseDTO;
import com.kauangamestore.model.Empresa;
import com.kauangamestore.repository.EmpresaRepository;
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
public class EmpresaServiceImpl implements EmpresaService {

    @Inject
    EmpresaRepository repository;

    @Inject
    Validator validator;

    @Override
    public List<EmpresaResponseDTO> getAll(int page, int pageSize) {
        return repository.findAll().page(page, pageSize).list().stream()
            .map(EmpresaResponseDTO::valueOf)
            .toList();
    }

    @Override
    public EmpresaResponseDTO findById(Long id) {
        Empresa empresa = repository.findById(id);
        if (empresa == null) {
            throw new NotFoundException("Empresa nao encontrada.");
        }
        return EmpresaResponseDTO.valueOf(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> findByNome(String nome, int page, int pageSize) {
        return repository.findByNome(nome).page(page, pageSize).list().stream()
            .map(EmpresaResponseDTO::valueOf)
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
    public EmpresaResponseDTO create(EmpresaDTO dto) {
        validar(dto);
        Empresa entity = new Empresa();
        apply(dto, entity);
        repository.persist(entity);
        return EmpresaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public EmpresaResponseDTO update(Long id, EmpresaDTO dto) {
        validar(dto);
        Empresa entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Empresa nao encontrada.");
        }
        apply(dto, entity);
        entity.persist();
        return EmpresaResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Empresa nao encontrada.");
        }
    }

    private void validar(EmpresaDTO dto) {
        Set<ConstraintViolation<EmpresaDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void apply(EmpresaDTO dto, Empresa entity) {
        entity.setNome(safeTrim(dto.nome()));
        entity.setPaisOrigem(safeTrim(dto.paisOrigem()));
        entity.setDescricao(safeTrim(dto.descricao()));
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
