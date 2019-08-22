package com.innvo.repository.search;

import com.innvo.domain.Contentpage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Contentpage} entity.
 */
public interface ContentpageSearchRepository extends ElasticsearchRepository<Contentpage, Long> {
}
