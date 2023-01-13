package com.parthibanrajasekaran.controller;

import com.parthibanrajasekaran.model.Catalog;
import com.parthibanrajasekaran.model.CourseResponse;
import com.parthibanrajasekaran.repository.CatalogRepository;
import com.parthibanrajasekaran.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
public class CatalogController {

    private static final Logger log = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    CourseResponse courseResponse;

    @Autowired
    CatalogService catalogService;

    @PostMapping("/addCourse")
    public ResponseEntity<CourseResponse> addCourse(@RequestBody Catalog catalog) {

        final String id = catalogService.createId(catalog.getCourse(), catalog.getCategory());

        if (!catalogService.verifyIfCourseWithIdExists(id)) {
            catalog.setId(id);
            // JPA repository is used to flush data from the input to the DB via save
            catalogRepository.save(catalog);

            // Creating response headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("unique", id);
            log.debug("Unique Id is added to the header successfully");

            // Adding response message for the request
            courseResponse.setMsg("Course has been successfully added");
            courseResponse.setId(id);
            log.info(String.format("Course with id: %s added successfully to the catalog", id));
            return new ResponseEntity<CourseResponse>(courseResponse, httpHeaders, HttpStatus.CREATED);
        } else {
            log.debug("Course already exists - Duplicate entry");
            courseResponse.setMsg("Course already exists");
            courseResponse.setId(id);
            return new ResponseEntity<>(courseResponse, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/getCourse/{id}")
    public ResponseEntity getCourseById(@PathVariable(value = "id") String id) {
        if (catalogService.verifyIfCourseWithIdExists(id)) {
            Catalog catalog = catalogService.getCourseById(id);
            return new ResponseEntity<Catalog>(catalog, HttpStatus.FOUND);
        } else {
            log.info(MessageFormat.format("Course with id: {0} does not exists", id));
            courseResponse.setMsg(String.format("Course with id: %s does not exists", id));
            return new ResponseEntity<>(courseResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getCourse/author")
    public List<Catalog> getCourseByAuthorName(@RequestParam(value = "authorName") String author) {
        return catalogRepository.findAllByAuthor(author);
    }

    @GetMapping("/getCourses")
    public List<Catalog> getAllCourses() {
        log.info("Retrieving all the Courses from the database");
        return catalogRepository.findAll();
    }

    //  @PostMapping("/updateCourse/{id}")
    @PutMapping("/updateCourse/{id}")
    public ResponseEntity updateCourse(@PathVariable(value = "id") String id, @RequestBody Catalog catalog) {
        if (catalogService.verifyIfCourseWithIdExists(id)) {
            Catalog catalogToBeUpdated = catalogService.getCourseById(id);

            catalogToBeUpdated.setCategory(catalog.getCategory());
            catalogToBeUpdated.setAuthor(catalog.getAuthor());
            catalogToBeUpdated.setCourse(catalog.getCourse());
            catalogRepository.save(catalogToBeUpdated);
            log.debug(String.format("Course with id: %s updated successfully", id));
            return new ResponseEntity<Catalog>(catalogToBeUpdated, HttpStatus.OK);
        } else {
            log.info(MessageFormat.format("Course with id: {0} does not exists", id));
            courseResponse.setMsg(String.format("Course with id: %s does not exists", id));
            return new ResponseEntity<>(courseResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity deleteCourse(@RequestBody Catalog catalog) {

        final String id = catalog.getId();
        if (catalogService.verifyIfCourseWithIdExists(id)) {
            Catalog catalogToBeDeleted = catalogService.getCourseById(id);
            catalogRepository.delete(catalogToBeDeleted);
            log.debug(String.format("Course with id: %s deleted successfully", id));
            return new ResponseEntity<>(String.format("Course with id: %s deleted successfully", id), HttpStatus.OK);
        } else {
            log.info(MessageFormat.format("Course with id: {0} does not exists", id));
            courseResponse.setMsg(String.format("Course with id: %s does not exists", id));
            return new ResponseEntity<>(courseResponse, HttpStatus.NOT_FOUND);
        }
    }

}
