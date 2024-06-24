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
        
        //1. filtramos los libros con el valor introducido
        //2. ordenamos los libros filtrados para que este por fecha de publicacion y biografia mas corta
        //3. buscamos el libro mas reciente entre los ya filtrados
        //4. lo convertimos a bookdate
        Optional<BookDate> libroMasRecienteBuscado = books.stream()
                .filter(book -> book.getTitle().contains(filter) ||
                                book.getSummary().contains(filter) || 
                                book.getAuthor().getBio().contains(filter)) 
                .sorted(Comparator.comparing(Book::getPublicationDate, Comparator.nullsLast(LocalDate::compareTo))
                        .thenComparingInt(book -> Optional.ofNullable(book.getAuthor().getBio()).orElse("").length()))
                .findFirst()//obtenemos el primero de la lista
                .map(this::toBookDate);
        
        return libroMasRecienteBuscado;
    }
    
    private BookDate toBookDate (Book book){
        String formattedDate = book.getPublicationDate() != null ? book.getPublicationDate()
                .format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) : "";
        return new BookDate(
                book.getId(),
                book.getTitle(),
                book.getPages(),
                book.getSummary(),
                book.getPublicationDate(),
                book.getAuthor(),
                formattedDate);
    }
}
