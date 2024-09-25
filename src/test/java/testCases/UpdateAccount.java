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

public class UpdateAccount extends GenerateTokenBearer {
	String baseUri;
	String updateAccountEndPoint;
	String getSingleAccountEndPoint;
	String headerContentType;
	
	Map <String, String> UpdateMap;
	
	public static Logger logger = LogManager.getLogger(UpdateAccount.class);
	

	public UpdateAccount()
	{
		baseUri = getValueFromProperty("baseURL");
		getSingleAccountEndPoint = getValueFromProperty("getSingleAccountInfoEndPoint");
		updateAccountEndPoint = getValueFromProperty("updateAccountInfoEndPoint");
		headerContentType = getValueFromProperty("contentType");
		UpdateMap = new HashMap <String,String>();
	}
	
	
	public Map<String,String> getUpdateMap()
	{
		UpdateMap.put("account_id", "911");
		UpdateMap.put("account_name", "MD Techfios account 111");
		UpdateMap.put("account_number", "123456789");
		UpdateMap.put("description", "Test description 1");
		UpdateMap.put("balance", "1000.22");
		UpdateMap.put("contact_person", "MD Islam");
		return UpdateMap;
		
	}

	@Test(priority = 1)
	public void updateAccount()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.body(getUpdateMap())
			.log().all().
		when()
			.put(updateAccountEndPoint).
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
		Assert.assertEquals(accountUpdatedMessage, "Account updated successfully.","Account updated message not matched");
		
		}
	
	
	@Test(priority = 2)
	public void getSingleAccountInfo()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.queryParam("account_id", getUpdateMap().get("account_id"))
			.log().all().
		when()
			.get(getSingleAccountEndPoint).
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
		String actualAccountName = jp.getString("account_name");
		logger.info("*****Actual Account Name:******" + actualAccountName);
		
		
		String actualAccountNumber = jp.getString("account_number");
		logger.info("*****Actual Account Number:******" + actualAccountNumber);
		
		
		String actualDescription = jp.getString("description");
		logger.info("*****Actual Description:******" + actualDescription);
		
		String actualBalance = jp.getString("balance");
		logger.info("*****Actual Balance:******" + actualBalance);

		
		String actualContactPerson = jp.getString("contact_person");
		logger.info("*****Actual Contact Person:******" + actualContactPerson);
		
		String expectedAccountName = getUpdateMap().get("account_name");
		logger.info("*****Expected Account Name:******" + expectedAccountName);
		Assert.assertEquals(actualAccountName, expectedAccountName,"Account names are not matching");
		
		String expectedAccountNumber = getUpdateMap().get("account_number");
		logger.info("*****Expected Account Number:******" + expectedAccountNumber);
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber,"Account numbers are not matching");
		
		String expectedDescription = getUpdateMap().get("description");
		logger.info("*****Expected Description:******" + expectedDescription);
		Assert.assertEquals(actualDescription, expectedDescription,"Account descriptions are not matching");
		
		String expectedBalance = getUpdateMap().get("balance");
		logger.info("*****Expected Balance:******" + expectedBalance);
		Assert.assertEquals(actualBalance, expectedBalance,"Account balances are not matching");
		
		String expectedContactPerson = getUpdateMap().get("contact_person");
		logger.info("*****Expected Contact Person:******" + expectedContactPerson);
		Assert.assertEquals(actualContactPerson, expectedContactPerson,"Account contact persons names are not matching");
	}
	
	
}
