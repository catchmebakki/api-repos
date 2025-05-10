package com.ssi.ms.collecticase.service;
        import com.ssi.ms.common.database.dao.ParameterParDao;
        import com.ssi.ms.common.database.repository.ParameterParRepository;
        import com.ssi.ms.common.service.UserService;
        import com.ssi.ms.platform.exception.custom.CustomValidationException;
        import com.ssi.ms.platform.exception.custom.NotFoundException;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COLC_NO_OF_LKUP_REC;
        import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PAR_COLC_NO_OF_LKUP_REC;
        import java.util.HashMap;
        import java.util.Map;


@Slf4j
@Service
public class CollecticaseParameterService {
    @Autowired
    private ParameterParRepository commonParRepository;

    public Map<String, Long> getParametersValues() {
        return new HashMap<>(){{
            put(PAR_COLC_NO_OF_LKUP_REC, commonParRepository.findByParShortName(COLC_NO_OF_LKUP_REC).getParNumericValue());
        }};
    }
}
