package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.PosController;
import org.stockify.dto.response.SessionPosCreateResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionPosCreateModelAssembler implements RepresentationModelAssembler<SessionPosCreateResponse, EntityModel<SessionPosCreateResponse>> {

    @NotNull
    @Override
    public EntityModel<SessionPosCreateResponse> toModel(@NotNull SessionPosCreateResponse entity) {
        return EntityModel.of(entity,
                // Link to the associated POS
                linkTo(methodOn(PosController.class).getById(entity.getIdPos())).withRel("pos")
        );
    }
}