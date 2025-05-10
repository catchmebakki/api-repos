package com.ssi.ms.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previous;

/**
 * @author munirathnam.surepalli
 * Interface for Date utility functions.
 * Suppresses PMD warnings related to field naming conventions.
 *
 */
@SuppressWarnings("PMD.FieldNamingConventions")
public interface DateUtil {

	/**
	 * Convert a Date to a LocalDate using the provided function.
	 *
	 * @param date {@link Date} The Date to be converted.
	 * @return {@link LocalDate} The LocalDate representation of the provided Date using the function.
	 */
    Function<Date, LocalDate> dateToLocalDate = date -> null == date ? null
            : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

	Function<Date, LocalDateTime> dateToLocalDateTime = date -> null == date ? null
			: Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    Function<LocalDate, Date> localDateToDate = localDate -> null == localDate ? null
            : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	Function<LocalDateTime, Date> localDateTimeToDate = localDate -> null == localDate ? null
			: Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());

	BiPredicate<Date, Date> checkFutureDate = (date, systemDate) ->   date != null && date.after(systemDate);

	BiPredicate<Date, Date> checkPastDate = (date, systemDate) -> date != null && date.before(systemDate);

	Function<LocalDate, String> localDateToString = localDate -> null == localDate ? null
			: localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

	Function<Date, String> dateToString = date -> null == date ? null
			: new SimpleDateFormat("MM/dd/yyyy").format(date);

	Function<String, Date> stringToDate = date -> {
		try {
			return null == date ? null
					: new SimpleDateFormat("MM/dd/yyyy").parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	};

	Function<Date, Date> getPrevSundayDate = weekDate -> null == weekDate ? null
			: localDateToDate.apply(dateToLocalDate.apply(weekDate).with(previous(SUNDAY)));

}
