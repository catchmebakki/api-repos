package com.ssi.ms.masslayoff.controller;

import com.ssi.ms.masslayoff.service.EmployerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_ACC_N0_MANDATORY;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.EMPLOYER_ACC_NO_NOT_10_DIGIT;
/**
 * @author Praveenraja Paramsivam
 * EmployerController provides services to get Employer details
 */
@RestController
@RequestMapping("/employer")
@Validated
@Slf4j
@CrossOrigin
public class EmployerController {
    @Autowired
    private EmployerService employerService;
    /**
     * getEmployerDetails method will return EmployerDetails based on the provided employer UI account number.
     *
     * @param empUiAcctNbr {@link String} The employer UI account number.
     * @return {@link ResponseEntity} ResponseEntity<EmployerGroupByAccNbrDTO> contains the response data.
     */
    @GetMapping(path = "/{empUiAcctNbr}", produces = "application/json")
    public ResponseEntity getEmployerDetails(
            @Valid
            @NotNull(message = EMPLOYER_ACC_N0_MANDATORY)
            @Size(min = 10, max = 10, message = EMPLOYER_ACC_NO_NOT_10_DIGIT)
            @PathVariable("empUiAcctNbr") String empUiAcctNbr) {
        return ResponseEntity.ok().body(employerService.getEmployerDetails(empUiAcctNbr));
    }


}
