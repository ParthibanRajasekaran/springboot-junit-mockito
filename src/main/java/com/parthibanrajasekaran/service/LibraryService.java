package com.parthibanrajasekaran.service;

import com.parthibanrajasekaran.model.Library;
import com.parthibanrajasekaran.repository.LibraryRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

  @Autowired
  LibraryRepository libraryRepository;

  public String createId(String isbn, int aisle){
    return isbn+aisle;
  }

  public boolean verifyIfBookWithIdExists(String id) {
    Optional<Library> library = libraryRepository.findById(id);
    return library.isPresent();
  }

  public Library getBookById(String id){
    return libraryRepository.findById(id).get();
  }
}
