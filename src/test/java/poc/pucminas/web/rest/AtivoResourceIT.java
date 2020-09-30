package poc.pucminas.web.rest;

import poc.pucminas.AtivoApp;
import poc.pucminas.domain.Ativo;
import poc.pucminas.repository.AtivoRepository;
import poc.pucminas.service.AtivoService;
import poc.pucminas.service.dto.AtivoDTO;
import poc.pucminas.service.mapper.AtivoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AtivoResource} REST controller.
 */
@SpringBootTest(classes = AtivoApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AtivoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTIDADE = "AAAAAAAAAA";
    private static final String UPDATED_QUANTIDADE = "BBBBBBBBBB";

    @Autowired
    private AtivoRepository ativoRepository;

    @Autowired
    private AtivoMapper ativoMapper;

    @Autowired
    private AtivoService ativoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtivoMockMvc;

    private Ativo ativo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ativo createEntity(EntityManager em) {
        Ativo ativo = new Ativo()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .quantidade(DEFAULT_QUANTIDADE);
        return ativo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ativo createUpdatedEntity(EntityManager em) {
        Ativo ativo = new Ativo()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE);
        return ativo;
    }

    @BeforeEach
    public void initTest() {
        ativo = createEntity(em);
    }

    @Test
    @Transactional
    public void createAtivo() throws Exception {
        int databaseSizeBeforeCreate = ativoRepository.findAll().size();
        // Create the Ativo
        AtivoDTO ativoDTO = ativoMapper.toDto(ativo);
        restAtivoMockMvc.perform(post("/api/ativos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ativoDTO)))
            .andExpect(status().isCreated());

        // Validate the Ativo in the database
        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeCreate + 1);
        Ativo testAtivo = ativoList.get(ativoList.size() - 1);
        assertThat(testAtivo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAtivo.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testAtivo.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
    }

    @Test
    @Transactional
    public void createAtivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ativoRepository.findAll().size();

        // Create the Ativo with an existing ID
        ativo.setId(1L);
        AtivoDTO ativoDTO = ativoMapper.toDto(ativo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtivoMockMvc.perform(post("/api/ativos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ativoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ativo in the database
        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ativoRepository.findAll().size();
        // set the field null
        ativo.setNome(null);

        // Create the Ativo, which fails.
        AtivoDTO ativoDTO = ativoMapper.toDto(ativo);


        restAtivoMockMvc.perform(post("/api/ativos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ativoDTO)))
            .andExpect(status().isBadRequest());

        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAtivos() throws Exception {
        // Initialize the database
        ativoRepository.saveAndFlush(ativo);

        // Get all the ativoList
        restAtivoMockMvc.perform(get("/api/ativos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ativo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)));
    }
    
    @Test
    @Transactional
    public void getAtivo() throws Exception {
        // Initialize the database
        ativoRepository.saveAndFlush(ativo);

        // Get the ativo
        restAtivoMockMvc.perform(get("/api/ativos/{id}", ativo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ativo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE));
    }
    @Test
    @Transactional
    public void getNonExistingAtivo() throws Exception {
        // Get the ativo
        restAtivoMockMvc.perform(get("/api/ativos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAtivo() throws Exception {
        // Initialize the database
        ativoRepository.saveAndFlush(ativo);

        int databaseSizeBeforeUpdate = ativoRepository.findAll().size();

        // Update the ativo
        Ativo updatedAtivo = ativoRepository.findById(ativo.getId()).get();
        // Disconnect from session so that the updates on updatedAtivo are not directly saved in db
        em.detach(updatedAtivo);
        updatedAtivo
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE);
        AtivoDTO ativoDTO = ativoMapper.toDto(updatedAtivo);

        restAtivoMockMvc.perform(put("/api/ativos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ativoDTO)))
            .andExpect(status().isOk());

        // Validate the Ativo in the database
        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeUpdate);
        Ativo testAtivo = ativoList.get(ativoList.size() - 1);
        assertThat(testAtivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAtivo.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAtivo.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    public void updateNonExistingAtivo() throws Exception {
        int databaseSizeBeforeUpdate = ativoRepository.findAll().size();

        // Create the Ativo
        AtivoDTO ativoDTO = ativoMapper.toDto(ativo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtivoMockMvc.perform(put("/api/ativos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ativoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ativo in the database
        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAtivo() throws Exception {
        // Initialize the database
        ativoRepository.saveAndFlush(ativo);

        int databaseSizeBeforeDelete = ativoRepository.findAll().size();

        // Delete the ativo
        restAtivoMockMvc.perform(delete("/api/ativos/{id}", ativo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ativo> ativoList = ativoRepository.findAll();
        assertThat(ativoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
