package mybabycare.api.child.dto;

import javax.validation.constraints.NotBlank;

public record ChildRequest(
        @NotBlank String name,
        @NotBlank String birthday
) {
}
