package com.hodik.elastic.dto;

import com.hodik.elastic.util.Operations;
import com.hodik.elastic.util.SearchColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@AllArgsConstructor

public class SearchFilter {


    private final SearchColumn column;


    private final Operations operations;

    private final List<?> values;

}

