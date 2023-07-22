package com.hodik.elastic.util;

public enum SearchColumnProject implements SearchColumn {
    NAME("name"), IS_PRIVATE("isPrivate"), CREATED_DATE("createdDate"),
    FINAL_PLANNED_DATE("finalPlannedDate"), START_DATE("startDate"), CATEGORY("category"),
    DESCRIPTION("description"), IS_COMMERCIAL("isCommercial"), STATUS("status");
    final String name;

    SearchColumnProject(String name) {
        this.name = name;
    }


    public static SearchColumn getByNameIgnoringCase(String name) {
        for (SearchColumnProject value : SearchColumnProject.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Column not found " + name);
    }

    @Override
    public String getName() {
        return name;
    }
}
