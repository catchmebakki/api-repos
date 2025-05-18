package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.StateStaDAO;
import com.ssi.ms.collecticase.dto.StateDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<StateStaDAO, Long> {

	@Query("""
			SELECT new com.ssi.ms.collecticase.dto.StateDTO(
			            stateStaDAO.staCd as stateCode, stateStaDAO.staDescTxt as stateName)
			from StateStaDAO stateStaDAO
			where stateStaDAO.staProvinceInd = :staProvinceInd
			ORDER BY stateStaDAO.staSortOrderNbr asc
			""")
	List<StateDTO> getStates(@Param("staProvinceInd") String staProvinceInd);

}
