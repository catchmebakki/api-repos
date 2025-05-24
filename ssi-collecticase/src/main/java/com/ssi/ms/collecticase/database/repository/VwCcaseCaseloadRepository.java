package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderEntityDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseOpmDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseRemedyDAO;
import com.ssi.ms.collecticase.dto.CaseReassignDTO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VwCcaseCaseloadRepository extends CrudRepository<VwCcaseCaseloadDAO, Long> {

    @Query("""   
                     SELECT new com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO(
                     CASE WHEN vwCcaseCaseload.bankrupt = 'Y' THEN 'B' END as claimantBankrupt,
                     CASE WHEN vwCcaseCaseload.bankrupt = 'Y' THEN 'Bankrupt' END as claimantBankruptDesc,
                     CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'F' END as claimantFraud,
                     CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'Fraud' END as claimantFraudDesc,
                     CASE WHEN vwCcaseCaseload.newCase = 'Y' THEN 'New' END as caseStatus,
                     vwCcaseCaseload.caseNo as caseNo,
                     vwCcaseCaseload.caseNo as id,
                     vwCcaseCaseload.claimantName || '-'|| cmtSsn as claimantName,
                     vwCcaseCaseload.casePriorityDesc as casePriorityDesc,
                     vwCcaseCaseload.caseAge as caseAge,
                     vwCcaseCaseload.mostRecentRemedy as mostRecentRemedy,
                     vwCcaseCaseload.caseCharacteristics as caseCharacteristics,
                     vwCcaseCaseload.nextFollowupDate as nextFollowupRemedyWithDate,
                     CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'Fraud Only'
                        WHEN vwCcaseCaseload.nfEarnings = 'Y' THEN 'Non-Fraud Earnings Only'
                        WHEN vwCcaseCaseload.frdNfEarnings = 'Y' THEN 'Fraud and Non-Fraud Earnings Only'
                        WHEN vwCcaseCaseload.nonFrd = 'Y' THEN 'Non-Fraud Only' END as claimantFraudStatus,
                     substr(vwCcaseCaseload.nextFollowupDate, 0, 10) as nextFollowupDate,
                     substr(vwCcaseCaseload.nextFollowupDate, 12) as nextFollowupRemedy)
            from VwCcaseCaseloadDAO vwCcaseCaseload where vwCcaseCaseload.staffId = :staffId
            """)
    Page<VwCcaseCaseloadDTO> getCaseLoadByStaffId(Long staffId, Pageable pageable);

    @Query("""          
            from VwCcaseHeaderDAO vwCcaseHeader where vwCcaseHeader.caseNo = :caseId
            """)
    VwCcaseHeaderDAO getCaseHeaderInfoByCaseId(Long caseId);

    @Query("""          
            from VwCcaseOpmDAO vwCcaseOpm
              JOIN CcaseCasesCmcDAO ccaseCasesCmc
              ON vwCcaseOpm.fkCmtId = ccaseCasesCmc.claimantCmtDAO.cmtId
              where ccaseCasesCmc.cmcId = :caseId
            """)
    List<VwCcaseOpmDAO> getClaimantOpmInfoByCaseId(Long caseId);

    @Query("""          
            from VwCcaseRemedyDAO vwCcaseRemedy where vwCcaseRemedy.cmcId = :caseId
            """)
    List<VwCcaseRemedyDAO> getCaseRemedyInfoByCaseId(Long caseId);

    @Query("""          
            from VwCcaseHeaderEntityDAO vwCcaseHeaderEntity where vwCcaseHeaderEntity.caseId = :caseId
            """)
    List<VwCcaseHeaderEntityDAO> getCaseEntityInfoByCaseId(Long caseId);

    @Query("""      
               SELECT new com.ssi.ms.collecticase.dto.CaseReassignDTO(
                cmtName, TO_CHAR(caseNo),
                CASE WHEN frd = 'Y' THEN 'All Fraud' 
                    WHEN nonFrd = 'Y' THEN 'All Non-Fraud' 
                    WHEN nonFrdEarnings = 'Y' THEN 'All Non-FRD Earnings' END as claimantFraudNonFraudDesc, 
                staffName as claimantCaseAssignedStaffName        
                )    
            from VwCcaseHeaderDAO vwCcaseHeader where vwCcaseHeader.caseNo = :caseId
            """)
    List<CaseReassignDTO> getCaseReassignInfoByCaseId(Long caseId);

    @Query("""      
               SELECT new com.ssi.ms.collecticase.dto.CaseReassignDTO(
                'Mutliple' as claimantName, 'Multiple' as claimantCaseNo,
                CASE WHEN (SELECT count(*)  FROM VwCcaseHeaderDAO ccaseHeaderDAO
                       WHERE ccaseHeaderDAO.caseNo IN (:caseIdList) and ccaseHeaderDAO.frd = 'Y') >= 1 THEN 'All Fraud'
                 WHEN (SELECT count(*)  FROM VwCcaseHeaderDAO ccaseHeaderDAO 
                                WHERE ccaseHeaderDAO.caseNo IN (:caseIdList) and ccaseHeaderDAO.frd = 'Y') = 0 AND
                     (SELECT count(*)  FROM VwCcaseHeaderDAO ccaseHeaderDAO 
                                WHERE ccaseHeaderDAO.caseNo IN (:caseIdList) and ccaseHeaderDAO.nonFrdEarnings = 'Y') 
                                >= 1 THEN 'All Non-FRD Earnings'
                WHEN (SELECT count(*)  FROM VwCcaseHeaderDAO ccaseHeaderDAO 
                             WHERE ccaseHeaderDAO.caseNo IN (:caseIdList) and ccaseHeaderDAO.frd = 'Y') = 0 AND
                     (SELECT count(*)  FROM VwCcaseHeaderDAO ccaseHeaderDAO 
                                WHERE ccaseHeaderDAO.caseNo IN (:caseIdList) and ccaseHeaderDAO.nonFrdEarnings = 'Y') 
                                = 0 THEN 'All Non-Fraud' 
                 ELSE 'Mixed' END as claimantFraudNonFraudDesc,
                staffName as claimantCaseAssignedStaffName        
                )    
            from VwCcaseHeaderDAO vwCcaseHeader where vwCcaseHeader.caseNo IN (:caseIdList)
            """)
    List<CaseReassignDTO> getCaseReassignInfoByCaseIds(List<Long> caseIdList);

    @Query("""
            from GttForCaselookupDAO gttForCaselookupDAO
            """)
    List<GttForCaselookupDAO> getCaseLookupData();

}
