package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Library;
import java.util.List;

public interface LibraryRepositoryCustom {

  List<Library> findAllByAuthor(String authorName);

}
