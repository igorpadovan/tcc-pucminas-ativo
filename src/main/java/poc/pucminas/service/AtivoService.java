package poc.pucminas.service;

import poc.pucminas.service.dto.AtivoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link poc.pucminas.domain.Ativo}.
 */
public interface AtivoService {

    /**
     * Save a ativo.
     *
     * @param ativoDTO the entity to save.
     * @return the persisted entity.
     */
    AtivoDTO save(AtivoDTO ativoDTO);

    /**
     * Get all the ativos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AtivoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" ativo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AtivoDTO> findOne(Long id);

    /**
     * Delete the "id" ativo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
