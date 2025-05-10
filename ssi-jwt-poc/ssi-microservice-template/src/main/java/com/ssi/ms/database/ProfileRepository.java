package com.ssi.ms.database;


import com.ssi.ms.database.dao.ProfileDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ProfileRepository extends CrudRepository<ProfileDAO, Long> { }
