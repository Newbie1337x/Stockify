package org.stockify.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.stockify.model.service.EmployeeService;

/**
 * Component that runs initialization tasks when the application starts.
 * Currently sets all employees to OFFLINE status on startup.
 */
@Component
@RequiredArgsConstructor
public class StartupConfig implements ApplicationRunner {

    private final EmployeeService employeeService;

    @Override
    public void run(ApplicationArguments args) {
        // Set all employees to OFFLINE when the server starts
        employeeService.setAllEmployeesToOffline();
    }
}