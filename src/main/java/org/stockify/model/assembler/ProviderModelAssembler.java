package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.provider.ProviderController;
import org.stockify.dto.request.provider.ProviderFilterRequest;
import org.stockify.dto.response.ProviderResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProviderModelAssembler implements RepresentationModelAssembler<ProviderResponse, EntityModel<ProviderResponse>> {

    @NotNull
    @Override
    public EntityModel<ProviderResponse> toModel(@NotNull ProviderResponse providerResponse) {
        return EntityModel.of(providerResponse,
                linkTo(methodOn(ProviderController.class)
                        .getProviderById(providerResponse.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ProviderController.class)
                        .listProviders(new ProviderFilterRequest(), PageRequest.of(0, 10), null))
                        .withRel("providers")
        );
    }
}
