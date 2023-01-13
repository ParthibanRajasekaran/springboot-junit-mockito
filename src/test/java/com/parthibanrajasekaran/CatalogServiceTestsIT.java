package com.parthibanrajasekaran;

import com.parthibanrajasekaran.model.Catalog;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CatalogServiceTestsIT {

    @Test
    public void getAuthorNameForCourseTest() throws JSONException {

        String expected = "[\n"
                + "    {\n"
                + "        \"course\": \"Appium\",\n"
                + "        \"id\": \"Appium44\",\n"
                + "        \"category\": 44,\n"
                + "        \"author\": \"Max Musterman\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"course\": \"UFT\",\n"
                + "        \"id\": \"UFT20225\",\n"
                + "        \"category\": 20225,\n"
                + "        \"author\": \"Parthiban\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"course\": \"RESTAssured\",\n"
                + "        \"id\": \"RESTAssured2025\",\n"
                + "        \"category\": 2025,\n"
                + "        \"author\": \"Parthiban\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"course\": \"SAFe\",\n"
                + "        \"id\": \"SAFe2023\",\n"
                + "        \"category\": 2023,\n"
                + "        \"author\": \"Rocky\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"course\": \"Selenium\",\n"
                + "        \"id\": \"Selenium2024\",\n"
                + "        \"category\": 2024,\n"
                + "        \"author\": \"Parthiban Rajasekaran\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"course\": \"SpringBoot\",\n"
                + "        \"id\": \"SpringBoot2022\",\n"
                + "        \"category\": 2022,\n"
                + "        \"author\": \"Parthiban Rajasekaran\"\n"
                + "    }\n"
                + "]";
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getCourses", String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    //  @Test
    public void addCourseTest() {
        TestRestTemplate restTemplate = new TestRestTemplate();

        //request header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // request body
        HttpEntity<Catalog> request = new HttpEntity<>(buildInventoryItem(), httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addCourse", request, String.class);
        System.out.println(response.getStatusCode().value());
        System.out.println(response.getBody());
        assertEquals(201, response.getStatusCode().value());
    }

    private Catalog buildInventoryItem() {
        Catalog catalog = new Catalog();
        catalog.setCourse("Springboot");
        catalog.setAuthor("Adhvik");
        catalog.setCategory(2022);
        return catalog;
    }

}
