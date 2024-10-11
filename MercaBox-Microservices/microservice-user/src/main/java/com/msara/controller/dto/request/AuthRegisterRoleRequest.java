package com.msara.controller.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthRegisterRoleRequest(
        @Size(max = 2, message = "The user cannot have more that 2 roles") List<String> roleListName) {
}
