package com.hodik.elastic.util;

public enum SearchColumnUser implements SearchColumn{
    EMAIL("email"), CV ("cv"), DESCRIPTION ("description"), FIRST_NAME("firstName"), LAST_NAME ("lastName"),
   PASSWORD ("password"), ROLE ("role"), STATUS ("status"), TYPE ("type"), SKILLS ("skills"),
    EXPERTISE ("skills.expertise"), SKILL_NAME ("skills.skillName");
    String name;

    SearchColumnUser(String name) {
        this.name = name;
    }


    public static SearchColumnUser getByNameIgnoringCase(String name) {
        for (SearchColumnUser value : SearchColumnUser.values()) {
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


