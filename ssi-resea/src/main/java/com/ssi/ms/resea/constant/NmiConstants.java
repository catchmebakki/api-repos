package com.ssi.ms.resea.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

public interface NmiConstants {

    @Getter
    @AllArgsConstructor
    enum RESEA_CREATE_ISSUE_NMI_LIST {
        ABILITY(1L),
        ACTIVELY_SEEKING_WORK(2L),
        ATTENDING_TRAINING(49L),
        AVAILABILITY(3L),
        DEDUCTIBLE_INCOME(4L),
        EARNINGS(7L),
        //EXPECTED_RETURN_TO_WORK(55L),
        PENSION_OR_RETIREMENT(2365L),
        REFERRAL(2373L),
        REFUSAL_OF_JOB_OFFERED(14L);
        private final Long nmiId;
    }

    @Getter
    @AllArgsConstructor
    enum ACTIVELY_SEEKING_WORK {
        FAILED_TO_REGISTER_WITH_JOB_SYSTEM(1292L),
        FAILURE_TO_PROVIDE_JOB_CONTACTS_UPON_REQUEST(1289L),
        HIRED_FOR_FUTURE_WORK(2094L),
        INADEQUATE_WORK_SEARCH(2667L),
        NO_WORK_SEARCH_WITH_CCF(2565L);
        private final Long nmiId;
    }

    Long NMI_FAILURE_TO_ATTEND_1_ON_1_SESSION = 2560L;  //Used in No show functionality

    @Getter
    @AllArgsConstructor
    enum RESEA_CREATE_ISSUE_ADD_ACTIVITY {
        ABILITY(1L),
        ACTIVELY_SEEKING_WORK(2L),
        ATTENDING_TRAINING(49L),
        AVAILABILITY(3L),
        DEDUCTIBLE_INCOME(4L),
        EARNINGS(7L),
        EXPECTED_RETURN_TO_WORK(55L),
        PENSION_OR_RETIREMENT(2365L),
        REFERRAL(2373L),
        REFUSAL_OF_JOB_OFFERED(14L);
        private final Long nmiId;
    }

}
