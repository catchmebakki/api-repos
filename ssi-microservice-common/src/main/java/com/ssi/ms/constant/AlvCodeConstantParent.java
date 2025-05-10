package com.ssi.ms.constant;

import java.util.stream.Stream;

/**
 * @author Praveenraja Paramsivam
 * This class contains an interface status codes along with their associated numeric codes.
 */
public interface AlvCodeConstantParent {
	/**
	 * Retrieve the code value.
	 *
	 * @return {@link Integer} The code value as an Integer.
	 */
    Integer getCode();
    /**
     * Retrieve an Enum instance of the specified class based on the provided token representing an ALV code.
     *
     * @param cls {@link Class<E>} The Class of the Enum to retrieve.
     * @param token {@link Object} The ALV code token to search for.
     * @param <E> {@link E} The type of the Enum.
     * @return {@link Enum<E> & AlvCodeConstantParent>} The Enum instance matching the provided ALV code token, or null if not found.
     */
    static <E extends Enum<E> & AlvCodeConstantParent> E forAlvCode(Class<E> cls, Object token) {
        return Stream.of(cls.getEnumConstants())
                .filter(e -> e.getCode().equals(token))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown token '"
                        + token + "' for enum " + cls.getName()));
    }
}
