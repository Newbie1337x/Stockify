package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.PosController;
import org.stockify.dto.request.pos.PosFilterRequest;
import org.stockify.dto.response.PosResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PosModelAssembler implements RepresentationModelAssembler<PosResponse, EntityModel<PosResponse>> {

    @NotNull
    @Override
    public EntityModel<PosResponse> toModel(@NotNull PosResponse entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PosController.class)
                        .getById(entity.getId()))
                        .withSelfRel(),

                linkTo(methodOn(PosController.class)
                        .getPos(new PosFilterRequest(), PageRequest.of(0,10), null))
                        .withRel("pos")
        );
    }
}
