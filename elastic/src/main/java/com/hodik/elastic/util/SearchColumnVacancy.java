package com.hodik.elastic.util;

public enum SearchColumnVacancy implements SearchColumn {
    PROJECT_ID("projectId"), DESCRIPTION("description"), CREATOR("creator"), ABOUT_PROJECT("aboutProject"),
    EXPECTED("expected"), JOB_POSITION("jobPosition");

    final String name;

    SearchColumnVacancy(String name) {
        this.name = name;
    }


    public static SearchColumn getByNameIgnoringCase(String name) {
        for (SearchColumnVacancy value : SearchColumnVacancy.values()) {
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



