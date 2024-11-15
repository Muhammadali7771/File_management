package org.example.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookCreateDTO {
    private String title;
    private String overview;
    private MultipartFile[] files;
}
