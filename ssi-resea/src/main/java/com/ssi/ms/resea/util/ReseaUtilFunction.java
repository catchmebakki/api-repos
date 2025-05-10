package com.ssi.ms.resea.util;

import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.dto.DashboardCalResDTO;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.ssi.ms.resea.constant.ReseaConstants.DATE_TIME_FORMAT_24H;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_FIRST_SUBS_APPT;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_DO_NOT_SCHEDULE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_CAL_EVENT_TYPE_IN_USE_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_FIRST_SUBSEQUENT_APPT_ALV;

@SuppressWarnings("PMD.FieldNamingConventions")
public interface ReseaUtilFunction {
    BiPredicate<Date, Date> IS_CONFIG_ACTIVE = (expirationDate, systemDate) -> expirationDate == null || !systemDate.after(expirationDate);

    BiPredicate<Date, Date> IS_CONFIG_NOT_REINSTATABLE = (expirationDate, systemDate) -> expirationDate == null || systemDate.before(expirationDate);

    BiFunction<Object, Object, Object> coalesce = (obj1, obj2) -> obj1 != null ? obj1 : obj2;

    static void updateErrorMap(HashMap<String, List<String>> errorMap, List<ReseaErrorEnum> errorEnums) {
        for (final var errorEnum : errorEnums) {
            errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<>());
            errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
                    .add(errorEnum.getFrontendErrorCode());
        }
    }

    static void updateErrorMap(Map<String, List<DynamicErrorDTO>> errorMap,
                               List<ReseaErrorEnum> errorEnums, List<String> errorParams) {
        int errorParamStart = 0;
        for (final var errorEnum : errorEnums) {
            errorMap.putIfAbsent(errorEnum.getFrontendField(), new ArrayList<DynamicErrorDTO>());
            errorMap.getOrDefault(errorEnum.getFrontendField(), new ArrayList<>())
                    .add(new DynamicErrorDTO(errorEnum.getFrontendErrorCode(),
                            errorEnum.getParams() == 0 ? null : errorParams
                                    .subList(errorParamStart, Math.min(errorParamStart+errorEnum.getParams(), errorParams.size()))));
            errorParamStart += errorEnum.getParams();
        }
    }

    static Function<String, String> convert24hTo12h() {
        return time -> {
            if (StringUtils.isNotBlank(time)) {
                time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
                        .format(DateTimeFormatter.ofPattern("hh:mm a"));
            }
            return time;
        };
    }

    static Function<String, String> convert12hTo24h() {
        return time -> {
            if (StringUtils.isNotBlank(time)) {
                time = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"))
                        .format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            return time;
        };
    }

    static Function<DashboardCalResDTO, String> getReseaEventLabel() {
        return dto -> {
            String label;
            if (StringUtils.isNotBlank(dto.getFirstName())) {
                label = dto.getFirstName() + " " + dto.getLastName();
            } else if ((dto.getEventType() != null && RSIC_CAL_EVENT_TYPE_DO_NOT_SCHEDULE_ALV == dto.getEventType()) ||
                    (dto.getEventType() != null && RSIC_CAL_EVENT_TYPE_IN_USE_ALV == dto.getEventType())) {
                label = dto.getUsageDesc();
            } else {
                label = dto.getEventTypeDesc();
            }
            return label;
        };
    }

    static BiFunction<String, String, Date> string24HToDate() {
        return (stringDate, stringTime) -> {
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_24H);
            try {
                date = sdf.parse(stringDate + " " + stringTime);
            } catch (ParseException ignored) { }
            return date;
        };
    }

    static Long getRscaStageFromRsicTimeslotUsage(Long rsicTimeslotUsageCd) {
        Long rscaStageCd = null;
        if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.INITIAL_APT.getCode();
        } else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.FIRST_SUBS_APT.getCode();
        } else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.SECOND_SUBS_APT.getCode();
        }
        return rscaStageCd;
    }
    static Long getRscaStatusFromRsicMtgStatus(Long rsicMtgStatusCd) {
        if (rsicMtgStatusCd == null) return null;
        Long rscaStatusCd = null;
        if (ReseaAlvEnumConstant.RsicMeetingStatusCd.SCHEDULED.getCode().equals(rsicMtgStatusCd)) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.SCHEDULED.getCode();
        }
        else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED.getCode().equals(rsicMtgStatusCd)) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode();
        }
        else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED.getCode().equals(rsicMtgStatusCd)) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.FAILED.getCode();
        }
        else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.FAILED_RTW.getCode().equals(rsicMtgStatusCd)) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.FAILED_RTW.getCode();
        }
        else if (ReseaAlvEnumConstant.RsicMeetingStatusCd.COMPLETED_RTW.getCode().equals(rsicMtgStatusCd)) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.COMPLETED_RTW.getCode();
        }
        return rscaStatusCd;
    }

    static Long getRscnNoteCategoryFromRsicTimeslotUsage(Long rsicTimeslotUsageCd, Long rscsStageCd) {
        Long rscnNoteCategoryCd = null;
        if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.INITIAL_APT.getCode();
        } else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.FIRST_SUBS_APT.getCode();
        } else if (ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode().equals(rsicTimeslotUsageCd)) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.SECOND_SUBS_APT.getCode();
        } else if (ReseaAlvEnumConstant.RscsStageCd.TERMINATED.getCode().equals(rscsStageCd)) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.TERMINATED.getCode();
        } else if (ReseaAlvEnumConstant.RscsStageCd.PRE_RESEA.getCode().equals(rscsStageCd)) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.PRE_RESEA.getCode();
        }
        return rscnNoteCategoryCd;
    }

    static Long getRscaStageFromRscsStage(Long rscsStageCd) {
        Long rscaStageCd = null;
        if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.PRE_RESEA.getCode())) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.PRE_RESEA.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.INITIAL_APT.getCode())) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.INITIAL_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.FIRST_SUBS_APT.getCode())) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.FIRST_SUBS_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.SECOND_SUBS_APT.getCode())) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.SECOND_SUBS_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.TERMINATED.getCode())) {
            rscaStageCd = ReseaAlvEnumConstant.RscaStageCd.TERMINATED.getCode();
        }
        return rscaStageCd;
    }

    static Long getRscaStatusFromRscsStatus(Long rscsStatusCd) {
        Long rscaStatusCd = null;
        if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.SCHEDULED.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.SCHEDULED.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.WAITLIST_REQ.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.WAITLIST_REQ.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.PENDING_FURTHER_SCH_BY_STAFF.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.PENDING_FURTHER_SCH_BY_STAFF.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.COMPLETED.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.COMPLETED.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.FAILED.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.FAILED.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.FAILED_RTW.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.FAILED_RTW.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.ON_PLACEHOLDER_SCH_WS_WAIVER.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.ON_PLACEHOLDER_SCH_WS_WAIVER.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.REPORTED_TO_TRAINING.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.REPORTED_TO_TRAINING.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.COMPLETED_RTW.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.COMPLETED_RTW.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.NO_CC_FILED_RECENTLY.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.NO_CC_FILED_RECENTLY.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.SECOND_SUB_APPT_COMPLETED.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.SECOND_SUB_APPT_COMPLETED.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.BYE.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.BYE.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.RETURNED_TO_WORK.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.RETURNED_TO_WORK.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.INCORRECTLY_SELECTED_FOR_RESEA_PARTICIPATION.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.INCORRECTLY_SELECTED_FOR_RESEA_PARTICIPATION.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.OUT_OF_STATE_CLAIM.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.OUT_OF_STATE_CLAIM.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.BENEFIT_EXHAUSTED.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.BENEFIT_EXHAUSTED.getCode();
        } else if (rscsStatusCd.equals(ReseaAlvEnumConstant.RscsStatusCd.WITHDRAWN_CLM_BY_CMT.getCode())) {
            rscaStatusCd = ReseaAlvEnumConstant.RscaStatusCd.WITHDRAWN_CLM_BY_CMT.getCode();
        }
        return rscaStatusCd;
    }

    static Long getRscnNoteCategoryFromRscsStage(Long rscsStageCd) {
        Long rscnNoteCategoryCd = null;
        if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.PRE_RESEA.getCode())) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.PRE_RESEA.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.INITIAL_APT.getCode())) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.INITIAL_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.FIRST_SUBS_APT.getCode())) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.FIRST_SUBS_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.SECOND_SUBS_APT.getCode())) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.SECOND_SUBS_APT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.TERMINATED.getCode())) {
            rscnNoteCategoryCd = ReseaAlvEnumConstant.RscnNoteCategoryCd.TERMINATED.getCode();
        }
        return rscnNoteCategoryCd;
    }

    static BiFunction<String, String, Long> durationBetweenStartAndEndTime() {
        return (startTime, endTime) -> {
            long minutes = 0;
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                LocalTime startTs = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime endTs = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
                minutes = Duration.between(startTs, endTs).toMinutes();
            }
            return minutes;
        };
    }

    static Function<String, Short> convertDayOfWeek() {
        return dayOfWeek -> switch (dayOfWeek.toUpperCase()) {
            case "MONDAY" -> 2;
            case "TUESDAY" -> 3;
            case "WEDNESDAY" -> 4;
            case "THURSDAY" -> 5;
            case "FRIDAY" -> 6;
            default -> 0;
        };
    }

    static Function<Short, String> convertDayOfWeekToString() {
        return dayOfWeek -> switch (dayOfWeek) {
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            default -> null;
        };
    }

    static Long rscsStageToRsicUsage(Long rscsStageCd) {
        Long rsicUsageCd = null;
        if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.INITIAL_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.FIRST_SUBS_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode();
        } else if (rscsStageCd.equals(ReseaAlvEnumConstant.RscsStageCd.SECOND_SUBS_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode();
        }
        return rsicUsageCd;
    }

    static Long rscaStageToRsicUsage(Long rscaStageCd) {
        Long rsicUsageCd = null;
        if (rscaStageCd.equals(ReseaAlvEnumConstant.RscaStageCd.INITIAL_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.INITIAL_APPOINTMENT.getCode();
        } else if (rscaStageCd.equals(ReseaAlvEnumConstant.RscaStageCd.FIRST_SUBS_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.FIRST_SUBS_APPOINTMENT.getCode();
        } else if (rscaStageCd.equals(ReseaAlvEnumConstant.RscaStageCd.SECOND_SUBS_APT.getCode())) {
            rsicUsageCd = ReseaAlvEnumConstant.RsicTimeslotUsageCd.SECOND_SUBS_APPOINTMENT.getCode();
        }
        return rsicUsageCd;
    }

    BiFunction<String, Integer, String> substring = (inputString, length) ->
            StringUtils.isNotBlank(inputString) && inputString.length() > length ? inputString.substring(0, length) : inputString;
}
