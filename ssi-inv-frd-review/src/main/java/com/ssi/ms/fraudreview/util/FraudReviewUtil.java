package com.ssi.ms.fraudreview.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FraudReviewUtil {

	public static String clobToString(Clob data) {
		final StringBuilder strBuilder = new StringBuilder();
		try {
			final Reader reader = data.getCharacterStream();
			final BufferedReader bufferedReader = new BufferedReader(reader);

			String line;
			while (null != (line = bufferedReader.readLine())) {
				strBuilder.append(line);
			}
			bufferedReader.close();

		} catch (SQLException e) {
			log.error("SQLException while converting search output clob data to string :" + e.getMessage(), e);
			throw new RuntimeException("Error while converting clob data to string.", e);

		} catch (IOException e) {
			log.error("IOException while converting search output clob data to string :" + e.getMessage(), e);
			throw new RuntimeException("Error while converting clob data to string.", e);
		}
		return strBuilder.toString();
	}

}
