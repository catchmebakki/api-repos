package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.VwCcaseCollectibleDebtsDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCountyCtyDAO;
import com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface VwCcaseCountyCtyRepository extends CrudRepository<VwCcaseCountyCtyDAO, Long> {

    @Query("""
            SELECT new com.ssi.ms.collecticase.dto.CcaseCountyCtyDTO(
            ctyId as countyId, ctyName as countyName)
            from VwCcaseCountyCtyDAO vwCcaseCountyCtyDAO
			where vwCcaseCountyCtyDAO.stateSta.staCd = :stateCd
			ORDER BY vwCcaseCountyCtyDAO.ctyName desc
			""")
    List<CcaseCountyCtyDTO> getPropertyLienActivityPage(String stateCd);
}
