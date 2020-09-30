package poc.pucminas.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import poc.pucminas.web.rest.TestUtil;

public class AtivoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ativo.class);
        Ativo ativo1 = new Ativo();
        ativo1.setId(1L);
        Ativo ativo2 = new Ativo();
        ativo2.setId(ativo1.getId());
        assertThat(ativo1).isEqualTo(ativo2);
        ativo2.setId(2L);
        assertThat(ativo1).isNotEqualTo(ativo2);
        ativo1.setId(null);
        assertThat(ativo1).isNotEqualTo(ativo2);
    }
}
