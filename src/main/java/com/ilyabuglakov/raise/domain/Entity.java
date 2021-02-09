package com.ilyabuglakov.raise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Base class for all database entities
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Entity {

    private Integer id;

}
