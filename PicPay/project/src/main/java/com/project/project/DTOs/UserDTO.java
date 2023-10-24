package com.project.project.DTOs;

import com.project.project.domain.user.UserType;
import java.math.BigDecimal;

public record UserDTO(UserType userType,
    String firstName, String lastName,
    String document, String email, BigDecimal balance,
    String password) {

}
