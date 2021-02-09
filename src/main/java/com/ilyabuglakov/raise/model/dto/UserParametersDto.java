package com.ilyabuglakov.raise.model.dto;

import com.ilyabuglakov.raise.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserParametersDto {
    User user;
    List<UserCharacteristic> userCharacteristics;
    int testsSolved;
    int testsCreated;
    int commentsPosted;
}
