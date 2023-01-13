package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Catalog;

import java.util.List;

public interface CatalogRepositoryCustom {

  List<Catalog> findAllByAuthor(String authorName);

}
