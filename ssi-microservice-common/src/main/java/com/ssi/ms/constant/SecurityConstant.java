package com.ssi.ms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Praveenraja Paramsivam
 * SecurityConstant class provides token types, claim keys and jwt body.
 */
public interface SecurityConstant {
    String CLAIMS = "claims";
    /**
     * Enumeration representing claim keys for JWT body.
     */
    @Getter
    @AllArgsConstructor
    enum ClaimsBody {
        USER_ID("userId"), CLAIM_ID("claimId"), CLAIMANT_ID("cmtId"), SCOPES("scopes"),
        ROLE_ID("roleId");
        private String value;
    }
    /**
     * Enumeration representing token types.
     */
    @AllArgsConstructor
    @Getter
    enum TokenType  {
        ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");
        private String value;
    }
}
