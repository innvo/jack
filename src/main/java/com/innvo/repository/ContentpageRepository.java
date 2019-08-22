package com.innvo.repository;

import com.innvo.domain.Contentpage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Contentpage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentpageRepository extends JpaRepository<Contentpage, Long> {

}
