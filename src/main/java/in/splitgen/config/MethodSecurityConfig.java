package in.splitgen.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
// Removed: extends MethodSecurityConfiguration
public class MethodSecurityConfig {

    private final TourPermissionEvaluator tourPermissionEvaluator;
    private final ApplicationContext applicationContext;

    public MethodSecurityConfig(TourPermissionEvaluator tourPermissionEvaluator, ApplicationContext applicationContext) {
        this.tourPermissionEvaluator = tourPermissionEvaluator;
        this.applicationContext = applicationContext;
    }

    /**
     * Defines the custom expression handler and registers the TourPermissionEvaluator.
     * This method is now registered as a Bean instead of relying on class extension.
     */
    @Bean // Changed to a public Bean method
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        // Inject the custom permission evaluator
        expressionHandler.setPermissionEvaluator(tourPermissionEvaluator);
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }
}

