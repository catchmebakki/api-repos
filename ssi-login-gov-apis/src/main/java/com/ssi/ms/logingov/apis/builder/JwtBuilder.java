package com.ssi.ms.logingov.apis.builder;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64URL;
import com.ssi.ms.logingov.apis.constant.ApplicationConstants;

/**
 * JwtBuilder is a utility class for building JSON Web Tokens (JWTs). It
 * provides methods to create JWTs with a specified subject and expiration time.
 * @Author: munirathnam.surepall
 */
public class JwtBuilder {

	private final ECPrivateKey privateKey;
	private final String issuer;
	private final String alogrithm;
	private final String kid;

	/**
	 * Constructs a JwtBuilder object.
	 *
	 * @param privateKey the private key used for signing the JWT tokens
	 * @param kid        the key ID associated with the key used for signing
	 * @param issuer     the issuer of the JWT token
	 * @param algorithm  the cryptographic algorithm used for signing the JWT token
	 * @throws InvalidKeySpecException  if the provided key specification is invalid
	 * @throws NoSuchAlgorithmException if the specified algorithm is not available
	 */
	public JwtBuilder(String privateKey, String kid, String issuer, String algorithm)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		Security.addProvider(new BouncyCastleProvider());
		this.privateKey = this.buildPrivateKey(privateKey);
		this.kid = kid;
		this.issuer = issuer;
		this.alogrithm = algorithm;
	}

	// This method takes a String representation of a private key in PEM format
	// and returns an ECPrivateKey object.
	private ECPrivateKey buildPrivateKey(String privateKeyString)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		final KeyFactory keyPairGenerator = KeyFactory.getInstance(ApplicationConstants.ALG_EC);
		return (ECPrivateKey) keyPairGenerator.generatePrivate(
				new PKCS8EncodedKeySpec(Base64.decodeBase64(removeEncapsulationBoundaries(privateKeyString))));
	}

	/**
	 * Removes unnecessary characters from the key.
	 *
	 * @param key The private key that should have spaces, newlines, header, and
	 *            footer removed
	 * @return The private key with spaces, newlines, header, and footer removed
	 */
	private static String removeEncapsulationBoundaries(String key) {
		return key.replaceAll("\n", "").replaceAll(" ", "").replaceAll("-{5}[a-zA-Z]*-{5}", "");
	}

	/**
	 * Decrypts the cipherText using the provided parameters.
	 * 
	 * @param cipherText   The encrypted text to decrypt.
	 * @param epkX         The X coordinate of the ephemeral public key.
	 * @param epkY         The Y coordinate of the ephemeral public key.
	 * @param iviv         The initialization vector used for decryption.
	 * @param authTag      The authentication tag used for decryption.
	 * @param encryptedKey The encrypted key used for decryption.
	 * @param aad          Additional authenticated data used during decryption.
	 * @return The decrypted plaintext.
	 * @throws JOSEException If an error occurs during decryption.
	 */
	public String decrypt(String cipherText, String epkX, String epkY, String iviv, String authTag, String encryptedKey,
			String aad) throws JOSEException {
		final ECDHDecrypter decrypter = new ECDHDecrypter(this.privateKey);

		final Base64URL xxxx = new Base64URL(epkX);
		final Base64URL yyyy = new Base64URL(epkY);
		final JWK epk = new ECKey.Builder(Curve.P_256, xxxx, yyyy).build();
		final Base64URL ivb64 = new Base64URL(iviv);
		final Base64URL authTagb64 = new Base64URL(authTag);
		final JOSEObjectType joseObjectType = new JOSEObjectType(ApplicationConstants.JWE);
		final JWEHeader header = new JWEHeader(JWEAlgorithm.ECDH_ES_A256KW, EncryptionMethod.A256GCM, joseObjectType,
				null, null, null, null, null, null, null, null, kid, epk, null, null, null, null, 0, ivb64, authTagb64,
				null, null, null);
		final Base64URL encryptedKeyb64 = new Base64URL(encryptedKey);

		final byte[] aadBytes = aad.getBytes();
		final byte[] retvalbytes = decrypter.decrypt(header, encryptedKeyb64, ivb64, new Base64URL(cipherText),
				authTagb64, aadBytes);

		return new String(retvalbytes, StandardCharsets.UTF_8);
	}

}
