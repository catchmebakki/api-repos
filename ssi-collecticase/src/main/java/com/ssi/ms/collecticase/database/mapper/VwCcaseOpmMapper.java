package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.database.dao.VwCcaseHeaderDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseOpmDAO;
import com.ssi.ms.collecticase.dto.VwCcaseHeaderDTO;
import com.ssi.ms.collecticase.dto.VwCcaseOpmDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VwCcaseOpmMapper {

    @Mapping(target = "claimantId", source = "fkCmtId")
    @Mapping(target = "claimantDtmNBR", source = "dtmNbr")
    @Mapping(target = "claimantDtmVersionNBR", source = "versionnbr")
    @Mapping(target = "claimantDtmMailedDt", source = "dtmMailedDt")
    @Mapping(target = "claimantOpmAmount", source = "totalAmt")
    @Mapping(target = "claimantOpmBalAmount", source = "opmBal")
    @Mapping(target = "claimantFraudInd", source = "fraudInd")
    @Mapping(target = "claimantBankruptcyOpmStatusDesc", source = "bopstatusdesc")
    @Mapping(target = "claimantBankruptcyOpmDate", source = "bopdate")
    VwCcaseOpmDTO daoToDto(VwCcaseOpmDAO vwCcaseOpmDAO);
}
