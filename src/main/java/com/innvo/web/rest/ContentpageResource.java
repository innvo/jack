package com.innvo.web.rest;

import com.innvo.domain.Contentpage;
import com.innvo.repository.ContentpageRepository;
import com.innvo.repository.search.ContentpageSearchRepository;
import com.innvo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.innvo.domain.Contentpage}.
 */
@RestController
@RequestMapping("/api")
public class ContentpageResource {

    private final Logger log = LoggerFactory.getLogger(ContentpageResource.class);

    private static final String ENTITY_NAME = "contentpage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentpageRepository contentpageRepository;

    private final ContentpageSearchRepository contentpageSearchRepository;

    public ContentpageResource(ContentpageRepository contentpageRepository, ContentpageSearchRepository contentpageSearchRepository) {
        this.contentpageRepository = contentpageRepository;
        this.contentpageSearchRepository = contentpageSearchRepository;
    }

    /**
     * {@code POST  /contentpages} : Create a new contentpage.
     *
     * @param contentpage the contentpage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentpage, or with status {@code 400 (Bad Request)} if the contentpage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contentpages")
    public ResponseEntity<Contentpage> createContentpage(@Valid @RequestBody Contentpage contentpage) throws URISyntaxException {
        log.debug("REST request to save Contentpage : {}", contentpage);
        if (contentpage.getId() != null) {
            throw new BadRequestAlertException("A new contentpage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contentpage result = contentpageRepository.save(contentpage);
        contentpageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/contentpages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contentpages} : Updates an existing contentpage.
     *
     * @param contentpage the contentpage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentpage,
     * or with status {@code 400 (Bad Request)} if the contentpage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentpage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contentpages")
    public ResponseEntity<Contentpage> updateContentpage(@Valid @RequestBody Contentpage contentpage) throws URISyntaxException {
        log.debug("REST request to update Contentpage : {}", contentpage);
        if (contentpage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contentpage result = contentpageRepository.save(contentpage);
        contentpageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentpage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contentpages} : get all the contentpages.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentpages in body.
     */
    @GetMapping("/contentpages")
    public ResponseEntity<List<Contentpage>> getAllContentpages(Pageable pageable) {
        log.debug("REST request to get a page of Contentpages");
        Page<Contentpage> page = contentpageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contentpages/:id} : get the "id" contentpage.
     *
     * @param id the id of the contentpage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentpage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contentpages/{id}")
    public ResponseEntity<Contentpage> getContentpage(@PathVariable Long id) {
        log.debug("REST request to get Contentpage : {}", id);
        Optional<Contentpage> contentpage = contentpageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contentpage);
    }

    /**
     * {@code DELETE  /contentpages/:id} : delete the "id" contentpage.
     *
     * @param id the id of the contentpage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contentpages/{id}")
    public ResponseEntity<Void> deleteContentpage(@PathVariable Long id) {
        log.debug("REST request to delete Contentpage : {}", id);
        contentpageRepository.deleteById(id);
        contentpageSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/contentpages?query=:query} : search for the contentpage corresponding
     * to the query.
     *
     * @param query the query of the contentpage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contentpages")
    public ResponseEntity<List<Contentpage>> searchContentpages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Contentpages for query {}", query);
        Page<Contentpage> page = contentpageSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
