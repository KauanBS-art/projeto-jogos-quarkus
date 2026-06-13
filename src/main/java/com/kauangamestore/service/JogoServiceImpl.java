package com.kauangamestore.service;

import com.kauangamestore.dto.JogoDTO;
import com.kauangamestore.dto.JogoResponseDTO;
import com.kauangamestore.model.Categoria;
import com.kauangamestore.model.Empresa;
import com.kauangamestore.model.Jogo;
import com.kauangamestore.model.Plataforma;
import com.kauangamestore.repository.CategoriaRepository;
import com.kauangamestore.repository.EmpresaRepository;
import com.kauangamestore.repository.JogoRepository;
import com.kauangamestore.repository.PlataformaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class JogoServiceImpl implements JogoService {

    @Inject
    JogoRepository jogoRepository;

    @Inject
    EmpresaRepository empresaRepository;

    @Inject
    PlataformaRepository plataformaRepository;

    @Inject
    CategoriaRepository categoriaRepository;

    @Inject
    Validator validator;

    @Override
    public List<JogoResponseDTO> getAll(int page, int pageSize) {
        return jogoRepository.findAll().page(page, pageSize).list().stream()
            .map(JogoResponseDTO::valueOf)
            .toList();
    }

    @Override
    public JogoResponseDTO findById(Long id) {
        Jogo jogo = jogoRepository.findById(id);
        if (jogo == null) {
            throw new NotFoundException("Jogo nao encontrado.");
        }
        return JogoResponseDTO.valueOf(jogo);
    }

    @Override
    public List<JogoResponseDTO> findByNome(String nome, int page, int pageSize) {
        return jogoRepository.findByNome(nome).page(page, pageSize).list().stream()
            .map(JogoResponseDTO::valueOf)
            .toList();
    }

    @Override
    public long count() {
        return jogoRepository.count();
    }

    @Override
    public long countNome(String nome) {
        return jogoRepository.countNome(nome);
    }

    @Override
    @Transactional
    public JogoResponseDTO create(JogoDTO dto) {
        validar(dto);
        Jogo entity = new Jogo();
        apply(dto, entity);
        jogoRepository.persist(entity);
        return JogoResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public JogoResponseDTO update(Long id, JogoDTO dto) {
        validar(dto);
        Jogo entity = jogoRepository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Jogo nao encontrado.");
        }
        apply(dto, entity);
        entity.persist();
        return JogoResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!jogoRepository.deleteById(id)) {
            throw new NotFoundException("Jogo nao encontrado.");
        }
    }

    @Override
    public List<JogoResponseDTO> findPromocoes(String nome, int page, int pageSize) {
        return jogoRepository.findByPromocao(nome).page(page, pageSize).list().stream()
            .map(JogoResponseDTO::valueOf)
            .toList();
    }

    @Override
    public long countPromocoes(String nome) {
        return jogoRepository.countPromocoes(nome);
    }

    private void validar(JogoDTO dto) {
        Set<ConstraintViolation<JogoDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void apply(JogoDTO dto, Jogo entity) {
        Empresa empresa = empresaRepository.findById(dto.idEmpresa());
        if (empresa == null) {
            throw new NotFoundException("Empresa nao encontrada.");
        }

        entity.setTitulo(safeTrim(dto.titulo()));
        entity.setDescricao(safeTrim(dto.descricao()));
        entity.setPreco(dto.preco());
        entity.setEstoque(dto.estoque());
        entity.setPercentualDesconto(dto.percentualDesconto());
        entity.setDataLancamento(dto.dataLancamento());
        entity.setDesenvolvedora(empresa);
        entity.setPlataformas(carregarPlataformas(dto.idPlataformas()));
        entity.setCategorias(carregarCategorias(dto.idCategorias()));
    }

    private List<Plataforma> carregarPlataformas(List<Long> ids) {
        List<Plataforma> plataformas = new ArrayList<>();
        if (ids == null) {
            return plataformas;
        }

        for (Long id : ids) {
            Plataforma plataforma = plataformaRepository.findById(id);
            if (plataforma == null) {
                throw new NotFoundException("Plataforma nao encontrada: " + id);
            }
            plataformas.add(plataforma);
        }

        return plataformas;
    }

    private List<Categoria> carregarCategorias(List<Long> ids) {
        List<Categoria> categorias = new ArrayList<>();
        if (ids == null) {
            return categorias;
        }

        for (Long id : ids) {
            Categoria categoria = categoriaRepository.findById(id);
            if (categoria == null) {
                throw new NotFoundException("Categoria nao encontrada: " + id);
            }
            categorias.add(categoria);
        }

        return categorias;
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
