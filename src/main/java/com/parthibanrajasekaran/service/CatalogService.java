package com.parthibanrajasekaran.service;

import com.parthibanrajasekaran.model.Catalog;
import com.parthibanrajasekaran.repository.CatalogRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

  @Autowired
  CatalogRepository catalogRepository;

  public String createId(String course, int category){
    return course+category;
  }

  public boolean verifyIfCourseWithIdExists(String id) {
    Optional<Catalog> catalog = catalogRepository.findById(id);
    return catalog.isPresent();
  }

  public Catalog getCourseById(String id){
    return catalogRepository.findById(id).get();
  }
}
