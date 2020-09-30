package poc.pucminas.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import poc.pucminas.web.rest.TestUtil;

public class AtivoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AtivoDTO.class);
        AtivoDTO ativoDTO1 = new AtivoDTO();
        ativoDTO1.setId(1L);
        AtivoDTO ativoDTO2 = new AtivoDTO();
        assertThat(ativoDTO1).isNotEqualTo(ativoDTO2);
        ativoDTO2.setId(ativoDTO1.getId());
        assertThat(ativoDTO1).isEqualTo(ativoDTO2);
        ativoDTO2.setId(2L);
        assertThat(ativoDTO1).isNotEqualTo(ativoDTO2);
        ativoDTO1.setId(null);
        assertThat(ativoDTO1).isNotEqualTo(ativoDTO2);
    }
}
