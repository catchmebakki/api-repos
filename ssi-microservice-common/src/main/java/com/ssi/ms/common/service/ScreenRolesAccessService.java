package com.ssi.ms.common.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssi.ms.common.database.dao.ScreenRolesDao;
import com.ssi.ms.common.database.repository.ScreenRoleAccessRepository;
import com.ssi.ms.constant.CommonConstant;

/**
 * @author munirathnam.surepall The {@code ScreenRolesAccessService} class
 *         provides methods to manage access control for screens based on user
 *         roles.
 */
@Service
public class ScreenRolesAccessService {

	@Value("${application.screen-id}")
	private Long screenId;

	@Value("${application.screen-id-reseaConfig:0}")
	private Long screenIdReseaConfig;
	private Map<Integer, String> roleAccessMap;

	private String scope;
	@Autowired
	private ScreenRoleAccessRepository screenRoleAccessRepo;
	/**
	 * This method is an event listener that is triggered when the Spring Boot
	 * application is fully initialized and ready to accept requests. It is
	 * responsible for loading access roles or permissions. for the
	 * {@code ApplicationReadyEvent}, which is fired when the application context is
	 * fully initialized and ready.
	 * @param scope The {@code ApplicationReadyEvent} that triggers this method. It
	 *              contains information about the application context.
	 */
	public Map<Integer, String> loadAccessRoles(String scope) {
		if (roleAccessMap == null || scope != null && !scope.equals(this.scope)) {
			List<ScreenRolesDao> screenRoles = null;
			if (CommonConstant.SCOPE_RESEA_CONFIG.equals(scope)) {
				screenRoles = screenRoleAccessRepo.findAllByScreenId(screenIdReseaConfig);
			} else if (scope != null) {
				screenRoles = screenRoleAccessRepo.findAllByScreenId(screenId);
			}
			if (screenRoles != null) {
				roleAccessMap = screenRoles.stream()
						.collect(Collectors.toMap(ScreenRolesDao::getRollId, val -> String.valueOf(val.getSrlAccessCode())));
			}
		}
		return roleAccessMap;
	}

}
