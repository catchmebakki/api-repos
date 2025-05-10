package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ClmLofClfDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClmLofClfRepository extends CrudRepository<ClmLofClfDao, Long> {
    @Query("""
            FROM ClmLofClfDao clf WHERE
            clf.claimClmDAO.clmId = :clmId
            and clf.clfActiveInd = 'Y'
            and clf.localOfficeLofDAO.lofDisplayInd = 'Y'
            and clf.localOfficeLofDAO.lofBuTypeCd = :lofBuTypeCd
            and clf.localOfficeLofDAO.lofId != :interstateLof
            """)
    List<ClmLofClfDao> getLofByClmId(Long clmId, Long lofBuTypeCd, Long interstateLof);


}
