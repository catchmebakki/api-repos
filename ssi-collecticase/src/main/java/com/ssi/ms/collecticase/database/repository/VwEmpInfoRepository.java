package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.VwEmpInfoDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VwEmpInfoRepository extends CrudRepository<VwEmpInfoDAO, Long> {

    @Query("""          
			from VwEmpInfoDAO vwEmpInfoDAO
			            where vwEmpInfoDAO.empId = :empId
			""")
    List<VwEmpInfoDAO> getEmployerInfo(Long empId);
}
