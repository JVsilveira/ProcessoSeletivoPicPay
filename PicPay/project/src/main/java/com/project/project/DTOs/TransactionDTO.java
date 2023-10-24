package com.project.project.DTOs;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, Long senderId, Long recieverId) {

}
