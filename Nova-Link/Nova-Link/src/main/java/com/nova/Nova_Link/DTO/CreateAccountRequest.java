package com.nova.Nova_Link.DTO;

import com.nova.Nova_Link.ENUMS.AccountType;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Account holder name is required")
    @Size(max = 100, message = "Account holder name must not exceed 100 characters")
    private String accountHolderName;
    @NotNull(message = "Account type is required")
    private AccountType type;
}
