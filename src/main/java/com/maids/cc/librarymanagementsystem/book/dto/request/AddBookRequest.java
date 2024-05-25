package com.maids.cc.librarymanagementsystem.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBookRequest {
    @NotBlank(message = "This field is required")
    private String title;
    @NotBlank(message = "This field is required")
    private String author;
    @NotBlank(message = "This field is required")
    private String publicationYear;
    @NotBlank(message = "This field is required")
    private String ISBN;
}
