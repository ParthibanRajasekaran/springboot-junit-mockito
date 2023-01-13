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
import com.parthibanrajasekaran.controller.LibraryController;
import com.parthibanrajasekaran.model.BookResponse;
import com.parthibanrajasekaran.model.Library;
import com.parthibanrajasekaran.repository.LibraryRepository;
import com.parthibanrajasekaran.service.LibraryService;
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
class LibraryControllerTests {

	@MockBean
	LibraryService libraryService;

	@Autowired
	LibraryController libraryController;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	LibraryRepository libraryRepository;

	@Test
	public void verifyAddBookIfBookDoesNotExistsWithoutServer(){
		Library library = buildLibrary();

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(buildLibrary());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		when(libraryService.createId(library.getIsbn(),library.getAisle())).thenReturn(library.getId());

		when(libraryService.verifyIfBookWithIdExists(library.getId())).thenReturn(false);

		// mocking repository.save and just return the required object
		when(libraryRepository.save(any())).thenReturn(library);

		try {
			assert requestBody != null;
			this.mockMvc.perform(post("/addBook")
							.contentType(MediaType.APPLICATION_JSON)
							.content(requestBody))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").value(library.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void AddBookIfBookExists(){
		Library library = buildLibrary();

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(buildLibrary());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		when(libraryService.createId(library.getIsbn(),library.getAisle())).thenReturn(library.getId());

		when(libraryService.verifyIfBookWithIdExists(library.getId())).thenReturn(true);

		// mocking repository.save and just return the required object
		when(libraryRepository.save(any())).thenReturn(library);

		try {
			assert requestBody != null;
			this.mockMvc.perform(post("/addBook")
							.contentType(MediaType.APPLICATION_JSON)
							.content(requestBody))
					.andDo(print())
					.andExpect(status().isAccepted())
					.andExpect(jsonPath("$.id").value(library.getId()))
					.andExpect(jsonPath("$.msg").value("Book already exists"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void verifyAddBookIfBookDoesNotExists(){
		Library library = buildLibrary();

		when(libraryService.createId(library.getIsbn(),library.getAisle())).thenReturn(library.getId());

		when(libraryService.verifyIfBookWithIdExists(library.getId())).thenReturn(false);

		ResponseEntity<BookResponse> response = libraryController.addBook(buildLibrary());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		BookResponse addBookResponse = response.getBody();
		assertEquals(library.getId(), addBookResponse.getId());
		assertEquals("Book has been successfully added", addBookResponse.getMsg());
	}

	@Test
	public void verifyAddBookIfBookExists(){
		Library library = buildLibrary();

		when(libraryService.createId(library.getIsbn(),library.getAisle())).thenReturn(library.getId());

		when(libraryService.verifyIfBookWithIdExists(library.getId())).thenReturn(true);

		ResponseEntity<BookResponse> response = libraryController.addBook(buildLibrary());
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

		BookResponse addBookResponse = response.getBody();
		assertEquals(library.getId(), addBookResponse.getId());
		assertEquals("Book already exists", addBookResponse.getMsg());
	}

	@Test
	public void getBooksByAuthorTest(){
		List<Library> library = new ArrayList<Library>();
		library.add(buildLibrary());
		library.add(buildLibrary());

		when(libraryRepository.findAllByAuthor(any())).thenReturn(library);

		try {
			this.mockMvc.perform(get("/getBook/author").param("authorName","Parthiban Rajasekaran"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.[0].id").value("SAFe2022"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateBookTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(updateLibrary());

		Library library = buildLibrary();
		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(true);
		when(libraryService.getBookById(any())).thenReturn(library);

		assert requestBody != null;
		this.mockMvc.perform(put("/updateBook/"+library.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andDo(print())
			.andExpect(status().isOk())
		  .andExpect(jsonPath("$.author").value(updateLibrary().getAuthor()));
	}

	@Test
	public void updateBookWhenBookDoesNotExistTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(updateLibrary());

		Library library = buildLibrary();
		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(false);
		when(libraryService.getBookById(any())).thenReturn(library);

		assert requestBody != null;
		this.mockMvc.perform(put("/updateBook/"+library.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteBookTest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(deleteLibrary());

		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(true);
		when(libraryService.getBookById(any())).thenReturn(deleteLibrary());
		doNothing().when(libraryRepository).delete(deleteLibrary());

		this.mockMvc.perform(delete("/deleteBook")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void deleteBookWhenBookDoesNotExistTest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		final String	requestBody = objectMapper.writeValueAsString(deleteLibrary());

		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(false);
		when(libraryService.getBookById(any())).thenReturn(deleteLibrary());
		doNothing().when(libraryRepository).delete(deleteLibrary());

		this.mockMvc.perform(delete("/deleteBook")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	public void getAllBooksTest() throws Exception {
		when(libraryRepository.findAll()).thenReturn(Collections.singletonList(buildLibrary()));

		this.mockMvc.perform(get("/getBooks"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value("SAFe2022"));
	}

	@Test
	public void getBookByIdTest() throws Exception {
		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(true);
		when(libraryService.getBookById(any())).thenReturn(buildLibrary());

		this.mockMvc.perform(get("/getBook/"+buildLibrary().getId()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value("SAFe2022"));
	}

	@Test
	public void getBookByIdWhenBookDoesNotExistTest() throws Exception {
		when(libraryService.verifyIfBookWithIdExists(any())).thenReturn(false);
		when(libraryService.getBookById(any())).thenReturn(buildLibrary());

		this.mockMvc.perform(get("/getBook/"+buildLibrary().getId()))
				.andDo(print())
				.andExpect(status().isNotFound());
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

	private Library updateLibrary(){
		Library library = new Library();
		library.setBook_name("mockito");
		library.setAuthor("Rooney");
		library.setAisle(2022);
		return library;
	}

	private Library deleteLibrary(){
		Library library = new Library();
		library.setId("SAFe2022");
		return library;
	}

}
