package poc.pucminas.service.mapper;


import poc.pucminas.domain.*;
import poc.pucminas.service.dto.AtivoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ativo} and its DTO {@link AtivoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AtivoMapper extends EntityMapper<AtivoDTO, Ativo> {



    default Ativo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ativo ativo = new Ativo();
        ativo.setId(id);
        return ativo;
    }
}
