package com.mwnz.evaluation.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String error_description;
}