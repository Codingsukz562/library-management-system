package com.library.patterns;


import com.library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

public class TitleSearchStrategy implements SearchStrategy 
{
    @Override
    public List<Book> search(List<Book> books, String query) 
    {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}