package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.dto.wscc.ConfigWsccListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListResDTO;

import java.util.Date;

public interface CustomWsccRepository {

	ConfigWsccListResDTO filterWsccBasedLookupCriteria(ConfigWsccListReqDTO wsccReqDTO, Date systemDate);

}
