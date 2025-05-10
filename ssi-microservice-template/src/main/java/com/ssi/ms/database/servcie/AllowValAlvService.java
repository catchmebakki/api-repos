package com.ssi.ms.database.servcie;

import com.ssi.ms.database.dao.AllowValAlv;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowValAlvService extends CrudRepository<AllowValAlv, Long> {
}
