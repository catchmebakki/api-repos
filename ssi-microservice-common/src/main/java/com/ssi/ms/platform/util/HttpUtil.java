package com.ssi.ms.platform.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Function;
/**
 * @author munirathnam.surepall
 * Interface for HTTP utility functions.
 * Suppresses PMD warnings related to field naming conventions.
 */
@SuppressWarnings("PMD.FieldNamingConventions")
public interface HttpUtil {

    /**
     * This function will return logged in user staffId.
     */
	/**
	 * Retrieve the logged-in staff ID using the provided HttpServletRequest.
	 *
	 * @param httpRequest {@link HttpServletRequest} The HttpServletRequest associated with the request.
	 * @return {@link String} The staff ID of the logged-in user as a String.
	 */
    Function<HttpServletRequest, String> getLoggedInStaffId = httpRequest ->
            (String) ((Map) httpRequest.getAttribute("claims")).get("userId");

	Function<HttpServletRequest, String> getLoggedInRoleId = httpRequest ->
			String.valueOf(((Map) httpRequest.getAttribute("claims")).get("roleId"));
}
