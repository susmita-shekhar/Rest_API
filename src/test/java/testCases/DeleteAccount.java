package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class DeleteAccount extends GenerateTokenBearer {
	String baseUri;
	String deleteAccountEndPoint;
	String AccountEndPoint;
	String headerContentType;
	String getSingleAccountEndPoint;
	String deleteAccountId;
	
	public static Logger logger = LogManager.getLogger(DeleteAccount.class);
	

	public DeleteAccount()
	{
		baseUri = getValueFromProperty("baseURL");
		getSingleAccountEndPoint = getValueFromProperty("getSingleAccountInfoEndPoint");
		deleteAccountEndPoint = getValueFromProperty("deleteOneAccountEndPoint");
		headerContentType = getValueFromProperty("contentType");
		deleteAccountId = "766";
	}
	
	
	@Test(priority = 1)
	public void deleteAccount()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.queryParam("account_id", deleteAccountId)
			.log().all().
		when()
			.delete(deleteAccountEndPoint).
		then()
			.log().all()
			.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response code:" + responseCode);
		Assert.assertEquals(responseCode, 200, "Status codes are NOT matching!");

		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response header content type:" + responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType, "Content types are NOT matching!");


		String responseBody = response.getBody().asString(); // Get the body as a string.
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String accountUpdatedMessage = jp.getString("message");
		logger.info("*****Actual Account Updated:******" + accountUpdatedMessage);
		Assert.assertEquals(accountUpdatedMessage, "Account deleted successfully.","Account delete message not matched");
		
		}
	
	
	@Test(priority = 2)
	public void getSingleAccountInfo()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.queryParam("account_id", deleteAccountId)
			.log().all().
		when()
			.get(getSingleAccountEndPoint).
		then()
			.log().all()
			.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response code:" + responseCode);
		Assert.assertEquals(responseCode, 404, "Status codes are NOT matching!");



	}
	
	
}
