package org.pilot.predictx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContentMatcherRequest {

    @NotNull(message = "Input-1 is mandatory")
    @NotEmpty(message = "Input-1 should not be blank")
    private String input1;

    @NotNull(message = "Input-2 is mandatory")
    @NotEmpty(message = "Input-2 should not be blank")
    private String input2;

}