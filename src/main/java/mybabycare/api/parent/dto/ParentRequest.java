package mybabycare.api.parent.dto;

import javax.validation.constraints.NotBlank;

public record ParentRequest(
        @NotBlank String name,
        @NotBlank String phoneNumber
) {
}
