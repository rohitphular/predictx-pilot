package org.pilot.predictx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Builder
@Value
@Getter
public class ApiResponse<T> {

    T payload;

}