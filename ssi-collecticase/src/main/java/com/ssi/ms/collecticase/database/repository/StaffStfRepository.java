package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.StaffStfDAO;
import com.ssi.ms.collecticase.dto.StaffDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffStfRepository extends CrudRepository<StaffStfDAO, Long> {

    @Query("""          
			from StaffStfDAO staffStf
			            where staffStf.userDAO.userId = :userId
			""")
    List<StaffStfDAO> getStaffInfoByUserId(Long userId);

//	@Query("""
//          SELECT new com.ssi.ms.collecticase.dto.StaffDTO(
//                lofStaffLsfDAO.stfDAO.stfId as staffId,
//                lofStaffLsfDAO.stfDAO.stfFirstName||' '||lofStaffLsfDAO.stfDAO.stfLastName as staffName)
//       from LofStaffLsfDAO lofStaffLsfDAO join UsrRolUrlDAO usrRolUrlDAO
//          on lofStaffLsfDAO.stfDAO.userDAO.userId = usrRolUrlDAO.userDAO.userId
//                   where lofStaffLsfDAO.lofDAO.lofId = :localOfficeId
//                   and lofStaffLsfDAO.stfDAO.userDAO.usrStatusCd = :userStatusCd
//                   and usrRolUrlDAO.fkRolId IN (:roleList)
//           order by lofStaffLsfDAO.stfDAO.stfFirstName, lofStaffLsfDAO.stfDAO.stfLastName
//       """)
//	List<StaffDTO> getStaffListByLofAndRole(Long localOfficeId, Long userStatusCd, List<Long> roleList);

}
