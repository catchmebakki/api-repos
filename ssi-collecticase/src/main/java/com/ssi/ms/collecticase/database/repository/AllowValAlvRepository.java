package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.AllowValAlvDAO;
import com.ssi.ms.collecticase.dto.AllowValueResDTO;
import com.ssi.ms.collecticase.dto.AlvDescResDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowValAlvRepository extends CrudRepository<AllowValAlvDAO, Long> {

    @Query("""
			from AllowValAlvDAO alv where alv.alvActiveInd = 'Y'
			and alv.allowCatAlcDAO.alcId = :alcId
			ORDER BY alv.alvSortOrderNbr asc, alv.alvId asc
			""")
    List<AllowValAlvDAO> getActiveAlvsByAlc(Long alcId);

	@Query("""   
            SELECT new com.ssi.ms.collecticase.dto.AlvDescResDTO(
            fnInvGetAlvDescription(alv.alvId) as desc
            )
            from AllowValAlvDAO alv where alv.alvId = :alvId
            """)
	AlvDescResDTO getShortDescByAlc(Long alvId);

}
