package mybabycare.api.integration.vaccination.controller;

import mybabycare.api.integration.vaccination.VaccinationApiClient;
import mybabycare.api.integration.vaccination.VaccinationPeriodResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/vaccinations")
public class VaccinationController {

    private final VaccinationApiClient vaccinationApiClient;

    public VaccinationController(VaccinationApiClient vaccinationApiClient) {
        this.vaccinationApiClient = vaccinationApiClient;
    }

    @GetMapping("/periods")
    public List<VaccinationPeriodResponse> getVaccinationPeriods(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday
    ) {
        return vaccinationApiClient.getVaccinationPeriods(birthday);
    }
}
