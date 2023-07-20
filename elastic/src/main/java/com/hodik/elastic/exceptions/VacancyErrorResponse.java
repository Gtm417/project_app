package com.hodik.elastic.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacancyErrorResponse {
    private String message;
}