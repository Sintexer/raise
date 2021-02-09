package com.ilyabuglakov.raise.domain;

import com.ilyabuglakov.raise.domain.type.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends Entity {

    private String email;
    private String name;
    private String surname;
    private String password;
    private Status status;
    private LocalDate registrationDate;

}
