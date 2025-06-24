package org.stockify.model.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.shift.ShiftsController;
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
                linkTo(methodOn(ShiftsController.class).getShift(shift.getId())).withSelfRel(),
                linkTo(methodOn(ShiftsController.class).saveShift(new ShiftCreateRequest())).withRel("save"),
                linkTo(methodOn(ShiftsController.class).listShifts(new ShiftFilterRequest(), null,null)).withRel("shifts"),
                linkTo(methodOn(ShiftsController.class).listMyShifts(null,null)).withRel("my-shifts"),
                linkTo(methodOn(ShiftsController.class).delete(shift.getId())).withRel("delete")
        );
    }
}