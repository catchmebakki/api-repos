package com.ssi.ms.common.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;

/**
 * @author munirathnam.surepall
 * ParameterParService provides service to get ParameterPar information.
 */
@Service
public class ParameterParService {
	@Autowired
	private ParameterParRepository parameterParRepository;

	/**
	 * Retrieve a Long parameter value based on the provided parameter short name.
	 *
	 * @param {@link String} parShortName The short name of the parameter to retrieve.
	 * @return {@link Long} The Long parameter value associated with the provided short name.
	 */
	public Long getLongParamValue(String parShortName) {
		final ParameterParDao parameterParDao = parameterParRepository.findByParShortName(parShortName);
		return parameterParDao != null ? parameterParDao.getParNumericValue() : null;
	}

	/**
	 * Retrieve the current database date.
	 *
	 * @return {@link Date} The current database date as a Date instance.
	 */
	public Date getDBDate() {
		return new Date(parameterParRepository.getCurrentTimestamp().getTime());
	}

	/**
	 * Retrieve the current database timestamp.
	 *
	 * @return {@link Timestamp} The current database timestamp as a Timestamp instance.
	 */
	public Timestamp getDBTimeStamp() {
		return parameterParRepository.getCurrentTimestamp();
	}
}
