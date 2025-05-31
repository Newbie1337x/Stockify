package org.stockify.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderFilterRequest {

    private String name;
    private String businessName;
    private String taxId;
    private String email;
    private String phone;
    private String active;
    private Long id;
    private String taxAddress;

}
