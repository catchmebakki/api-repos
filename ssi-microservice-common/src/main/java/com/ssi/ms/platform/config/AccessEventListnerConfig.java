package com.ssi.ms.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.ssi.ms.common.service.ScreenRolesAccessService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author munirathnam.surepall The {@code ScreenRolesAccessService} class
 *         provides methods to manage access control for screens based on user
 *         roles.
 */
@Configuration
@Slf4j
public class AccessEventListnerConfig {

	@Autowired
	private ScreenRolesAccessService accessService;

	/**
	 * This method is an event listener that is triggered when the Spring Boot
	 * application is fully initialized and ready to accept requests. It is
	 * responsible for loading access roles or permissions. Note that the method is
	 * annotated with {@code @EventListener} and listens for the
	 * {@code ApplicationReadyEvent}, which is fired when the application context is
	 * fully initialized and ready.
	 * @param event The {@code ApplicationReadyEvent} that triggers this method. It
	 *              contains information about the application context.
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void loadAccessRoleOnStartup() {
		accessService.loadAccessRoles(null);
		log.info("loaded successfully on startup");
	}
}
