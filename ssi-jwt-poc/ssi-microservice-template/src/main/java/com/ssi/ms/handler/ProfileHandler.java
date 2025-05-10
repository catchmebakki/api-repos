package com.ssi.ms.handler;


import com.ssi.ms.database.service.ProfileDatabaseManagerService;
import com.ssi.ms.dto.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileHandler {
    @Autowired
    private ProfileDatabaseManagerService profileDatabaseManagerService;

    public Profile getProfileForId(final long id) {
        return profileDatabaseManagerService.getProfileForid(id);
    }
    public long addProfile(final Profile profile) {
        return profileDatabaseManagerService.addProfile(profile);
    }
    public boolean saveProfile(final Profile profile) {
        return profileDatabaseManagerService.saveProfile(profile);
    }

    public boolean deleteProfile(final long id) {
        profileDatabaseManagerService.deleteProfile(id);
        return true;
    }
}
