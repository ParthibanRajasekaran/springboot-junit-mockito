package com.parthibanrajasekaran.controller;

import com.parthibanrajasekaran.model.BookResponse;
import com.parthibanrajasekaran.model.Library;
import com.parthibanrajasekaran.repository.LibraryRepository;
import com.parthibanrajasekaran.service.LibraryService;
import java.text.MessageFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

  private static final Logger log = LoggerFactory.getLogger(LibraryController.class);

  @Autowired
  LibraryRepository libraryRepository;

  @Autowired
  BookResponse bookResponse;

  @Autowired
  LibraryService libraryService;

  @PostMapping("/addBook")
  public ResponseEntity<BookResponse> addBook(@RequestBody Library library){

    final String id = libraryService.createId(library.getIsbn(),library.getAisle());

    if(!libraryService.verifyIfBookWithIdExists(id)) {
      library.setId(id);
      // JPA repository is used to flush data from the input to the DB via save
      libraryRepository.save(library);

      // Creating response headers
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("unique",id);
      log.debug("Unique Id is added to the header successfully");

      // Adding response message for the request
      bookResponse.setMsg("Book has been successfully added");
      bookResponse.setId(id);
      log.info(String.format("Book with id: %s added successfully to the library", id));
      return new ResponseEntity<BookResponse>(bookResponse, httpHeaders,HttpStatus.CREATED);
    }
   else {
      log.debug("Book already exists - Duplicate entry");
      bookResponse.setMsg("Book already exists");
      bookResponse.setId(id);
      return new ResponseEntity<>(bookResponse, HttpStatus.ACCEPTED);
    }
  }

  @GetMapping("/getBook/{id}")
  public ResponseEntity getBookById(@PathVariable(value = "id") String id){
    if(libraryService.verifyIfBookWithIdExists(id)) {
      Library library = libraryService.getBookById(id);
      return new ResponseEntity<Library>(library, HttpStatus.FOUND);
    } else{
      log.info(MessageFormat.format("Book with id: {0} does not exists", id));
      bookResponse.setMsg(String.format("Book with id: %s does not exists", id));
      return new ResponseEntity<>(bookResponse, HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/getBook/author")
  public List<Library> getBookByAuthorName(@RequestParam(value = "authorName") String author){
      return libraryRepository.findAllByAuthor(author);
  }

  @GetMapping("/getBooks")
  public List<Library> getAllBooks(){
    log.info("Retrieving all the books from the database");
    return libraryRepository.findAll();
  }

//  @PostMapping("/updateBook/{id}")
  @PutMapping("/updateBook/{id}")
  public ResponseEntity updateBook(@PathVariable(value = "id") String id, @RequestBody Library library){
    if(libraryService.verifyIfBookWithIdExists(id)) {
      Library libraryToBeUpdated = libraryService.getBookById(id);

      libraryToBeUpdated.setAisle(library.getAisle());
      libraryToBeUpdated.setAuthor(library.getAuthor());
      libraryToBeUpdated.setBook_name(library.getBook_name());
      libraryRepository.save(libraryToBeUpdated);
      log.debug(String.format("Book with id: %s updated successfully", id));
      return new ResponseEntity<Library>(libraryToBeUpdated, HttpStatus.OK);
    } else{
      log.info(MessageFormat.format("Book with id: {0} does not exists", id));
      bookResponse.setMsg(String.format("Book with id: %s does not exists", id));
      return new ResponseEntity<>(bookResponse, HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/deleteBook")
  public ResponseEntity deleteBook(@RequestBody Library library){

    final String id = library.getId();
    if(libraryService.verifyIfBookWithIdExists(id)) {
      Library libraryToBeDeleted = libraryService.getBookById(id);
      libraryRepository.delete(libraryToBeDeleted);
      log.debug(String.format("Book with id: %s deleted successfully", id));
      return new ResponseEntity<>(String.format("Book with id: %s deleted successfully", id), HttpStatus.OK);
    } else{
      log.info(MessageFormat.format("Book with id: {0} does not exists", id));
      bookResponse.setMsg(String.format("Book with id: %s does not exists", id));
      return new ResponseEntity<>(bookResponse, HttpStatus.NOT_FOUND);
    }
  }

}
