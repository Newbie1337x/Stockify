package org.stockify.model.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controllers.ClientController;
import org.stockify.dto.response.ClientResponse;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientModelAssembler implements RepresentationModelAssembler<ClientResponse, EntityModel<ClientResponse>> {

    @Override
    public EntityModel<ClientResponse> toModel(ClientResponse clientResponse) {
        return EntityModel.of(clientResponse,
                linkTo(methodOn(ClientController.class).getClientById(clientResponse.getId())).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAllClients(0, 20, null)).withRel("clients"),
                linkTo(methodOn(ClientController.class).deleteClientById(clientResponse.getId())).withRel("delete"),
                linkTo(methodOn(ClientController.class).putClient(clientResponse.getId(), null)).withRel("update"),
                linkTo(methodOn(ClientController.class).patchClient(clientResponse.getId(), null)).withRel("partial-update"));
    }
}