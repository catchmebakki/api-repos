package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.dto.CorrespondenceDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcaseCmaNoticesCmnRepository extends CrudRepository<CcaseCmaNoticesCmnDAO, Long> {

    @Query("""       
                     SELECT new com.ssi.ms.collecticase.dto.CorrespondenceDTO(
                     ccaseCmaNoticesCmn.correspondenceCorDAO.corId,
                     coalesce(ccaseCmaNoticesCmn.ccaseCraCorrespondenceCrcDAO.crcRptName,
                              ccaseCmaNoticesCmn.correspondenceCorDAO.reportsRptDAO.rptName),
                     TO_CHAR(ccaseCmaNoticesCmn.correspondenceCorDAO.corTs, 'MM/DD/YYYY'),
                     fnInvGetAlvDescription(ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.cmaActivityTypeCd),
                     fnInvGetAlvDescription(ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.cmaRemedyType),
                     fnGetNoticesFilePath(ccaseCmaNoticesCmn.correspondenceCorDAO.corId,
                                    ccaseCmaNoticesCmn.correspondenceCorDAO.corTs,
                                    ccaseCmaNoticesCmn.correspondenceCorDAO.reportsRptDAO.rptFormNbr)
                     )
            from CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmn
                        where ccaseCmaNoticesCmn.ccaseActivitiesCmaDAO.ccaseCasesCmcDAO.cmcId
                                  = :caseId
                        and ccaseCmaNoticesCmn.correspondenceCorDAO.corStatusCd IN (:correspondenceStatusList)
            """)
    List<CorrespondenceDTO> getCaseCorrespondenceByCaseId(Long caseId,
                                                          List<Long> correspondenceStatusList);
}