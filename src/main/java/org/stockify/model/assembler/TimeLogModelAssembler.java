package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.EmployeeController;
import org.stockify.controller.TimeLogController;
import org.stockify.controller.store.StoreController;
import org.stockify.dto.response.TimeLogResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TimeLogModelAssembler implements RepresentationModelAssembler<TimeLogResponse, EntityModel<TimeLogResponse>> {

    @NotNull
    @Override
    public EntityModel<TimeLogResponse> toModel(@NotNull TimeLogResponse entity) {
        return EntityModel.of(entity,
                // Self link to the time log
                linkTo(methodOn(TimeLogController.class).getTimeLogById(entity.getId())).withSelfRel(),
                // Link to the employee that this time log belongs to
                linkTo(methodOn(EmployeeController.class).getEmployeeById(entity.getEmployeeId())).withRel("employee"),
                // Link to the store that this time log belongs to
                linkTo(methodOn(StoreController.class).getStoreById(entity.getStoreId())).withRel("store")
        );
    }
}