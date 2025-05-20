package com.ssi.ms.collecticase.outputpayload;

import com.ssi.ms.collecticase.dto.VwCcaseEntityDTO;
import lombok.Data;

import java.util.List;

@Data
public class ActivityEntityContactResponse {
    // Entity Contact showInd
    Boolean disableEntityContact;
    // Entity Contact
    List<VwCcaseEntityDTO> entityList;
}
