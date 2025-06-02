package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.PosController;
import org.stockify.dto.response.SessionPosResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionPosModelAssembler implements RepresentationModelAssembler<SessionPosResponse, EntityModel<SessionPosResponse>> {

    @NotNull
    @Override
    public EntityModel<SessionPosResponse> toModel(@NotNull SessionPosResponse entity) {
        return EntityModel.of(entity,
                // Self link to the session (no direct endpoint for getting a session by ID in PosController)
                linkTo(methodOn(PosController.class).getById(entity.getIdPos())).withRel("pos")
        );
    }
}