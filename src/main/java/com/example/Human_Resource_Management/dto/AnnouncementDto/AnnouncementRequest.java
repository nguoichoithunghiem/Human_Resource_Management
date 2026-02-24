package com.example.Human_Resource_Management.dto.AnnouncementDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Content must not exceed 2000 characters")
    private String content;

    @NotNull(message = "CreatedBy is required")
    @Positive(message = "CreatedBy must be a valid id")
    private Long createdBy;
}