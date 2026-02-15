package mybabycare.api.integration.vaccination.controller;

import mybabycare.api.integration.vaccination.VaccinationApiClient;
import mybabycare.api.integration.vaccination.VaccinationPeriodResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VaccinationController.class)
class VaccinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VaccinationApiClient vaccinationApiClient;

    @Test
    void getVaccinationPeriods() throws Exception {
        given(vaccinationApiClient.getVaccinationPeriods(eq(java.time.LocalDate.of(2024, 1, 15))))
                .willReturn(List.of(new VaccinationPeriodResponse("B형간염", "1차", "출생 시", "생후 0개월")));

        mockMvc.perform(get("/v1/vaccinations/periods")
                        .param("birthday", "2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vaccineName").value("B형간염"))
                .andExpect(jsonPath("$[0].inoculationOrder").value("1차"))
                .andExpect(jsonPath("$[0].recommendedPeriod").value("출생 시"));
    }
}
