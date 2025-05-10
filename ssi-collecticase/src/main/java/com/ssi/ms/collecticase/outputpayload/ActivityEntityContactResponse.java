package com.ssi.ms.collecticase.outputpayload;

import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ActivityEntityContactResponse {
    // Entity Contact showInd
    boolean disableEntityContact;
        // Entity Contact
        List<VwCcaseEntityDTO> entityList;

}
