package com.ssi.ms.resea.validator;


import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.dto.IssuesDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;

/**
 * {@code ReseaCommonValidator} is a validation component in the application responsible for
 * handling common validation in RESEA.
 *
 * @author Anand
 */

public interface ReseaCommonValidator {

	static void validateCreateIssue(List<IssuesDTO> issuesDTOList, Date clmByeDt, List<ReseaErrorEnum> errorEnums) {
		if (!CollectionUtils.isEmpty(issuesDTOList)) {
			for (IssuesDTO issuesDTO : issuesDTOList) {
				if (Stream.of(issuesDTO.getIssueId(), issuesDTO.getStartDt()).anyMatch(Objects::isNull)) {
					errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ISSUE_INVALID);
				}
				else {
					if (null != clmByeDt && issuesDTO.getStartDt().after(clmByeDt)) {
						errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ISSUE_START_DT_INVALID);
					}
				}

				if (null != issuesDTO.getEndDt()) {
					if(!checkFutureDate.test(issuesDTO.getEndDt(), issuesDTO.getStartDt())) {
						errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ISSUE_END_DT_INVALID);
					}
					if (null != clmByeDt && issuesDTO.getEndDt().after(clmByeDt)) {
						errorEnums.add(ErrorMessageConstant.CommonErrorDetail.CREATE_ISSUE_END_DT_PRIOR_TO_BYE);
					}
				}
			}
		}
	}
}
