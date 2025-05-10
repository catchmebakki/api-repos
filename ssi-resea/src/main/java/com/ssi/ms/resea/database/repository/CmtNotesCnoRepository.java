package com.ssi.ms.resea.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.ssi.ms.resea.database.dao.CmtNotesCnoDao;

@Repository
public interface CmtNotesCnoRepository extends CrudRepository<CmtNotesCnoDao, Long> {


}
