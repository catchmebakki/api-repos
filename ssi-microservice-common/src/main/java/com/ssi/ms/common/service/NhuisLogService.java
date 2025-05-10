package com.ssi.ms.common.service;

import com.ssi.ms.common.database.dao.NhuisLogNhlDao;
import com.ssi.ms.common.database.repository.NhuisLogNhlRepository;
import com.ssi.ms.constant.AlvLogConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Praveenraja Paramsivam
 * NhuisLogService provides service to NHL log information.
 */
@Service
@Slf4j
public class NhuisLogService {
	private static final short MODULE_NAME_LENGTH = 50;
	private static final short METHOD_NAME_LENGTH = 100;
	private static final short ERROR_LENGTH = 4000;
	@Autowired
	private NhuisLogNhlRepository logRepo;
	@Value("${spring.application.name: not_configured}")
	private String applicationName;

	/**
	 * Save an error related to the provided exception to the National Hockey League (NHL) system.
	 *
	 * @param exception {@link Throwable} The Throwable exception to be logged and saved.
	 * @param userId {@link Long} The ID of the user associated with the error.
	 * @param claimId {@link Long} The ID of the claim associated with the error.
	 */
	public void saveErrorToNhl(final Throwable exception, Long userId, Long claimId) {
		final Long nhlId = null;
		final String space = " ";
		final String empty = "";
		final String stackTrace = "StackTrace:";
		final NhuisLogNhlDao nhuisLogNhl = new NhuisLogNhlDao();
		StackTraceElement stackTraceElement = null;
		String expMessageDetail = null;
		String errStackDetail = null;
		String expMessage = null;
		String errStack = null;
		// setErrorLogInfo(exception, errorLogDTO);
		stackTraceElement = getModuleInfo(exception);
		expMessageDetail = exception.getMessage();
		if (StringUtils.isNotBlank(expMessageDetail)) {
			final int expLength = expMessageDetail.length();
			if (expLength > ERROR_LENGTH) {
				expMessage = expMessageDetail.substring(0, ERROR_LENGTH);
			} else {
				expMessage = expMessageDetail;
			}
		} else {
			expMessage = "";
		}

		errStackDetail = getStackTrace(exception);
		if (errStackDetail != null) {
			final int errLength = errStackDetail.length();
			if (errLength > ERROR_LENGTH) {
				errStack = errStackDetail.substring(0, ERROR_LENGTH);
			} else {
				errStack = errStackDetail;
			}
		}

		nhuisLogNhl.setFkClmId(claimId);
		nhuisLogNhl.setFkUsrId(userId);
		nhuisLogNhl.setNhlApplnName(AlvLogConstant.ApplicationName.MICRO_SERVICE.getCode());
		nhuisLogNhl.setNhlErrDesc(errStack);
		nhuisLogNhl.setNhlErrStatusCd(AlvLogConstant.ErrorStatus.LOGGING_ERROR.getCode());
		nhuisLogNhl.setNhlErrTxt(expMessage);
		nhuisLogNhl.setNhlAddtnlErrData(StringUtils.defaultIfBlank(expMessageDetail, empty) + space + stackTrace
				+ StringUtils.defaultIfBlank(errStackDetail, empty));
		nhuisLogNhl.setNhlLogType(AlvLogConstant.LogType.ERROR.getCode());
		nhuisLogNhl.setNhlMethodName(StringUtils.defaultIfBlank(getMethodName(stackTraceElement), space));
		nhuisLogNhl.setNhlModuleName(StringUtils.defaultIfBlank(getModuleName(stackTraceElement), space));
		nhuisLogNhl.setNhlProgName(AlvLogConstant.ProgramName.ONLINE.getCode() + "");
		nhuisLogNhl.setNhlStdErrorCd(AlvLogConstant.ErrorStatus.LOGGING_ERROR.getCode());
		nhuisLogNhl.setNhlLastUpdBy(userId + "");
		nhuisLogNhl.setNhlLastUpdUsing(applicationName);
		nhuisLogNhl.setNhlApplnVersion("1.0.1");
		nhuisLogNhl.setNhlCreatedBy(userId + "");
		nhuisLogNhl.setNhlCreatedUsing(applicationName);
		logRepo.save(nhuisLogNhl);
		log.info("Error logged in to NHL table");
	}

	/**
	 * Retrieve module information from the provided Throwable exception's stack trace.
	 *
	 * @param exception {@link Throwable} The Throwable exception from which to extract module information.
	 * @return {@link StackTraceElement} The StackTraceElement representing the module information in the stack trace.
	 */
	private StackTraceElement getModuleInfo(final Throwable exception) {
		final StackTraceElement[] stackElements = exception.getStackTrace();
		StackTraceElement stackTraceElement = null;
		StackTraceElement localStackTraceElement = null;
		for (int lcv = 0; lcv < stackElements.length; lcv++) {
			final String className = stackElements[lcv].getClassName();
			if (className.contains("com.ssi")) {
				stackTraceElement = stackElements[lcv];
				break;
			}
			if (lcv == 0) {
				localStackTraceElement = stackElements[lcv];
			}
		}
		if (stackTraceElement == null) {
			stackTraceElement = localStackTraceElement;
		}
		return stackTraceElement;
	}

	/**
	 * Retrieve the module name from the provided StackTraceElement.
	 *
	 * @param stackTraceElement {@link StackTraceElement} The StackTraceElement containing module information.
	 * @return {@link String} The module name extracted from the provided stack trace element.
	 */
	private String getModuleName(final StackTraceElement stackTraceElement) {
		String moduleName = "";
		Integer className;
		if (stackTraceElement != null && stackTraceElement.getClassName() != null) {
			moduleName = stackTraceElement.getClassName();
			className = moduleName.lastIndexOf('.') + 1;
			if (className > 0) {
				moduleName = moduleName.substring(className);
			}
			if (moduleName.length() > MODULE_NAME_LENGTH) {
				moduleName = moduleName.substring(0, MODULE_NAME_LENGTH);
			}
		}
		return moduleName;
	}

	/**
	 * Retrieve the method name from the provided StackTraceElement.
	 *
	 * @param stackTraceElement {@link StackTraceElement} The StackTraceElement containing method information.
	 * @return {@link String} The method name extracted from the provided stack trace element.
	 */
	private String getMethodName(final StackTraceElement stackTraceElement) {
		String methodName = "";
		if (stackTraceElement != null && stackTraceElement.getMethodName() != null) {
			if (stackTraceElement.getMethodName().length() > METHOD_NAME_LENGTH) {
				methodName = stackTraceElement.getMethodName().substring(0, METHOD_NAME_LENGTH);
			} else {
				methodName = stackTraceElement.getMethodName();
			}
		}
		return methodName;
	}

	/**
	 * Retrieve the stack trace as a formatted string from the provided Throwable exception.
	 *
	 * @param exception {@link Throwable} The Throwable exception from which to retrieve the stack trace.
	 * @return {@link String} The formatted stack trace as a string.
	 */
	private String getStackTrace(final Throwable exception) {
		String stacktrace = "";
		if (exception != null) {
			final StringWriter strwriter = new StringWriter();
			exception.printStackTrace(new PrintWriter(strwriter));
			stacktrace = strwriter.toString();
		}
		return stacktrace;
	}

}
