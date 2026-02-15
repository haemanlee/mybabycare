package mybabycare.api.integration.vaccination;

import java.time.LocalDate;

public record VaccinationPeriodResponse(
        String vaccineName,
        LocalDate startDate,
        LocalDate endDate,
        String targetAge
) {
}
