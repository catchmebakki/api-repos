package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.ReportsRptDAO;
import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Mapper(componentModel = "spring")
public interface CcaseCraCorrespondenceCrcMapper {

    @Mapping(target = "remedyActivityCorrId", source = "ccaseCraCorrespondenceCrcDAO.crcId")
    @Mapping(target = "remedyActivityCorrName", source = "ccaseCraCorrespondenceCrcDAO.reportsRptDAO.rptName")
    @Mapping(target = "remedyActivityCorrEnable", source = "ccaseCraCorrespondenceCrcDAO.crcEnable")
    @Mapping(target = "remedyActivityCorrCounty", source = "ccaseCraCorrespondenceCrcDAO.crcCounty")
    @Mapping(target = "remedyActivityCorrAutoSet", source = "ccaseCraCorrespondenceCrcDAO.crcAutoSet")
    @Mapping(target = "remedyActivityCorrCurrentFiling", source = "ccaseCraCorrespondenceCrcDAO.crcCurrFiling")
    @Mapping(target = "remedyActivityCorrPaymentCategory", source = "ccaseCraCorrespondenceCrcDAO.crcPmtCategory")
    @Mapping(target = "remedyActivityCorrDoNotGarnish", source = "ccaseCraCorrespondenceCrcDAO.crcDoNotGarnish")
    @Mapping(target = "remedyActivityCorrCourtOrdered", source = "ccaseCraCorrespondenceCrcDAO.crcCourtOrdered")
    CcaseCraCorrespondenceCrcDTO dropdownDaoToDto(CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO);
}

