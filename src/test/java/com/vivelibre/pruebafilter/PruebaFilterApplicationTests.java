package com.vivelibre.pruebafilter;

import com.vivelibre.pruebafilter.pojos.Author;
import com.vivelibre.pruebafilter.pojos.Book;
import com.vivelibre.pruebafilter.pojos.BookDate;
import com.vivelibre.pruebafilter.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PruebaFilterApplicationTests {

	private BookService bookService;
	private List<Book> books;

	@BeforeEach
	void setUp() {
		bookService = new BookService();
		//cargamos nuestra lista de libros para pruebas
		books = new ArrayList<>();
		books.add(new Book(1, "The Hunger Games", 374, "Summary of the hunger games",
				null, new Author("Suzanne", "Collins", "Bio de Suzanne Collins")));
		books.add(new Book(2, "Harry Potter and the Sorcerer's Stone", 309, "Summary of harry potter",
				LocalDate.of(1997, 6, 26), new Author("J.K.", "Rowling", "Bio of J.K. Rowling")));
		books.add(new Book(3, "A Game of Thrones", 694, "Summary of Game of Thrones",
				LocalDate.of(1996, 8, 6), new Author("George", "Martin", "Bio of George R.R. Martin")));
		books.add(new Book(4, "Catching Fire", 391, "Summary of Catching Fire",
				LocalDate.of(1998, 8, 6), new Author("Suzanne", "Collins", "Another bio of Suzanne Collins")));
	}

	@Test
	void testFilterWithNoPublicationDate() {
		Optional<BookDate> result = bookService.filter("Hunger", books);
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	void testFilterWithPublicationDate() {
		Optional<BookDate> result = bookService.filter("Harry", books);
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals("Harry Potter and the Sorcerer's Stone", result.get().getTitle());
		Assertions.assertEquals("06-26-1997", result.get().getDate());
	}

	@Test
	void testFilterWithMultipleMatches() {
		Optional<BookDate> result = bookService.filter("Collins", books);
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals("Catching Fire", result.get().getTitle());
	}

	@Test
	void testFilterWithNoMatches() {
		Optional<BookDate> result = bookService.filter("Nonexistent", books);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	void testFilterWithEmptyList() {
		Optional<BookDate> result = bookService.filter("Harry", new ArrayList<>());
		Assertions.assertFalse(result.isPresent());
	}

}
