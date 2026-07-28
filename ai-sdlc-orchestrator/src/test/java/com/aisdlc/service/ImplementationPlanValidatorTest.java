package com.aisdlc.service;

import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.ImplementationStep;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ImplementationPlanValidatorTest {

    @Test
    void allowsMissingModifyPathsWithoutCrashingValidation() {
        ImplementationPlanValidator validator = new ImplementationPlanValidator();

        ImplementationPlan plan = new ImplementationPlan(
                "EMP-101",
                "Add department support",
                List.of(new ImplementationStep(
                        1,
                        "springboot-backend/src/main/java/com/Jonas/springbootbackend/service/EmployeeService.java",
                        "MODIFY",
                        "Add department handling",
                        "BACKEND"
                )),
                List.of("Run backend tests"),
                List.of("Potential path mismatch"),
                true
        );

        assertDoesNotThrow(() -> validator.validate(plan));
    }
}
