package com.vivelibre.pruebafilter.service;

import com.vivelibre.pruebafilter.pojos.Book;
import com.vivelibre.pruebafilter.pojos.BookDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {

    /**
     * Método que filtra y muestra por pantalla libros siguiente los criterios dados.
     * @param filter
     * @param books
     * @return Optional(bookdate>optional de libro con la fecha pedida
     */
    public Optional<BookDate> filter (String filter, List<Book> books){
        
        //mostramos los libros que no tienen fecha de publicacion
        books.stream()
                .filter(book -> book.getPublicationDate() == null)
                .forEach(book -> System.out.println("Libro sin fecha de publicación: " + book.getTitle()));
        
        //filtramos los libros con el valor introducido
        List<Book> librosFiltrados = books.stream()
                .filter(book -> book.getTitle().contains(filter) ||
                                book.getSummary().contains(filter) || 
                                book.getAuthor().getBio().contains(filter)).collect(Collectors.toList());
        
        if (librosFiltrados.isEmpty()){
            return Optional.empty();
        }else {//ordenamos los libros filtrados para que este por fecha de publicacion y biografia mas corta
            librosFiltrados.sort(Comparator.comparing(Book::getPublicationDate, Comparator.nullsLast(LocalDate::compareTo))
                    .thenComparingInt(book -> Optional.ofNullable(book.getAuthor().getBio()).orElse("").length()));
        }
        
        //buscamos el libro mas reciente entre los ya filtrados que tengan fechas
        Book libroMasReciente = librosFiltrados.stream()
                .filter(book -> book.getPublicationDate() != null)
                .max(Comparator.comparing(Book::getPublicationDate))
                .orElse(null);
        
        if(libroMasReciente == null){
            return Optional.empty();
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String formattedDate = libroMasReciente.getPublicationDate().format(formatter);
            BookDate bookDate = new BookDate(libroMasReciente.getId(),
                libroMasReciente.getTitle(),
                libroMasReciente.getPages(),
                libroMasReciente.getSummary(),
                libroMasReciente.getPublicationDate(),
                libroMasReciente.getAuthor(),
                formattedDate);
            return Optional.of(bookDate);
        }
    }
}
