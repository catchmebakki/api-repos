package com.ssi.ms.handler;


import com.ssi.ms.database.ProfileDatabaseManager;
import com.ssi.ms.dto.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileHandler {
    @Autowired
    private ProfileDatabaseManager profileDatabaseManager;
    public Profile getProfileForId(final long id) {
        return profileDatabaseManager.getProfileForid(id);
    }
    public long addProfile(final Profile profile) {
        return profileDatabaseManager.addProfile(profile);
    }
    public boolean saveProfile(final Profile profile) {
        return profileDatabaseManager.saveProfile(profile);
    }

    public boolean deleteProfile(final long id) {
        profileDatabaseManager.deleteProfile(id);
        return true;
    }
}
