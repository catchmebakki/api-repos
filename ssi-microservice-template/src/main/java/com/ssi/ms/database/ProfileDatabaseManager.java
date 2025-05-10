package com.ssi.ms.database;

import com.ssi.ms.database.dao.ProfileDAO;
import com.ssi.ms.database.mapper.ProfileMapper;
import com.ssi.ms.database.servcie.ProfileService;
import com.ssi.ms.dto.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ProfileDatabaseManager {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileMapper profileMapper;

    public Profile getProfileForid(final Long id) {
        var profileDao = new ProfileDAO();
        profileDao.setId(id);
        return profileMapper.daoToDto(profileService.findById(profileDao.getId()).get());
    }

    public boolean saveProfile(final Profile profile) {
        var profileDAO = profileService.save(profileMapper.dtoToDao(profile));
        return profileDAO != null;
    }

    public long addProfile(final Profile profile) {
        var profileDAO = profileService.save(profileMapper.dtoToDao(profile));
        return profileDAO.getId();
    }

    public void deleteProfile(final Long id) {
        var profileDao = new ProfileDAO();
        profileDao.setId(id);
        profileService.deleteById(profileDao.getId());
    }
}
