package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.LocalOfficeLofDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalOfficeLofRepository extends CrudRepository<LocalOfficeLofDAO, Long> {

    @Query("""
            from LocalOfficeLofDAO lof where lof.lofDisplayInd = 'Y'
            """)
    List<LocalOfficeLofDAO> findAllActiveBusinessUnits();


}
