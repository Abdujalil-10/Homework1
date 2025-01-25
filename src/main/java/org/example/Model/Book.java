package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
    private int id;
    private String name;
    private String genre;
    private int author_id;
    private Author author;
}
