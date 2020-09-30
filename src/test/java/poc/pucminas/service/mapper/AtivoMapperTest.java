package poc.pucminas.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AtivoMapperTest {

    private AtivoMapper ativoMapper;

    @BeforeEach
    public void setUp() {
        ativoMapper = new AtivoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ativoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ativoMapper.fromId(null)).isNull();
    }
}
