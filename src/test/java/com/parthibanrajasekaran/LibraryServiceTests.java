package com.parthibanrajasekaran;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.parthibanrajasekaran.model.Library;
import com.parthibanrajasekaran.repository.LibraryRepository;
import com.parthibanrajasekaran.service.LibraryService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class LibraryServiceTests {

  @Autowired
  LibraryService libraryService;

  @MockBean
  LibraryRepository libraryRepository;

  @Test
  public void verifyCreateId(){
    final String actualId = libraryService.createId("Zalenium",2022);
    assertEquals("Zalenium2022",actualId, "checkBuildId validated successfully");
  }

  @Test
  public void verifyIfBookWithIdExistsTest(){
    Library library = buildLibrary();
    when(libraryRepository.findById(any())).thenReturn(Optional.of(library));

    assertTrue(libraryService.verifyIfBookWithIdExists(library.getId()));
  }

  @Test
  public void getBookByIdTest(){
    Library library = buildLibrary();
    when(libraryRepository.findById(any())).thenReturn(Optional.of(library));

    assertEquals(libraryService.getBookById(library.getId()), library);
  }

  private Library buildLibrary(){
    Library library = new Library();
    library.setBook_name("SpringBoot");
    library.setAuthor("Adhvik");
    library.setAisle(2022);
    library.setIsbn("SAFe");
    library.setId("SAFe2022");
    return library;
  }

}
