package com.hodik.elastic.dto;

import com.hodik.elastic.util.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor

public class SearchFilter {


    private final String column;

    private final Operations operations;

    private final List<?> values;

}

