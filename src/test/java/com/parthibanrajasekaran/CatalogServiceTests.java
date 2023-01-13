package com.parthibanrajasekaran;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.parthibanrajasekaran.model.Catalog;
import com.parthibanrajasekaran.repository.CatalogRepository;
import com.parthibanrajasekaran.service.CatalogService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CatalogServiceTests {

  @Autowired
  CatalogService catalogService;

  @MockBean
  CatalogRepository catalogRepository;

  @Test
  public void verifyCreateId(){
    final String actualId = catalogService.createId("Zalenium",2022);
    assertEquals("Zalenium2022",actualId, "checkBuildId validated successfully");
  }

  @Test
  public void verifyIfBookWithIdExistsTest(){
    Catalog catalog = buildLibrary();
    when(catalogRepository.findById(any())).thenReturn(Optional.of(catalog));

    assertTrue(catalogService.verifyIfCourseWithIdExists(catalog.getId()));
  }

  @Test
  public void getBookByIdTest(){
    Catalog catalog = buildLibrary();
    when(catalogRepository.findById(any())).thenReturn(Optional.of(catalog));

    assertEquals(catalogService.getCourseById(catalog.getId()), catalog);
  }

  private Catalog buildLibrary(){
    Catalog catalog = new Catalog();
    catalog.setCourse("SpringBoot");
    catalog.setAuthor("Adhvik");
    catalog.setCategory(2022);
    catalog.setId("SAFe2022");
    return catalog;
  }

}
