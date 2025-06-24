package org.stockify.model.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.shift.ShiftController;
import org.stockify.dto.request.shift.ShiftCreateRequest;
import org.stockify.dto.request.shift.ShiftFilterRequest;
import org.stockify.dto.response.shift.ShiftResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShiftModelAssembler implements RepresentationModelAssembler<ShiftResponse, EntityModel<ShiftResponse>> {

    @Override
    public EntityModel<ShiftResponse> toModel(ShiftResponse shift) {
        return EntityModel.of(shift,
                linkTo(methodOn(ShiftController.class).getShift(shift.getId())).withSelfRel(),
                linkTo(methodOn(ShiftController.class).saveShift(new ShiftCreateRequest())).withRel("save"),
                linkTo(methodOn(ShiftController.class).listShifts(new ShiftFilterRequest(), null,null)).withRel("shifts"),
                linkTo(methodOn(ShiftController.class).listMyShifts(null,null)).withRel("my-shifts"),
                linkTo(methodOn(ShiftController.class).delete(shift.getId())).withRel("delete")
        );
    }
}