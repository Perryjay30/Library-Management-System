package com.maids.cc.librarymanagementsystem.book.service;


import com.maids.cc.librarymanagementsystem.book.dto.request.AddBookRequest;
import com.maids.cc.librarymanagementsystem.book.dto.request.EditBookRequest;
import com.maids.cc.librarymanagementsystem.book.model.Book;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;

import java.util.List;

public interface BookService {
    Response addBookToLibrary(AddBookRequest addBookRequest);
    Book retrieveBookById(Long bookId);
    Response editBookDetails(Long bookId, EditBookRequest editBookRequest);
    List<Book> retrieveAllBooks();
    Response removeBookFromLibrary(Long bookId);
}
