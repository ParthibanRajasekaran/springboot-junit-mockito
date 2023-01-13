package com.parthibanrajasekaran;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.parthibanrajasekaran.model.Library;
import org.assertj.core.api.Assert;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class LibraryServiceTestsIT {

  @Test
  public void getAuthorNameBooksTest() throws JSONException {

    String expected = "[\n"
        + "    {\n"
        + "        \"book_name\": \"appium\",\n"
        + "        \"id\": \"appium20225\",\n"
        + "        \"aisle\": 20225,\n"
        + "        \"isbn\": \"appium\",\n"
        + "        \"author\": \"Parthiban Rajasekaran\"\n"
        + "    },\n"
        + "    {\n"
        + "        \"book_name\": \"HP UFT\",\n"
        + "        \"id\": \"HP UFT20225\",\n"
        + "        \"aisle\": 20225,\n"
        + "        \"isbn\": \"HP UFT\",\n"
        + "        \"author\": \"Parthiban\"\n"
        + "    },\n"
        + "    {\n"
        + "        \"book_name\": \"REST Assured\",\n"
        + "        \"id\": \"REST Assured20225\",\n"
        + "        \"aisle\": 20225,\n"
        + "        \"isbn\": \"REST Assured\",\n"
        + "        \"author\": \"Parthiban\"\n"
        + "    },\n"
        + "    {\n"
        + "        \"book_name\": \"SpringBoot\",\n"
        + "        \"id\": \"SAFe2022\",\n"
        + "        \"aisle\": 2022,\n"
        + "        \"isbn\": \"SAFe\",\n"
        + "        \"author\": \"Adhvik\"\n"
        + "    },\n"
        + "    {\n"
        + "        \"book_name\": \"selenium\",\n"
        + "        \"id\": \"selenium20225\",\n"
        + "        \"aisle\": 20225,\n"
        + "        \"isbn\": \"selenium\",\n"
        + "        \"author\": \"Parthiban Rajasekaran\"\n"
        + "    },\n"
        + "    {\n"
        + "        \"book_name\": \"Spring\",\n"
        + "        \"id\": \"Spring2022\",\n"
        + "        \"aisle\": 2022,\n"
        + "        \"isbn\": \"Spring\",\n"
        + "        \"author\": \"Adhvik\"\n"
        + "    }\n"
        + "]";
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks", String.class);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());

    JSONAssert.assertEquals(expected, response.getBody(),false);
  }

//  @Test
  public void addBookTest(){
    TestRestTemplate restTemplate = new TestRestTemplate();

    //request header
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    // request body
    HttpEntity<Library> request = new HttpEntity<>(buildLibrary(),httpHeaders);

    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
    System.out.println(response.getStatusCode().value());
    System.out.println(response.getBody());
    assertEquals(201, response.getStatusCode().value());
  }

  private Library buildLibrary(){
    Library library = new Library();
    library.setBook_name("Spring");
    library.setAuthor("Adhvik");
    library.setAisle(2022);
    library.setIsbn("Spring");
    return library;
  }

}
