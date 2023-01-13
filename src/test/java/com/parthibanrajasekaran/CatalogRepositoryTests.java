package com.parthibanrajasekaran;

import com.parthibanrajasekaran.controller.CatalogController;
import com.parthibanrajasekaran.model.Catalog;
import com.parthibanrajasekaran.repository.CatalogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogRepositoryTests {

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private CatalogController catalogController;

    @Test
    public void testFindAllByAuthor_Success() {
        Catalog course1 = new Catalog("Course1", "1", 1, "Author1");
        Catalog course2 = new Catalog("Course2", "2", 2, "Author2");
        Catalog course3 = new Catalog("Course3", "3", 3, "Author1");
        List<Catalog> courses = Arrays.asList(course1, course2, course3);

        when(catalogRepository.findAllByAuthor("Author1")).thenReturn(Arrays.asList(course1, course3));

        List<Catalog> result = catalogController.getCourseByAuthorName("Author1");
        assertEquals(2, result.size());
        assertEquals("Author1", result.get(0).getAuthor());
    }


}
