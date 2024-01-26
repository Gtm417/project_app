//package com.hodik.performance.test.app.service;
//
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.PropertySource;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class PropertyService {
//
//    private final ConfigurableEnvironment environment;
//    private final ConfigurableApplicationContext context;
//
//    public PropertyService(ConfigurableEnvironment environment, ConfigurableApplicationContext context) {
//        this.environment = environment;
//        this.context = context;
//    }
//
//    public void updateProperty(String key, String value) {
//        environment.getPropertySources().addFirst(new PropertySourceCustomizer(key, value));
//        context.refresh();
//    }
//
//    private static class PropertySourceCustomizer implements PropertySource<ConfigurableEnvironment> {
//
//        private final String key;
//        private final String value;
//
//        public PropertySourceCustomizer(String key, String value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        @Override
//        public void customize(ConfigurableEnvironment environment) {
//            environment.getPropertySources().forEach(propertySource -> {
//                if (propertySource.containsProperty(key)) {
//                    ((Map<String, Object>) propertySource.getSource()).put(key, value);
//                }
//            });
//        }
//    }
//}
