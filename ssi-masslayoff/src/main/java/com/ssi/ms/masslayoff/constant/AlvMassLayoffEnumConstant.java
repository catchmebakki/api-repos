package com.ssi.ms.masslayoff.constant;

import com.ssi.ms.constant.AlvCodeConstantParent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Praveenraja Paramsivam
 * This class contains an enumeration of mass layoff status codes along with their associated numeric codes.
 */
public class AlvMassLayoffEnumConstant {

	 /**
     * Enumeration representing the status codes for mass layoffs.
     */
    @Getter
    @AllArgsConstructor
    public enum MassLayoffStatusCd implements AlvCodeConstantParent {
        FINAL(4889),
        PENDING(4888);
        private Integer code;
    }

    /**
     * Enumeration representing the status codes for MSL (Mass Layoff) claimants.
     */
    @Getter
    @AllArgsConstructor
    public enum MslClaimantStatusCd implements AlvCodeConstantParent {
        CONFIRMED(4891),
        PENDING(4890);
        private Integer code;
    }

    /**
     * Enumeration representing the source codes for MSL (Mass Layoff) claimants.
     */
    @Getter
    @AllArgsConstructor
    public enum MslClaimantSourceCd implements AlvCodeConstantParent {
        CLONED(4882),
        CLAIMANT_WORK_HISTORY(4893),
        STAFF_ENTERED(4894),
        UPLOADED(4895);
        private Integer code;
    }

    /**
     * Enumeration representing the summary status codes for MSL (Mass Layoff ) upload processing.
     */
    @Getter
    @AllArgsConstructor
    public enum MslSummaryStatusCd implements AlvCodeConstantParent {
    	TO_BE_INITIATED(4880),
    	FILE_UPLOADED(4881),
    	FILE_UPLOAD_FAILED(4882),
    	FILE_PARSING_STARTED(4883),
    	FILE_PARSING_FAILED(4884),
    	FILE_PARSING_COMPLETED(4885),
    	TRANSFER_FROM_STAGING_INITIATED(4886),
    	UPLOAD_PROCESS_COMPLETED(4887);
        private Integer code;
    }
}
