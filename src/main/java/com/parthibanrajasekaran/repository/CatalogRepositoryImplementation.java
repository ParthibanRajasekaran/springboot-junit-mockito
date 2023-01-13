package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Catalog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CatalogRepositoryImplementation implements CatalogRepositoryCustom {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public List<Catalog> findAllByAuthor(String authorName) {
        return catalogRepository.findAllByAuthor(authorName);
    }
}
