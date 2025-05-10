package com.ssi.ms.resea.service;

import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant.LofBuTypeCd;
import com.ssi.ms.resea.constant.ReseaConstants.INDICATOR;
import com.ssi.ms.resea.database.dao.ClmLofClfDao;
import com.ssi.ms.resea.database.mapper.LocalOfficeLofMapper;
import com.ssi.ms.resea.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.resea.database.repository.LofStfLsfRepository;
import com.ssi.ms.resea.dto.OfficeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.LOF_INTERSTATE;
import static com.ssi.ms.resea.constant.ReseaConstants.ROL_RESEA_CASE_MANAGER;

@Slf4j
@Service
public class ReseaLocalOfficeService  extends ReseaBaseService {
    @Autowired
    private LofStfLsfRepository lsfRepository;
    @Autowired
    private LocalOfficeLofRepository lofRepository;
    @Autowired
    private LocalOfficeLofMapper lofMapper;

    public String getCaseLocalOfficeName(Long caseId) {
        final List<String> localOfficeNames = lsfRepository.getCaseLocalOfficeName(caseId);
        return CollectionUtils.isEmpty(localOfficeNames) ? null : localOfficeNames.get(0);
    }

    public List<OfficeResDTO> getLookupLocalOffices() {
        return lofRepository.findAllByLofBuTypeCd(LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE)
                .stream().map(dao -> lofMapper.daoToDto(dao)).toList();
    }

    public String getLofNameByClmId(Long clmId) {
        String cmtLofOfficeName = null;
        List<ClmLofClfDao> clmLofClfDaoList = null;
        clmLofClfDaoList = clfRepo.getLofByClmId(clmId, LofBuTypeCd.LOCAL_OFFICE.getCode(), LOF_INTERSTATE);
        if (clmLofClfDaoList != null && !CollectionUtils.isEmpty(clmLofClfDaoList)) {
            cmtLofOfficeName = clmLofClfDaoList.get(0).getLocalOfficeLofDAO().getLofName();
        }
        return CollectionUtils.isEmpty(clmLofClfDaoList) ? null : cmtLofOfficeName;
    }

    public List<OfficeResDTO> getStaffLocalOffice(Long userId) {
        return lofRepository.findLocalOfficesByStaffRole(LofBuTypeCd.LOCAL_OFFICE.getCode(), userId,
                        Long.valueOf(ROL_RESEA_CASE_MANAGER), ReseaAlvEnumConstant.UsrStatusCd.ACTIVE.getCode())
                .stream().map(dao -> lofMapper.daoToDto(dao)).toList();
    }
}
