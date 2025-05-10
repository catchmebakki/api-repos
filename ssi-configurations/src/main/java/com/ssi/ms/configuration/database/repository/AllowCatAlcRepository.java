package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.AllowCatAlcDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowCatAlcRepository extends CrudRepository<AllowCatAlcDAO, Long> {
    Iterable<AllowCatAlcDAO> findAllByAlcCategoryCd(Long alvCategoryCd);
}
