package com.ssi.ms.masslayoff.service;

import com.ssi.ms.masslayoff.database.mapper.EmployerMapper;
import com.ssi.ms.masslayoff.database.repository.EmployerEmpRepository;
import com.ssi.ms.masslayoff.dto.employer.EmployerDetailDTO;
import com.ssi.ms.masslayoff.dto.employer.EmployerGroupByAccNbrDTO;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_NOT_FOUND;

/**
 * @author Praveenraja Paramsivam
 * EmployerService provides service to employer details using empUiAcctNbr
 */
@Slf4j
@Service
public class EmployerService {

    @Autowired
    private EmployerEmpRepository employerEmpRepository;

    @Autowired
    private EmployerMapper employerMapper;

    /**
     * Retrieve employer details based on the provided employer UI account number.
     *
     * @param empUiAcctNbr {@link String} The employer UI account number associated with the employer details to retrieve.
     * @return {@link EmployerGroupByAccNbrDTO} The EmployerGroupByAccNbrDTO containing the response for the employer details request.
     */
    public EmployerGroupByAccNbrDTO getEmployerDetails(String empUiAcctNbr) {
       final List<EmployerDetailDTO> employerDetailDTOS = employerEmpRepository.getEmployerByAccNbrAndKillDate(empUiAcctNbr).stream()
                .map(dao -> employerMapper.daoToDto(dao))
                .toList();
        return Optional.of(employerDetailDTOS)
                .filter(dtos -> !Collections.isEmpty(dtos))
                .map(dtos -> EmployerGroupByAccNbrDTO.builder()
                        .empUiAcctNbr(empUiAcctNbr)
                        .details(dtos)
                        .build())
                .orElseThrow(() -> new NotFoundException("No employer found for " + empUiAcctNbr, EMPLOYER_NOT_FOUND));
    }
}
