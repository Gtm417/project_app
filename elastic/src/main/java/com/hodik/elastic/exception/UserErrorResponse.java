package com.hodik.elastic.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserErrorResponse {
   private String message;
}
