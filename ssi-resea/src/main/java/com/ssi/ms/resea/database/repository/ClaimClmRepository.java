package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.ClaimClmDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimClmRepository extends CrudRepository<ClaimClmDAO, Long> {


}
