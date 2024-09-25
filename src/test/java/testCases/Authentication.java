package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class Authentication extends ConfigReader {
	String baseUri;
	String authEndPoint;
	String authBodyFilePath;
	String headerContentType;
	static long responseTime;
	public static String bearer_token;
	public static Logger logger = LogManager.getLogger(Authentication.class);
	
	/*
	https://qa.codefios.com/api /user/login
	headers:
	Content-Type:application/json
	body:
	{
	"username": "demo1@codefios.com",
	"password": "abc123"
	}
	httpMethod: POST
	given(): all input details -> (baseURI,Headers,Authorization,Payload/Body,QueryParameters)
	when(): submit api requests-> HttpMethod(Endpoint/Resource)
	then(): validate response -> (status code, Headers, responseTime, Payload/Body)
	*/
	
	public Authentication()
	{
		baseUri = getValueFromProperty("baseURL");
		authEndPoint = getValueFromProperty("authenticationEndPoint");
		authBodyFilePath = "src\\main\\java\\data\\authBody.json";
		headerContentType = getValueFromProperty("contentType");
	}
	
	public static boolean compareResponseTime() {
		boolean withinRange = false;

		if (responseTime <= 2000) {
		System.out.println("Response time is within range.");
		logger.info("Response time is within range.");
		withinRange = true;
		} else {
		System.out.println("Response time is out of range!");
		logger.error("Response time is out of range!");
		withinRange = false;
		}
		return withinRange;
	}

	@Test
	public void authentication()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.body(new File(authBodyFilePath))
			.log().all().
		when()
			.post(authEndPoint).
		then()
			.log().all()
			.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response code:" + responseCode);
		Assert.assertEquals(responseCode, 201, "Status codes are NOT matching!");

		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response header content type:" + responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType, "Content types are NOT matching!");

		responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		Assert.assertEquals(compareResponseTime(), true);

		String responseBody = response.getBody().asString(); // Get the body as a string.
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		bearer_token = jp.getString("access_token");
		System.out.println("Bearer token:" + bearer_token);
	}
	
	
}
