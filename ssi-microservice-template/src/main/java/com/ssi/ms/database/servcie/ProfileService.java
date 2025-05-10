package com.ssi.ms.database.servcie;


import com.ssi.ms.database.dao.ProfileDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ProfileService extends CrudRepository<ProfileDAO, Long> { }
