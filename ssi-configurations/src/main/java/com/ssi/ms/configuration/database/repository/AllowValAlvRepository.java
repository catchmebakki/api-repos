package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.AllowCatAlcDAO;
import com.ssi.ms.configuration.database.dao.AllowValAlvDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowValAlvRepository extends CrudRepository<AllowValAlvDAO, Long> {

    Page<AllowValAlvDAO> findBy(Long fkAlcId, Pageable pageable);

    Page<AllowValAlvDAO> findByAllowCatAlcDAOAndAlvActiveInd(AllowCatAlcDAO alcDao, String alvActiveInd, Pageable pageable);

    Page<AllowValAlvDAO> findByAllowCatAlcDAO(AllowCatAlcDAO alcDao, Pageable pageable);

	@Query("""
			SELECT distinct alv.alvDecipherCode
			FROM AllowValAlvDAO alv
			WHERE alv.allowCatAlcDAO.alcId = :alcId
			AND alv.alvDecipherCode IS NOT NULL AND alv.alvDecipherCode != 'NA'
			ORDER BY alv.alvDecipherCode ASC
			""")
	List<String> getAlvDecipherCdByAlcId(Long alcId);

	@Query("""
			from AllowValAlvDAO alv where alv.alvDecipherCode = 'Y' and alv.allowCatAlcDAO.alcId = :alcId
			ORDER BY alv.alvSortOrderNbr asc
			""")
	List<AllowValAlvDAO> getDisplayOnList(Long alcId);
}
