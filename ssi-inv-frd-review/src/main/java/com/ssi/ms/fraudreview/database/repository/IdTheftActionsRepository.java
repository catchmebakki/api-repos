package com.ssi.ms.fraudreview.database.repository;

import org.springframework.data.repository.CrudRepository;

import com.ssi.ms.fraudreview.database.dao.IdTheftActionsITADAO;

public interface IdTheftActionsRepository  extends CrudRepository<IdTheftActionsITADAO, Long> {
	IdTheftActionsITADAO save(IdTheftActionsITADAO idTheftActionsDAO);
}