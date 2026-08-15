package com.nova.Nova_Link.DTO;

import com.nova.Nova_Link.ENUMS.AccountType;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;
    @NotNull(message = "Account type is required")
    private AccountType type;
}
