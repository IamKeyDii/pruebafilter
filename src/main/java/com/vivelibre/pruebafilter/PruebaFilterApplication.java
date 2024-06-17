package com.vivelibre.pruebafilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivelibre.pruebafilter.pojos.Book;
import com.vivelibre.pruebafilter.pojos.BookDate;
import com.vivelibre.pruebafilter.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class PruebaFilterApplication implements CommandLineRunner {
	
	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(PruebaFilterApplication.class, args);
	}
	
	@Override
	public void run (String... args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Book> books = Collections.emptyList();

		String filter = "Potter";

		try{
			//leemos el archivo json
			Resource resource = resourceLoader.getResource("classpath:books.json");
			InputStream inputStream = resource.getInputStream();
			books = objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});

		}catch (IOException e) {
			e.printStackTrace();
		}

		BookService bookservice = new BookService();
		Optional<BookDate> result = bookservice.filter(filter, books);

		result.ifPresent(bookDate -> {
			System.out.println("Resultado encontrado:");
			System.out.println("Título: " + bookDate.getTitle());
			System.out.println("Resumen: " + bookDate.getSummary());
			System.out.println("Fecha de publicación: " + bookDate.getPublicationDate());
			System.out.println("Fecha formateada: " + bookDate.getDate());
			System.out.println("Biografía del autor: " + bookDate.getAuthor().getBio());
		});
	}

}
