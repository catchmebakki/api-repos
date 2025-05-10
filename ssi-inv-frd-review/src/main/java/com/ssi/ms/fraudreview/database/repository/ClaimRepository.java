package com.ssi.ms.fraudreview.database.repository;

import org.springframework.data.repository.CrudRepository;

import com.ssi.ms.fraudreview.database.dao.ClaimClmDAO;

public interface ClaimRepository extends CrudRepository<ClaimClmDAO, Long> {
}
