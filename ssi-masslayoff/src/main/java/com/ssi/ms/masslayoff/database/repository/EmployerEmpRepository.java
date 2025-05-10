package com.ssi.ms.masslayoff.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssi.ms.masslayoff.database.dao.EmployerEmpDAO;
/**
 * @author Praveenraja Paramsivam
 * Repository for accessing and managing EmployerEmpDAO entities.
 */
@Repository
public interface EmployerEmpRepository extends JpaRepository<EmployerEmpDAO, Long> {
	/**
	 * Retrieves a list of active employee data objects based on the account number and other criteria.
	 * @param empUiAcctNbr {@link String} The account number to search for.
	 * @return A list of Employee data objects matching the criteria.
	 */
    @Query("""
        SELECT  new com.ssi.ms.masslayoff.database.dao.EmployerEmpDAO(
            emp.empId
            , emp.empName
            , emp.empUiAcctLoc)
        FROM EmployerEmpDAO emp
        WHERE emp.empUiAcctNbr LIKE :empUiAcctNbr
            AND emp.empKilledDt IS NULL
            AND (emp.empDeleteInd = 'N' OR emp.empDeleteInd IS NULL)
            order by emp.empUiAcctLoc, emp.empName
        """)
    List<EmployerEmpDAO> getEmployerByAccNbrAndKillDate(String empUiAcctNbr);
    /**
     * Retrieves an employee data object based on the provided account number and location.
     * @param empUiAcctNbr {@link String} account number to search for.
     * @param empUiAcctLoc {@link String} location associated with the account number.
     * @return An Employee data object matching the account number and location.
     */
    EmployerEmpDAO findByEmpUiAcctNbrAndEmpUiAcctLoc(String empUiAcctNbr, String empUiAcctLoc);

}
