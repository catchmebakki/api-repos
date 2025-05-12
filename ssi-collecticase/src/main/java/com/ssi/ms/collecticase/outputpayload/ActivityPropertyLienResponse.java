package com.ssi.ms.collecticase.outputpayload;

import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import lombok.Data;
import java.util.List;

@Data
public class ActivityPropertyLienResponse {
    // Property Lein
    List<CcaseCountyCtyDTO> countyCodesCtyList;

    Long propertyLien;
}
