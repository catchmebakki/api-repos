package com.ssi.ms.database.servcie;

import com.ssi.ms.database.dao.NhuisLogNhl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhuisLogNhlService extends CrudRepository<NhuisLogNhl, Long> {
}
