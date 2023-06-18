package com.hodik.elastic.util;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public enum SearchColumn {
    name, isPrivate, createdDate, finalPlannedDate, startDate, category, description, isCommercial, status
}
