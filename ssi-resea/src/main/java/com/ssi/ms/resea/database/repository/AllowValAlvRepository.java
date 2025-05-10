package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.AllowValAlvDAO;
import com.ssi.ms.resea.dto.ActivityTypeAllowValueResDTO;
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
			from AllowValAlvDAO alv where alv.alvActiveInd = 'Y'
			and alv.allowCatAlcDAO.alcId = :alcId
			and alv.alvDecipherCode like %:decipherCode%
			ORDER BY alv.alvSortOrderNbr asc, alv.alvId asc
			""")
    List<AllowValAlvDAO> getActiveAlvsByAlcAndDecipherCode(Long alcId,
														   String decipherCode);

	@Query("""
   			select new com.ssi.ms.resea.dto.ActivityTypeAllowValueResDTO(
   			alv.alvId,
   			alv.alvShortDecTxt,
   			rsac.rsacTemplatePage)
			from AllowValAlvDAO alv
			JOIN ReseaActivityConfigRsacDAO rsac ON rsac.rsacActivityCd = alv.alvId
			where alv.alvActiveInd = 'Y'
			and alv.allowCatAlcDAO.alcId = :alcId
			and rsac.rsacTemplatePage != 'GEN'
			and SYSDATE between rsac.rsacEffectiveDt and COALESCE(rsac.rsacEffUntilDt, COD('12/31/2999'))
			ORDER BY alv.alvSortOrderNbr asc, alv.alvId asc
			""")
	List<ActivityTypeAllowValueResDTO> getActivityTypeAlvs(Long alcId);

}
