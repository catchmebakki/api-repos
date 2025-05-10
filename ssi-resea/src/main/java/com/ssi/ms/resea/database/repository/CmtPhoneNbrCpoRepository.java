package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ClaimClmDAO;
import com.ssi.ms.resea.database.dao.CmtPhoneNbrCpoDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CmtPhoneNbrCpoRepository extends CrudRepository<CmtPhoneNbrCpoDAO, Long> {

    CmtPhoneNbrCpoDAO findByFkCmtIdAndCpoPhoneNbrPref(Long fkCmtId, Short cpoPhoneNbrPref);
}
