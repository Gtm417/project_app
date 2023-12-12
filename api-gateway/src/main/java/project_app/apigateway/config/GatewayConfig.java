package project_app.apigateway.config;

import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsContributor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;

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

    @Bean
    WebFluxTagsContributor uriTagContributorForConnectionProblems()
    {
        return (exchange, ex) -> willExchangeUriBeingParsed(exchange)
                ? Tags.empty()
                : Tags.of("uri", exchange.getRequest().getPath().value());
    }

    private static boolean willExchangeUriBeingParsed(final ServerWebExchange exchange)
    {
        return exchange.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) != null;
    }

}