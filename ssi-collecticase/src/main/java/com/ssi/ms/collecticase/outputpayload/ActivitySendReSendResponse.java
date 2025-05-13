package com.ssi.ms.collecticase.outputpayload;

import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import lombok.Data;
import java.util.List;

@Data
public class ActivitySendReSendResponse {
    // Send/Resend - Correspondence
    List<CcaseCraCorrespondenceCrcDTO> sendNoticesCrcList;
    List<CcaseCraCorrespondenceCrcDTO> manualNoticesCrcList;
    List<CcaseCraCorrespondenceCrcDTO> resendNoticesCrcList;
}
