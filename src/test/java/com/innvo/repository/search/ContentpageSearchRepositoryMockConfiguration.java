package com.innvo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ContentpageSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ContentpageSearchRepositoryMockConfiguration {

    @MockBean
    private ContentpageSearchRepository mockContentpageSearchRepository;

}
