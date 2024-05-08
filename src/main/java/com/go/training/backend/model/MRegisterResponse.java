package com.go.training.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MRegisterResponse {
    private String email;

    private String name;
}
