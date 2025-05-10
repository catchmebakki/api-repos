package com.ssi.ms.configuration.constant;


import oracle.security.crypto.util.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.function.Function;

import static java.util.Map.entry;

public interface ConfigurationConstants {

    enum ACTIVE {
        Y,
        N,
        ALL
    }

    enum PARMODIFICATIONTYPE {
        CHANGE,
        ENDDATE,
        REINSTATE
    }

    enum ALVMODIFICATIONTYPE {
        DEACTIVATE,
        CHANGE,
        REACTIVATE
    }

    enum WSCCMODIFICATIONTYPE {
        CONFIGURATION,
        STARTDATE
    }

    enum WSWCMODIFICATIONTYPE {
        CONFIGURATION,
        STARTDATE,
        ENDDATE,
        DEACTIVATE,
        REACTIVATE
    }

    MessageFormat APPENDUSERCOMMENTFORMAT = new MessageFormat("{0}<br/>[{1} - {2}]: {3}");
    //{Old Comment}<br/>[{User Name} - {Date Time}]: {User Comment}
    MessageFormat ADDUSERCOMMENTFORMAT = new MessageFormat("[{1} - {2}]: {3}");
    //[{User Name} - {Date Time}]: {User Comment}
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    Function<String[], String> GENERATECOMMENTS = (comments) -> {
        if (comments == null || comments.length == 0) {
            throw new InvalidFormatException("Invalid Comment Arguments");
        }
        String formattedComment;
        if (StringUtils.isNotBlank(comments[0])) {
            formattedComment = APPENDUSERCOMMENTFORMAT.format(comments).trim();
        } else {
            formattedComment = ADDUSERCOMMENTFORMAT.format(comments).trim();
        }
        return formattedComment;
    };

    Long ALV_DISPLAY_ON_STF = 4191L;
    Long ALV_DISPLAY_ON_CMT = 4194L;
    Long ALV_DISPLAY_ON_EMP = 4196L;
    Long ALV_DISPLAY_ON_STF_CMT = 4192L;
    Set<Long> ALV_DISPLAY_ON_STF_CMT_SET = Set.of(ALV_DISPLAY_ON_STF, ALV_DISPLAY_ON_CMT);
    Long ALV_DISPLAY_ON_STF_EMP = 4193L;
    Set<Long> ALV_DISPLAY_ON_STF_EMP_SET = Set.of(ALV_DISPLAY_ON_STF, ALV_DISPLAY_ON_EMP);
    Long ALV_DISPLAY_ON_CMT_EMP = 4195L;
    Set<Long> ALV_DISPLAY_ON_CMT_EMP_SET = Set.of(ALV_DISPLAY_ON_CMT, ALV_DISPLAY_ON_EMP);
    Map<Long, List<Long>> ALV_DISPLAY_ON_LIST = Map.ofEntries(
            entry(ALV_DISPLAY_ON_STF_CMT, ALV_DISPLAY_ON_STF_CMT_SET.stream().toList()),
            entry(ALV_DISPLAY_ON_STF_EMP, ALV_DISPLAY_ON_STF_EMP_SET.stream().toList()),
            entry(ALV_DISPLAY_ON_CMT_EMP, ALV_DISPLAY_ON_CMT_EMP_SET.stream().toList())
    );

    Long ALC_NMI_DISPLAY_CD = 660L;

    String PAR_DATE_FORMAT_VALIDATOR = "^\\d{1,2}/\\d{1,2}/\\d{2,4}$";

    String PAR_OVERLAP_EFF_DT_AUTO_UPDATE = "Auto-updated Parameter Effective Date due to overlap. Old Effective Date was '";

    String DELETE_EXP_DT_AUTO_UPDATE = "Auto-updated Expiration Date due to Delete. Old Expiration Date was '";

    int GENERATED_COMMENT_LENGTH = 4000;

    Long ALV_INACTIVE_SORT_ORDER_NBR = 99L;

    int MAX_PARAM_NAME_LENGTH = 150;

    int MAX_PARAM_TXT_VALUE_LENGTH = 100;

    int MAX_ALV_SPANISH_NAME_LENGTH = 200;
    int MAX_ALV_NAME_LENGTH = 50;
    int MAX_ALV_DESCRIPTION_LENGTH = 1000;
}
