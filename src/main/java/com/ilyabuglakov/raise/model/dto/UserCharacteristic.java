package com.ilyabuglakov.raise.model.dto;

import com.ilyabuglakov.raise.domain.type.Characteristic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCharacteristic {
    Characteristic characteristic;
    double score;
}
