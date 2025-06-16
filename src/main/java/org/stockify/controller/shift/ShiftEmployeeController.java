package org.stockify.controller.shift;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.EmployeeResponse;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.assembler.ShiftModelAssembler;
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.service.ShiftService;
import java.util.List;

@RestController
@RequestMapping("/shifts/{shiftId}/employees")
@Tag(name = "Shift Employees", description = "Endpoints for managing employees assigned to a shift")
public class ShiftEmployeeController {
    private final ShiftModelAssembler shiftModelAssembler;
    private final ShiftService shiftService;

    public ShiftEmployeeController(ShiftModelAssembler shiftModelAssembler, ShiftService shiftService, ShiftMapper shiftMapper) {
        this.shiftModelAssembler = shiftModelAssembler;
        this.shiftService = shiftService;
    }

    @Operation(summary = "Remove all employees from a shift", description = "Removes every employee assigned to the specified shift.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees removed successfully"),
            @ApiResponse(responseCode = "404", description = "Shift not found")
    })
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('DELETE')")
    public ResponseEntity<EntityModel<ShiftResponse>> removeAllEmployeesFromShift(
            @Parameter(description = "ID of the shift", required = true)
            @PathVariable Long shiftId) {
        return ResponseEntity.ok(
                shiftModelAssembler.toModel(
                        shiftService.deleteAllEmployeesFromShift(shiftId)
                )
        );
    }

    @Operation(summary = "Remove a specific employee from a shift", description = "Removes a single employee from the specified shift.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee removed from shift"),
            @ApiResponse(responseCode = "404", description = "Shift or employee not found")
    })
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('DELETE')")
    public ResponseEntity<EntityModel<ShiftResponse>> removeEmployeeFromShift(
            @Parameter(description = "ID of the shift", required = true) @PathVariable Long shiftId,
            @Parameter(description = "ID of the employee to remove", required = true) @PathVariable Long employeeId) {
        return ResponseEntity.ok(
                shiftModelAssembler.toModel(
                        shiftService.deleteEmployeeFromShift(shiftId, employeeId)
                )
        );
    }

    @Operation(summary = "Add an employee to a shift", description = "Assigns an employee to the specified shift.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee added to shift"),
            @ApiResponse(responseCode = "404", description = "Shift or employee not found")
    })
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ShiftResponse>> addEmployeetoShift(
            @Parameter(description = "ID of the shift", required = true) @PathVariable Long shiftId,
            @Parameter(description = "ID of the employee to add", required = true) @PathVariable Long employeeId) {
        return ResponseEntity.ok(
                shiftModelAssembler.toModel(
                        shiftService.addEmployeeToShift(shiftId, employeeId)
                )
        );
    }

    @Operation(summary = "Get all employees in a shift", description = "Returns the list of all employees assigned to the given shift.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of employees retrieved"),
            @ApiResponse(responseCode = "404", description = "Shift not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('READ') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesFromShift(
            @Parameter(description = "ID of the shift to get employees for", required = true)
            @PathVariable Long shiftId) {
        List<EmployeeResponse> employeeResponses = shiftService.findEmployeesByShiftId(shiftId);
        return ResponseEntity.ok(employeeResponses);
    }
}
