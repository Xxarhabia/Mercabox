package com.msara.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(@NotBlank String username, @NotBlank String email, @NotBlank String password,
                                  @NotBlank String confirmPassword, @Valid AuthRegisterRoleRequest roleRequest) {
}
