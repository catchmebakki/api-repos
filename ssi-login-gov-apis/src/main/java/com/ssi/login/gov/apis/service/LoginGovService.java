package com.ssi.login.gov.apis.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ssi.login.gov.apis.handler.JWTHandler;



@Service
public class LoginGovService {

	@Value("${webservice_path}")
	private String path;

	@Autowired
	private JWTHandler jWTHandler;

	// public LoginGovResDTO getSWARecords(Long swa_xid) {
	// //https://dev-arpaui-ipp.dol.gov/api/swa/699c2832-a181-47fd-99f4-a339c4434139
	// https://stage-arpaui-ipp.dol.gov/api/swa/?swa_xid=699c2832-a181-47fd-99f4-a339c4434139&lang_cd=en
	public ResponseEntity<String> getSWARecords() {
		final String jwtToken = "JWT " + jWTHandler.getJWT();
		final String currentTimestamp = Math.floor(System.currentTimeMillis() / 1000) + "";
		final String hexString = Math.floor(Math.random() * 16) + "";

		System.out.println("LoginGovService.getSWARecords()" + jwtToken);
		// String jwtToken = "JWT
		// eyJhbGciOiJFUzI1NiIsImtpZCI6ImFMcWlOZWMyU3pRX1VLcjUwcHlqdmFsZnJnNFNkLTd2ZlZwOWp2LUU4dEkifQ.eyJpYXQiOjE3MDYzNzgyOTcsImlzcyI6Ik5IIiwibm9uY2UiOiJmNjJkMjZjZTc1YmE4YzlhIn0.TM-PGRsXuggbYBHmNSymF0ofQIM5PMOAVvCLX-Kx8xDFyFrOlKeXwdIdp6hS2ykGOqcLAb91N9oV63bXXqoeIw";

		String swa_xid = "699c2832-a181-47fd-99f4-a339c4434139";
		String url = path + "/swa/v1/claims/" + swa_xid;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			// HttpHeaders headers = getHeaders();
			headers.set("Authorization", jwtToken);
			HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);

			Map<String, String> params = new HashMap<String, String>();
			params.put("currentTimestamp", currentTimestamp);
			params.put("hexString", hexString);

			// headers.setBearerAuth(jwtToken);
			// HttpEntity<String> entity = new HttpEntity<>("body", headers);
			System.out.println("LoginGovService.getSWARecords()" + jwtToken);
			ResponseEntity<String> responseUUID = restTemplate.exchange(url, HttpMethod.GET, jwtEntity, String.class,
					params);

			System.out.println("LoginGovService.getSWARecords()" + responseUUID);
			// String responseUUID = restTemplate.getForObject(url, entity, String.class);

			return responseUUID;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public Response getSWARecords1() {

		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://stage1-unemployment.dol.gov/swa/v1/claims/699c2832-a181-47fd-99f4-a339c4434139/")
					.method("GET", body)
					.addHeader("Authorization",
							"JWT eyJhbGciOiJFUzI1NiIsImtpZCI6ImFMcWlOZWMyU3pRX1VLcjUwcHlqdmFsZnJnNFNkLTd2ZlZwOWp2LUU4dEkifQ.eyJpYXQiOjE3MDY1NTA3MjksImlzcyI6Ik5IIiwibm9uY2UiOiJiZGQ0Mzc2NTFmMzJiY2Y5In0.MmgUYdTOPCQ0K1rPzuwyjrToPxEF0MgI1Fky6yK_9FYaouFar0g0uENd07uoNV75npoxIS03lHzaBYzANKMUiw")
					.build();
			Response response = client.newCall(request).execute();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/*public HttpResponse getSWARecords2() {

		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest
					.get("https://stage1-unemployment.dol.gov/swa/v1/claims/699c2832-a181-47fd-99f4-a339c4434139/")
					.header("Authorization",
							"JWT eyJhbGciOiJFUzI1NiIsImtpZCI6ImFMcWlOZWMyU3pRX1VLcjUwcHlqdmFsZnJnNFNkLTd2ZlZwOWp2LUU4dEkifQ.eyJpYXQiOjE3MDY1NTU0MTIsImlzcyI6Ik5IIiwibm9uY2UiOiJlYWE3ZTFkMmM1ZTUzMTJhIn0.rso7tTOJY153rAZFzv3RCJH40vnC14YZ5VaNlEsbY2X1HwPu3ffiR5boJQtYpoBgPgmF0ZuLa-KoAFfmru1ZJw")
					.asString();

			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
}
