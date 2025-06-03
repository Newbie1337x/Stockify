package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.PosController;
import org.stockify.controller.SessionPosController;
import org.stockify.dto.response.SessionPosResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionPosModelAssembler implements RepresentationModelAssembler<SessionPosResponse, EntityModel<SessionPosResponse>> {

    @NotNull
    @Override
    public EntityModel<SessionPosResponse> toModel(@NotNull SessionPosResponse entity) {
        return EntityModel.of(entity,
                // Self link to the session
                linkTo(methodOn(SessionPosController.class).getSessionById(entity.getId())).withSelfRel(),
                // Link to the POS that this session belongs to
                linkTo(methodOn(PosController.class).getById(entity.getIdPos())).withRel("pos")
        );
    }
}
