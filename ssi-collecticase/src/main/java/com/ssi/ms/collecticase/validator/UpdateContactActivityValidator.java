package com.ssi.ms.collecticase.validator;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.constant.ErrorMessageConstant;
import com.ssi.ms.collecticase.dto.UpdateContactActivityDTO;
import com.ssi.ms.collecticase.util.CollecticaseErrorEnum;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.platform.dto.DynamicErrorDTO;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_ADD_UPD_ATTY_CONTACT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_ADD_UPD_OTHER_REP_CONTACT;

@Component
@AllArgsConstructor
@Slf4j
public class UpdateContactActivityValidator {

    public HashMap<String, List<DynamicErrorDTO>> validateUpdateContactActivity(UpdateContactActivityDTO updateContactActivityDTO) {
        final HashMap<String, List<DynamicErrorDTO>> errorMap = new HashMap<>();
        final List<CollecticaseErrorEnum> errorEnums = new ArrayList<>();
        final List<String> errorParams = new ArrayList<>();
        String[] entityContact = null;

        if(StringUtils.isBlank(updateContactActivityDTO.getActivityEntityContact()))
        {
            errorEnums.add(ErrorMessageConstant.GeneralActivityDTODetail.ENTITY_CONTACT_REQUIRED);
        }

        if(StringUtils.isNotBlank(updateContactActivityDTO.getActivityEntityContact())) {
            entityContact = StringUtils.split(updateContactActivityDTO.getActivityEntityContact(), CollecticaseConstants.SEPARATOR);

            if (!List.of(ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE, ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT)
                    .contains(updateContactActivityDTO.getActivityTypeCd())
                    && !CollecticaseConstants.EMP_STRING.equalsIgnoreCase(entityContact[0])
                    && !CollecticaseConstants.REPRESENTATIVE_IND_STRING.equalsIgnoreCase(entityContact[0])
                    && !CollecticaseConstants.NEW_REPRESENTATIVE_IND_STRING.equalsIgnoreCase(entityContact[0])) {
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityName())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_NAME_REQUIRED);
                }
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityAddressLine1())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_ADDRESS_LINE1_REQUIRED);
                }
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityCity())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CITY_REQUIRED);
                }
                if (updateContactActivityDTO.getEntityCountry() == null) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_COUNTRY_REQUIRED);
                }
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityState())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_STATE_REQUIRED);
                }
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityZip())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_ZIP_REQUIRED);
                }
                if (StringUtils.isBlank(updateContactActivityDTO.getEntityTelephone())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_TELEPHONE_REQUIRED);
                }
                if (updateContactActivityDTO.getEntityPreference() == null) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_PREFERENCE_REQUIRED);
                }

                if (UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactPreference(),
                        CollecticaseConstants.ENTITY_PREFERENCE_FAX)) {
                    {
                        if (StringUtils.isBlank(updateContactActivityDTO.getEntityFax())) {
                            errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_FAX_REQUIRED);
                        }
                    }

                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.UI_ACCT_NBR_PATTERN,
                            updateContactActivityDTO.getEntityUIAcctNBR())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_UI_ACCT_NBR_INVALID);
                    }

                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.FEIN_NUMERIC_PATTERN,
                            updateContactActivityDTO.getEntityFEIN())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_FEIN_INVALID);
                    }

                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                            updateContactActivityDTO.getEntityAddressLine1())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_ADDRESS_INVALID);
                    }
                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                            updateContactActivityDTO.getEntityAddressLine2())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_ADDRESS_INVALID);
                    }
                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHA_PATTERN,
                            updateContactActivityDTO.getEntityCity())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CITY_INVALID);
                    }
                    if (!CollecticaseUtilFunction.isValidZip(updateContactActivityDTO.getEntityCountry(), updateContactActivityDTO.getEntityState())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_ZIP_INVALID);
                    }
                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                            updateContactActivityDTO.getEntityCity())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_PHONE_INVALID);
                    }
                    if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                            updateContactActivityDTO.getEntityFax())) {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_FAX_INVALID);
                    }
                }
            }
        }

        if(List.of(ACTIVITY_TYPE_ADD_UPD_ATTY_CONTACT,ACTIVITY_TYPE_ADD_UPD_OTHER_REP_CONTACT).contains(updateContactActivityDTO.getActivityTypeCd()))
        {
            if(StringUtils.isBlank(updateContactActivityDTO.getEntityRepresentedFor()))
            {
                errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_REPRESENTED_FOR_REQUIRED);
            }
        }

        if(UtilFunction.compareLongObject.test(updateContactActivityDTO.getActivityTypeCd(),
                CollecticaseConstants.ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT))
        {
            if(updateContactActivityDTO.getEntityContactId() == null ||
                    UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactId(),
                            -1L) )
            {
                errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_REQUIRED);
            }
        }

        if(!List.of(ACTIVITY_TYPE_DISASSOCIATE_ORG_FROM_CASE,ACTIVITY_TYPE_DISASSOCIATE_ORG_CONTACT)
                .contains(updateContactActivityDTO.getActivityTypeCd())
                    &&  updateContactActivityDTO.getEntityContactId() != null)
        {
            if(CollecticaseConstants.REPRESENTATIVE_IND_STRING.equalsIgnoreCase(entityContact[0])
                    || CollecticaseConstants.NEW_REPRESENTATIVE_IND_STRING.equalsIgnoreCase(entityContact[0]))
            {
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactFirstName()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_FIRST_NAME_REQUIRED);
                }
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactLastName()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_LAST_NAME_REQUIRED);
                }
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactAddressLine1()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_ADDRESS_REQUIRED);
                }
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactCity()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_CITY_REQUIRED);
                }
                if(updateContactActivityDTO.getEntityContactCountry() == null)
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_CITY_REQUIRED);
                }
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactState()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_STATE_REQUIRED);
                }
                if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactZip()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_ZIP_REQUIRED);
                }
                if(updateContactActivityDTO.getEntityContactPreference() == null)
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_PREFERENCE_REQUIRED);
                }
                if(UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactPreference(),
                        CollecticaseConstants.ENTITY_CONTACT_PREFERENCE_WORK_PHONE))
                {
                    if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactPhoneWork()))
                    {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_WORK_PHONE_REQUIRED);
                    }
                }

                if(StringUtils.isNotBlank(updateContactActivityDTO.getEntityContactPhoneWorkExt())
                        && StringUtils.isBlank(updateContactActivityDTO.getEntityContactPhoneWork()))
                {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_EXT_PROVIDED_WORK_PHONE_REQUIRED);
                }

                if(UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactPreference(),
                        CollecticaseConstants.ENTITY_CONTACT_PREFERENCE_HOME_PHONE))
                {
                    if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactPhoneHome()))
                    {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_HOME_PHONE_REQUIRED);
                    }
                }

                if(UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactPreference(),
                        CollecticaseConstants.ENTITY_CONTACT_PREFERENCE_CELL_PHONE))
                {
                    if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactPhoneCell()))
                    {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_CELL_PHONE_REQUIRED);
                    }
                }
                if(UtilFunction.compareLongObject.test(updateContactActivityDTO.getEntityContactPreference(),
                        CollecticaseConstants.ENTITY_CONTACT_PREFERENCE_FAX))
                {
                    if(StringUtils.isBlank(updateContactActivityDTO.getEntityContactFax()))
                    {
                        errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_FAX_REQUIRED);
                    }
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactFirstName())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_FIRST_NAME_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactLastName())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_LAST_NAME_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactAddressLine1())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_ADDRESS_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactAddressLine2())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_ADDRESS_LINE_2_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.ALPHANUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactCity())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_CITY_INVALID);
                }

                if (!CollecticaseUtilFunction.isValidZip(updateContactActivityDTO.getEntityContactCountry(),
                        updateContactActivityDTO.getEntityContactState())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_ZIP_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                        updateContactActivityDTO.getEntityContactPhoneWork())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_WORK_PHONE_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.NUMERIC_PATTERN,
                        updateContactActivityDTO.getEntityContactPhoneWorkExt())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_WORK_PHONE_EXT_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                        updateContactActivityDTO.getEntityContactPhoneHome())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_HOME_PHONE_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                        updateContactActivityDTO.getEntityContactPhoneCell())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_CELL_PHONE_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.PHONE_PATTERN,
                        updateContactActivityDTO.getEntityContactFax())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_FAX_INVALID);
                }

                if (!CollecticaseUtilFunction.validateRegExPattern(CollecticaseUtilFunction.EMAILS_PATTERN,
                        updateContactActivityDTO.getEntityContactEmails())) {
                    errorEnums.add(ErrorMessageConstant.UpdateContactActivityDTODetail.ENTITY_CONTACT_EMAILS_INVALID);
                }
            }
        }

        return errorMap;
    }

}
