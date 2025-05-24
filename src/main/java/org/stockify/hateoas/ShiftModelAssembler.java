package org.stockify.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.ShiftController;
import org.stockify.dto.response.shift.ShiftResponse;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShiftModelAssembler implements RepresentationModelAssembler<ShiftResponse, EntityModel<ShiftResponse>> {
    @Override
    public EntityModel<ShiftResponse> toModel(ShiftResponse shiftResponse) {
        return EntityModel.of(shiftResponse,
                linkTo(methodOn(ShiftController.class).getShiftById(shiftResponse.getId())).withSelfRel(),
                linkTo(methodOn(ShiftController.class).getAllShifts(0, 20, null)).withRel("clients"),
                linkTo(methodOn(ShiftController.class).deleteShiftById(shiftResponse.getId())).withRel("delete"),
                linkTo(methodOn(ShiftController.class).putShift(shiftResponse.getId(), null)).withRel("update"),
                linkTo(methodOn(ShiftController.class).patchShift(shiftResponse.getId(), null)).withRel("partial-update"));
    }
}
