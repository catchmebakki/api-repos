package com.ssi.ms.database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssi.ms.database.ProfileRepository;
import com.ssi.ms.database.dao.ProfileDAO;
import com.ssi.ms.database.mapper.ProfileMapper;
import com.ssi.ms.dto.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfileDatabaseManagerService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    public Profile getProfileForid(final Long id) {
        var profileDao = new ProfileDAO();
        profileDao.setId(id);
        return profileMapper.daoToDto(profileRepository.findById(profileDao.getId()).get());
    }

    public boolean saveProfile(final Profile profile) {
        var profileDAO = profileRepository.save(profileMapper.dtoToDao(profile));
        return profileDAO != null;
    }

    public long addProfile(final Profile profile) {
        var profileDAO = profileRepository.save(profileMapper.dtoToDao(profile));
        return profileDAO.getId();
    }

    public void deleteProfile(final Long id) {
        var profileDao = new ProfileDAO();
        profileDao.setId(id);
        profileRepository.deleteById(profileDao.getId());
    }
}
