package com.maids.cc.librarymanagementsystem.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditBookRequest {
    private String title;
    private String author;
    private String publicationYear;
    private String ISBN;
}
