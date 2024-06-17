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
import java.util.Scanner;

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
		BookService bookservice = new BookService();
		Scanner scanner = new Scanner(System.in);

		try{
			//leemos el archivo json
			Resource resource = resourceLoader.getResource("classpath:books.json");
			InputStream inputStream = resource.getInputStream();
			books = objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});

		}catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true){
			System.out.println("Introduce la palabra para filtrar ('-1' para salir): ");
			String filter = scanner.nextLine();
			
			if("-1".equalsIgnoreCase(filter)){
				System.out.println("Hasta pronto!");
				break;
			}
			
			Optional<BookDate> result = bookservice.filter(filter, books);
			
			if(result.isPresent()) {
				BookDate bookDate = result.get();
					System.out.println("Resultado encontrado:");
					System.out.println("Título: " + bookDate.getTitle());
					System.out.println("Resumen: " + bookDate.getSummary());
					System.out.println("Fecha de publicación: " + bookDate.getPublicationDate());
					System.out.println("Fecha formateada: " + bookDate.getDate());
					System.out.println("Biografía del autor: " + bookDate.getAuthor().getBio());
			}else{
				System.out.println("No se encontraron resultados para tu búsqueda ("+filter+")...prueba con otra palabra.");
			}
		}
		scanner.close();
	}

}
