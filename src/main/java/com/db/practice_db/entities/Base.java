package com.db.practice_db.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
public class Base {

    @NotBlank
    private String  name;
    @NotBlank
    private String description;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date date;



    public Base(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
