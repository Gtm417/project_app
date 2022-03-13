package com.example.notification.service;

import groovy.lang.GroovyRuntimeException;
import groovy.text.StreamingTemplateEngine;

import java.io.IOException;
import java.util.Map;

public class TemplateConfigurer {
    public static String processTemplate(final String template, final Map<String, Object> binding) {
        try {
            return new StreamingTemplateEngine()
                    .createTemplate(template)
                    .make(binding)
                    .toString();
        } catch (ClassNotFoundException | IOException | GroovyRuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

