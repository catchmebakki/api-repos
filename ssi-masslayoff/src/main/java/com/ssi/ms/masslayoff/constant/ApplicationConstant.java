package com.ssi.ms.masslayoff.constant;

/**
************************************************************************************************
*                     Modification Log                                                        *
************************************************************************************************
*
*  	Date            Developer           Defect  Description of Change
*  	----------      ---------           ------  ---------------------
*	12/22/2023		Sitaram				SV225644 - UD-231222-Mass Layoff status is not updating in NHUIS to incomplete
**
************************************************************************************************
*/

/**
 * @author Praveenraja Paramsivam
 */
public interface ApplicationConstant {

    String DATEFORMATE = "MM/dd/yyyy";
    String SSN_PATTERN = "^(\\d{3}-\\d{2}-\\d{4})$";
    String MUSMID = "musmId";
    int HEADER_ROW_COUNT = 10;
    String UPLOADED_FILENAME = "Uploaded file name";
    int ERROR_MSG_LENGTH_250 = 250;
    int ERROR_MSG_INDEX_249 = 249;
    String YES = "Y";

    long ALV_MASS_LAYOFF = 386;

    long MSL_TYPE_LAYOFF = 1244;

    long MSL_TYPE_OPEN = 281;

    long MSL_TYPE_MSL = 1081;

    long MSL_TYPE_MULTICLAIMANT = 1306;

    String FAR_FUTURE_DATE_YYYY_MM_DD = "2099-12-31";

    String INCOMPLETE = "I";	//SV225644

    String BLANK_SPACE = " ";	//SV225644
}
