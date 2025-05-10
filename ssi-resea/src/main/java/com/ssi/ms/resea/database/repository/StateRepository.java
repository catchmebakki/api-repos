package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.StateStaDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<StateStaDAO, Long> {
	@Query("""
			from StateStaDAO sta where sta.staProvinceInd = :staProvinceInd
			ORDER BY sta.staSortOrderNbr asc
			""")
	List<StateStaDAO> getStates(@Param("staProvinceInd") String staProvinceInd);

}
