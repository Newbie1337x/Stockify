package org.stockify.model.assembler;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.EmployeeController;
import org.stockify.dto.response.EmployeeResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<EmployeeResponse, EntityModel<EmployeeResponse>> {

    @NotNull
    @Override
    public EntityModel<EmployeeResponse> toModel(@NotNull EmployeeResponse entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(entity.getId())).withSelfRel(),
                linkTo(EmployeeController.class).slash("employee").withSelfRel()
        );
    }
}
