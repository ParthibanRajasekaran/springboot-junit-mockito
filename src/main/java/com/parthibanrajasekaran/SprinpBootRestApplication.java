package com.parthibanrajasekaran;

import com.parthibanrajasekaran.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SprinpBootRestApplication {
//public class SprinpBootRestApplication implements CommandLineRunner {

	@Autowired
	LibraryRepository libraryRepository;

	public static void main(String[] args) {
		SpringApplication.run(SprinpBootRestApplication.class, args);
	}

/*	@Override
	public void run(String... args) {
		Library library = libraryRepository.findById("fdsefr343").get();
		System.out.println(library.getAuthor());

		Library library1 = new Library();
		library1.setAisle(123);
		library1.setAuthor("Anushka");
		library1.setBook_name("Selenium");
		library1.setIsbn("slnm");
		library1.setId("slnm123");
		libraryRepository.save(library1);

		List<Library> allRecords = libraryRepository.findAll();
		for(Library record:allRecords){
			System.out.println(record.getAuthor());
			System.out.println(record.getBook_name());
		}

		libraryRepository.delete(library1);
	}*/
}
