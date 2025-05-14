package org.stockify.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderResponse {

    private Long id;
    private String businessName;
    private String taxId;
    private String taxAddress;
    private String phone;
    private String email;
    private String name;
    private boolean active;

}