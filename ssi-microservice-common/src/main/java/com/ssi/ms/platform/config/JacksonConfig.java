package com.ssi.ms.platform.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Praveenraja Paramsivam
 * JacksonConfig provides services to Jackson config local date deserialization and date deserialization.
 */
@Configuration
public class JacksonConfig {
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    /**
     * This method, define a custom Jackson2ObjectMapperBuilderCustomizer to configure local date deserialization.
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomLocalDateDeserialization() {
        return builder -> builder.deserializerByType(LocalDate.class, new JsonDeserializer() {

            @Override
            public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
                    throws IOException {
               final String dateString = jsonParser.getText();
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }
        });
    }

    /**
     * Configure custom date deserialization settings for Jackson ObjectMapperBuilder.
     *
     * @return {@link Jackson2ObjectMapperBuilderCustomizer} The Jackson2ObjectMapperBuilderCustomizer with custom date deserialization settings.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomDateDeserialization() {
        return builder -> builder.deserializerByType(Date.class, new JsonDeserializer() {
            @SneakyThrows
            @Override
            public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
                    throws IOException {
                final String dateString = jsonParser.getText();
                final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                return formatter.parse(dateString);
            }
        });
    }
}