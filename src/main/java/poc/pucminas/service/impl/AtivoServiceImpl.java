package poc.pucminas.service.impl;

import poc.pucminas.service.AtivoService;
import poc.pucminas.domain.Ativo;
import poc.pucminas.repository.AtivoRepository;
import poc.pucminas.service.dto.AtivoDTO;
import poc.pucminas.service.mapper.AtivoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ativo}.
 */
@Service
@Transactional
public class AtivoServiceImpl implements AtivoService {

    private final Logger log = LoggerFactory.getLogger(AtivoServiceImpl.class);

    private final AtivoRepository ativoRepository;

    private final AtivoMapper ativoMapper;

    public AtivoServiceImpl(AtivoRepository ativoRepository, AtivoMapper ativoMapper) {
        this.ativoRepository = ativoRepository;
        this.ativoMapper = ativoMapper;
    }

    @Override
    public AtivoDTO save(AtivoDTO ativoDTO) {
        log.debug("Request to save Ativo : {}", ativoDTO);
        Ativo ativo = ativoMapper.toEntity(ativoDTO);
        ativo = ativoRepository.save(ativo);
        return ativoMapper.toDto(ativo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AtivoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ativos");
        return ativoRepository.findAll(pageable)
            .map(ativoMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AtivoDTO> findOne(Long id) {
        log.debug("Request to get Ativo : {}", id);
        return ativoRepository.findById(id)
            .map(ativoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ativo : {}", id);
        ativoRepository.deleteById(id);
    }
}
