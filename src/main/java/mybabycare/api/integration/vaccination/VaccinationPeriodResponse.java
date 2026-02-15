package mybabycare.api.integration.vaccination;

public record VaccinationPeriodResponse(
        String vaccineName,
        String inoculationOrder,
        String recommendedPeriod,
        String targetAge
) {
}
