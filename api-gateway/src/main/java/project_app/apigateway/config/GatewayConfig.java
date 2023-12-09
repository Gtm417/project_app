package project_app.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {

    @Autowired
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("main", r -> r.path("/api/main/**")
                        .filters(f -> f.filter(filter)
                                .stripPrefix(2))
                        .uri("lb://main-app"))

                .route("search", r -> r.path("/api/search/**")
                        .filters(f -> f.filter(filter)
                                .stripPrefix(2)
                                .prefixPath("/elastic"))
                        .uri("lb://elastic-app"))
                .build();
    }

}