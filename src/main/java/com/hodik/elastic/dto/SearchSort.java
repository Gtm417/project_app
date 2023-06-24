package com.hodik.elastic.dto;

import com.hodik.elastic.util.SearchColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSort {
    private SearchColumn column;
    private Boolean ascending;
}
