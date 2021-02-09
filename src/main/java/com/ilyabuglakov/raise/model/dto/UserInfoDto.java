package com.ilyabuglakov.raise.model.dto;

import com.ilyabuglakov.raise.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    String name;
    String surname;
    String oldPassword;
    String newPassword;
    String newPasswordRepeat;

    User user;
}
