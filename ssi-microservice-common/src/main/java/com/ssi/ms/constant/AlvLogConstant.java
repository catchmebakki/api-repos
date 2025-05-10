package com.ssi.ms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Praveenraja Paramsivam
 * This class contains an enumeration of error codes along with their associated numeric codes.
 */
public class AlvLogConstant {
 	/**
	 * ApplicationName representing application names with associated AlvCode constants.
	 */
    @Getter
    @AllArgsConstructor
    public enum ApplicationName implements AlvCodeConstantParent {
        MICRO_SERVICE(4903);
        private Integer code;
    }

    /**
     * ErrorStatus representing application names with associated AlvCode constants.
     */
    @Getter
    @AllArgsConstructor
    public enum ErrorStatus implements AlvCodeConstantParent {
        LOGGING_ERROR(7041);
        private Integer code;
    }
    /**
     * LogType representing application names with associated AlvCode constants.
     */
    @Getter
    @AllArgsConstructor
    public enum LogType implements AlvCodeConstantParent {
        ERROR(1991);
        private Integer code;
    }
    /**
     * ProgramName representing application names with associated AlvCode constants.
     */
    @Getter
    @AllArgsConstructor
    public enum ProgramName implements AlvCodeConstantParent {
        ONLINE(1990);
        private Integer code;
    }
    /**
     * ProgramName representing application names with associated AlvCode constants.
     */
    @Getter
    @AllArgsConstructor
    public enum SRLAccessCd implements AlvCodeConstantParent {
        VIEW_ONLY(1412),
    	UPDATE(1413),
    	NONE(1414);
        private Integer code;
    }
}
