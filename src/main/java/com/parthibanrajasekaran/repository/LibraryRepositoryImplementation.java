package com.parthibanrajasekaran.repository;

import com.parthibanrajasekaran.model.Library;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class LibraryRepositoryImplementation implements LibraryRepositoryCustom {

  @Autowired
  LibraryRepository libraryRepository;

  @Override
  public List<Library> findAllByAuthor(String authorName) {
    List<Library> books = libraryRepository.findAll();
    List<Library> booksWithAuthor = new ArrayList<>();

    for(Library book : books){
      if(book.getAuthor().equalsIgnoreCase(authorName)){
        booksWithAuthor.add(book);
      }
    }
    return booksWithAuthor;
  }
}
