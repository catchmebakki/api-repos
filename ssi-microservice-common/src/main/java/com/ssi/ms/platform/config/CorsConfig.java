package com.ssi.ms.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author munirathnam.surepall
 * CorsConfig provides service to call API call's.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * Override this method to configure cross-origin resource sharing (CORS) mappings.
	 * @param registry {@link CorsRegistry} The CorsRegistry to be configured with CORS mappings.
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*");
    }
}