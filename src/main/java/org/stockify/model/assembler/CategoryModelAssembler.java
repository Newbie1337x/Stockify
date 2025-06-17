package org.stockify.model.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.stockify.controller.CategoryController;
import org.stockify.dto.response.CategoryResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<CategoryResponse, EntityModel<CategoryResponse>> {

    @NotNull
    @Override
    public EntityModel<CategoryResponse> toModel(@NotNull CategoryResponse categoryResponse) {
        return EntityModel.of(categoryResponse,
                linkTo(methodOn(CategoryController.class)
                        .getCategoryById(categoryResponse.id()))
                        .withSelfRel()
        );
    }
}