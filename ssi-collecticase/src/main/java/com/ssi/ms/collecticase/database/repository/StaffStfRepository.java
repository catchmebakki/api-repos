package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.OpmPayPlanOppDAO;
import com.ssi.ms.collecticase.database.dao.StaffStfDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StaffStfRepository extends CrudRepository<StaffStfDAO, Long> {

    @Query("""          
			from StaffStfDAO staffStf
			            where staffStf.userDAO.userId = :userId
			""")
    List<StaffStfDAO> getStaffInfoByUserId(Long userId);

}
