package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Catalog;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class CatalogRepositoryImplementation implements CatalogRepositoryCustom {

  @Autowired
  CatalogRepository catalogRepository;

  @Override
  public List<Catalog> findAllByAuthor(String authorName) {
    return catalogRepository.findAllByAuthor(authorName);
  }
}
