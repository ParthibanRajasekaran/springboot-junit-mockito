package com.parthibanrajasekaran;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthibanrajasekaran.controller.CatalogController;
import com.parthibanrajasekaran.model.CourseResponse;
import com.parthibanrajasekaran.model.Catalog;
import com.parthibanrajasekaran.repository.CatalogRepository;
import com.parthibanrajasekaran.service.CatalogService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class CatalogControllerTests {

	@MockBean
	CatalogService catalogService;

	@Autowired
	CatalogController catalogController;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CatalogRepository catalogRepository;

	@Test
	public void verifyAddCourseIfCourseDoesNotExistsWithoutServer(){
		Catalog catalog = buildInventoryItem();

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(buildInventoryItem());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		when(catalogService.createId(catalog.getCourse(), catalog.getCategory())).thenReturn(catalog.getId());

		when(catalogService.verifyIfCourseWithIdExists(catalog.getId())).thenReturn(false);

		// mocking repository.save and just return the required object
		when(catalogRepository.save(any())).thenReturn(catalog);

		try {
			assert requestBody != null;
			this.mockMvc.perform(post("/addCourse")
							.contentType(MediaType.APPLICATION_JSON)
							.content(requestBody))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").value(catalog.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void verifyAddCourseIfCourseExistsWithoutServer(){
		Catalog catalog = buildInventoryItem();

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(buildInventoryItem());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		when(catalogService.createId(catalog.getCourse(), catalog.getCategory())).thenReturn(catalog.getId());

		when(catalogService.verifyIfCourseWithIdExists(catalog.getId())).thenReturn(true);

		// mocking repository.save and just return the required object
		when(catalogRepository.save(any())).thenReturn(catalog);

		try {
			assert requestBody != null;
			this.mockMvc.perform(post("/addCourse")
							.contentType(MediaType.APPLICATION_JSON)
							.content(requestBody))
					.andDo(print())
					.andExpect(status().isAccepted())
					.andExpect(jsonPath("$.id").value(catalog.getId()))
					.andExpect(jsonPath("$.msg").value("Course already exists"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void verifyAddCourseIfCourseDoesNotExists(){
		Catalog catalog = buildInventoryItem();

		when(catalogService.createId(catalog.getCourse(), catalog.getCategory())).thenReturn(catalog.getId());

		when(catalogService.verifyIfCourseWithIdExists(catalog.getId())).thenReturn(false);

		ResponseEntity<CourseResponse> response = catalogController.addCourse(buildInventoryItem());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		CourseResponse addCourseResponse = response.getBody();
		assertEquals(catalog.getId(), addCourseResponse.getId());
		assertEquals("Course has been successfully added", addCourseResponse.getMsg());
	}

	@Test
	public void verifyAddCourseIfCourseExists(){
		Catalog catalog = buildInventoryItem();

		when(catalogService.createId(catalog.getCourse(), catalog.getCategory())).thenReturn(catalog.getId());

		when(catalogService.verifyIfCourseWithIdExists(catalog.getId())).thenReturn(true);

		ResponseEntity<CourseResponse> response = catalogController.addCourse(buildInventoryItem());
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

		CourseResponse addCourseResponse = response.getBody();
		assertEquals(catalog.getId(), addCourseResponse.getId());
		assertEquals("Course already exists", addCourseResponse.getMsg());
	}

	@Test
	public void getCoursesByAuthorTest(){
		List<Catalog> catalog = new ArrayList<Catalog>();
		catalog.add(buildInventoryItem());
		catalog.add(buildInventoryItem());

		when(catalogRepository.findAllByAuthor(any())).thenReturn(catalog);

		try {
			this.mockMvc.perform(get("/getCourse/author").param("authorName","Parthiban Rajasekaran"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.[0].id").value("SAFe2022"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateCourseTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(updateInventoryItem());

		Catalog catalog = buildInventoryItem();
		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(true);
		when(catalogService.getCourseById(any())).thenReturn(catalog);

		assert requestBody != null;
		this.mockMvc.perform(put("/updateCourse/"+ catalog.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andDo(print())
			.andExpect(status().isOk())
		  .andExpect(jsonPath("$.author").value(updateInventoryItem().getAuthor()));
	}

	@Test
	public void updateCourseWhenCourseDoesNotExistTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(updateInventoryItem());

		Catalog catalog = buildInventoryItem();
		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(false);
		when(catalogService.getCourseById(any())).thenReturn(catalog);

		assert requestBody != null;
		this.mockMvc.perform(put("/updateCourse/"+ catalog.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteCourseTest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(deleteInventoryItem());

		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(true);
		when(catalogService.getCourseById(any())).thenReturn(deleteInventoryItem());
		doNothing().when(catalogRepository).delete(deleteInventoryItem());

		this.mockMvc.perform(delete("/deleteCourse")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void deleteCourseWhenCourseDoesNotExistTest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(deleteInventoryItem());

		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(false);
		when(catalogService.getCourseById(any())).thenReturn(deleteInventoryItem());
		doNothing().when(catalogRepository).delete(deleteInventoryItem());

		this.mockMvc.perform(delete("/deleteCourse")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void getAllCoursesTest() throws Exception {
		when(catalogRepository.findAll()).thenReturn(Collections.singletonList(buildInventoryItem()));

		this.mockMvc.perform(get("/getCourses"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value("SAFe2022"));
	}

	@Test
	public void getCourseByIdTest() throws Exception {
		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(true);
		when(catalogService.getCourseById(any())).thenReturn(buildInventoryItem());

		this.mockMvc.perform(get("/getCourse/"+ buildInventoryItem().getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value("SAFe2022"));
	}

	@Test
	public void getCourseByIdWhenCourseDoesNotExistTest() throws Exception {
		when(catalogService.verifyIfCourseWithIdExists(any())).thenReturn(false);
		when(catalogService.getCourseById(any())).thenReturn(buildInventoryItem());

		this.mockMvc.perform(get("/getCourse/"+ buildInventoryItem().getId()))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCatalog() {
		Catalog catalog = new Catalog("Course1", "1", 1, "Author1");

		assertEquals("Course1", catalog.getCourse());
		assertEquals("1", catalog.getId());
		assertEquals(1, catalog.getCategory());
		assertEquals("Author1", catalog.getAuthor());
	}

	private Catalog buildInventoryItem(){
		Catalog catalog = new Catalog();
		catalog.setCourse("SpringBoot");
		catalog.setAuthor("Adhvik");
		catalog.setCategory(2022);
		catalog.setId("SAFe2022");
		return catalog;
	}

	private Catalog updateInventoryItem(){
		Catalog catalog = new Catalog();
		catalog.setCourse("mockito");
		catalog.setAuthor("Rooney");
		catalog.setCategory(2022);
		return catalog;
	}

	private Catalog deleteInventoryItem(){
		Catalog catalog = new Catalog();
		catalog.setId("SAFe2022");
		return catalog;
	}

}
