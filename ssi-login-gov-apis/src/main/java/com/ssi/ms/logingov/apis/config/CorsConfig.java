package com.ssi.ms.logingov.apis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig provides service to call API calls.
 * @Author: munirathnam.surepall
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * Override this method to configure cross-origin resource sharing (CORS)
	 * mappings.
	 * 
	 * @param registry {@link CorsRegistry} The CorsRegistry to be configured with
	 *                 CORS mappings.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
				.allowedHeaders("*");
	}
}
