package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CmtNotesCnoDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmtNotesCnoRepository extends CrudRepository<CmtNotesCnoDAO, Long> {


}
