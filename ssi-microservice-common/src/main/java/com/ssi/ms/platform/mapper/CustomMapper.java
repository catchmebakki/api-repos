package com.ssi.ms.platform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Praveenraja Paramsivam
 * CustomMapper provides services to local time and local date.
 */
@Component
@Mapper(componentModel = "spring")
public interface CustomMapper {
	/**
	 * Convert a Timestamp to a LocalTime using the named converter "timestampToLocalTime".
	 *
	 * @param timestamp {@link Timestamp} The Timestamp to be converted.
	 * @return {@link LocalTime} The LocalTime representation of the provided Timestamp.
	 */
    @Named("timestampToLocalTime")
    static LocalTime timestampToLocalTime(Timestamp timestamp) {
        final Instant instant = timestamp.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Convert a Date to a LocalDate.
     *
     * @param date {@link Date} The Date to be converted.
     * @return {@link LocalDate} The LocalDate representation of the provided Date.
     */
    static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    /**
     * Convert a java.sql.Date to a LocalDate using the named converter "sqlDateToLocalDate".
     *
     * @param date {@link Date} The java.sql.Date to be converted.
     * @return {@link LocalDate} The LocalDate representation of the provided java.sql.Date.
     */
    @Named("sqlDateToLocalDate")
    static LocalDate toLocalDate(java.sql.Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
