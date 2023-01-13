package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, String>, CatalogRepositoryCustom {

}
