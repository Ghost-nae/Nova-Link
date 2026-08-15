package com.nova.Nova_Link.DTO;

import com.nova.Nova_Link.ENUMS.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    private String accountHolderName;
    private AccountType type;
}
