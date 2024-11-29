package com.msara.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthVerifyUserRequest(@NotNull int verificationCode, @NotBlank String email) {
}
