package poc.pucminas.repository;

import poc.pucminas.domain.Ativo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ativo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {
}
