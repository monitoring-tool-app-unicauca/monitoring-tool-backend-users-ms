package co.edu.unicauca.monitoring.tool.backend.users.ms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;


@Configuration
public class OpenApiConfig {

    public static final String API_TITLE = "Users Microservice Rest Entry Points";
    public static final String API_DESCRIPTION = "Users Microservice Rest Entry Points.";
    public static final String API_VERSION = "1.0";

    public static final String CONTACT_NAME = "Development Team";
    public static final String CONTACT_EMAIL = "juliansmartinez@unicauca.edu.co";
    public static final String CONTACT_URL = "https://unicauca.edu.co";

    public static final String LICENSE_NAME = "MIT License";
    public static final String LICENSE_URL = "https://opensource.org/licenses/MIT";

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";
    public static final String SECURITY_SCHEME_TYPE = "HTTP";
    public static final String SECURITY_SCHEME_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_FORMAT = "JWT";


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .email(CONTACT_EMAIL)
                                .url(CONTACT_URL))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.valueOf(SECURITY_SCHEME_TYPE))
                                        .scheme(SECURITY_SCHEME_SCHEME)
                                        .bearerFormat(SECURITY_SCHEME_FORMAT)));
    }
}

