package com.innvo.web.rest;

import com.innvo.JackApp;
import com.innvo.domain.Contentpage;
import com.innvo.repository.ContentpageRepository;
import com.innvo.repository.search.ContentpageSearchRepository;
import com.innvo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.innvo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContentpageResource} REST controller.
 */
@SpringBootTest(classes = JackApp.class)
public class ContentpageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ContentpageRepository contentpageRepository;

    /**
     * This repository is mocked in the com.innvo.repository.search test package.
     *
     * @see com.innvo.repository.search.ContentpageSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContentpageSearchRepository mockContentpageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restContentpageMockMvc;

    private Contentpage contentpage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContentpageResource contentpageResource = new ContentpageResource(contentpageRepository, mockContentpageSearchRepository);
        this.restContentpageMockMvc = MockMvcBuilders.standaloneSetup(contentpageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contentpage createEntity(EntityManager em) {
        Contentpage contentpage = new Contentpage()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT);
        return contentpage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contentpage createUpdatedEntity(EntityManager em) {
        Contentpage contentpage = new Contentpage()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT);
        return contentpage;
    }

    @BeforeEach
    public void initTest() {
        contentpage = createEntity(em);
    }

    @Test
    @Transactional
    public void createContentpage() throws Exception {
        int databaseSizeBeforeCreate = contentpageRepository.findAll().size();

        // Create the Contentpage
        restContentpageMockMvc.perform(post("/api/contentpages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentpage)))
            .andExpect(status().isCreated());

        // Validate the Contentpage in the database
        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeCreate + 1);
        Contentpage testContentpage = contentpageList.get(contentpageList.size() - 1);
        assertThat(testContentpage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContentpage.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the Contentpage in Elasticsearch
        verify(mockContentpageSearchRepository, times(1)).save(testContentpage);
    }

    @Test
    @Transactional
    public void createContentpageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentpageRepository.findAll().size();

        // Create the Contentpage with an existing ID
        contentpage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentpageMockMvc.perform(post("/api/contentpages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentpage)))
            .andExpect(status().isBadRequest());

        // Validate the Contentpage in the database
        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeCreate);

        // Validate the Contentpage in Elasticsearch
        verify(mockContentpageSearchRepository, times(0)).save(contentpage);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentpageRepository.findAll().size();
        // set the field null
        contentpage.setTitle(null);

        // Create the Contentpage, which fails.

        restContentpageMockMvc.perform(post("/api/contentpages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentpage)))
            .andExpect(status().isBadRequest());

        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContentpages() throws Exception {
        // Initialize the database
        contentpageRepository.saveAndFlush(contentpage);

        // Get all the contentpageList
        restContentpageMockMvc.perform(get("/api/contentpages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentpage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
    
    @Test
    @Transactional
    public void getContentpage() throws Exception {
        // Initialize the database
        contentpageRepository.saveAndFlush(contentpage);

        // Get the contentpage
        restContentpageMockMvc.perform(get("/api/contentpages/{id}", contentpage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contentpage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContentpage() throws Exception {
        // Get the contentpage
        restContentpageMockMvc.perform(get("/api/contentpages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContentpage() throws Exception {
        // Initialize the database
        contentpageRepository.saveAndFlush(contentpage);

        int databaseSizeBeforeUpdate = contentpageRepository.findAll().size();

        // Update the contentpage
        Contentpage updatedContentpage = contentpageRepository.findById(contentpage.getId()).get();
        // Disconnect from session so that the updates on updatedContentpage are not directly saved in db
        em.detach(updatedContentpage);
        updatedContentpage
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT);

        restContentpageMockMvc.perform(put("/api/contentpages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContentpage)))
            .andExpect(status().isOk());

        // Validate the Contentpage in the database
        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeUpdate);
        Contentpage testContentpage = contentpageList.get(contentpageList.size() - 1);
        assertThat(testContentpage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContentpage.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the Contentpage in Elasticsearch
        verify(mockContentpageSearchRepository, times(1)).save(testContentpage);
    }

    @Test
    @Transactional
    public void updateNonExistingContentpage() throws Exception {
        int databaseSizeBeforeUpdate = contentpageRepository.findAll().size();

        // Create the Contentpage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentpageMockMvc.perform(put("/api/contentpages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentpage)))
            .andExpect(status().isBadRequest());

        // Validate the Contentpage in the database
        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Contentpage in Elasticsearch
        verify(mockContentpageSearchRepository, times(0)).save(contentpage);
    }

    @Test
    @Transactional
    public void deleteContentpage() throws Exception {
        // Initialize the database
        contentpageRepository.saveAndFlush(contentpage);

        int databaseSizeBeforeDelete = contentpageRepository.findAll().size();

        // Delete the contentpage
        restContentpageMockMvc.perform(delete("/api/contentpages/{id}", contentpage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contentpage> contentpageList = contentpageRepository.findAll();
        assertThat(contentpageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Contentpage in Elasticsearch
        verify(mockContentpageSearchRepository, times(1)).deleteById(contentpage.getId());
    }

    @Test
    @Transactional
    public void searchContentpage() throws Exception {
        // Initialize the database
        contentpageRepository.saveAndFlush(contentpage);
        when(mockContentpageSearchRepository.search(queryStringQuery("id:" + contentpage.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contentpage), PageRequest.of(0, 1), 1));
        // Search the contentpage
        restContentpageMockMvc.perform(get("/api/_search/contentpages?query=id:" + contentpage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentpage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contentpage.class);
        Contentpage contentpage1 = new Contentpage();
        contentpage1.setId(1L);
        Contentpage contentpage2 = new Contentpage();
        contentpage2.setId(contentpage1.getId());
        assertThat(contentpage1).isEqualTo(contentpage2);
        contentpage2.setId(2L);
        assertThat(contentpage1).isNotEqualTo(contentpage2);
        contentpage1.setId(null);
        assertThat(contentpage1).isNotEqualTo(contentpage2);
    }
}
