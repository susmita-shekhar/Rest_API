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

public class CreateNewAccount extends GenerateTokenBearer {
	String baseUri;
	String createAccountEndPoint;
	String getSingleAccountEndPoint;
	String getAllAccountEndPoint;
	String headerContentType;
	String createAccountFilePath;
	String firstAccountId;
	public static Logger logger = LogManager.getLogger(CreateNewAccount.class);
	

	public CreateNewAccount()
	{
		baseUri = getValueFromProperty("baseURL");
		getSingleAccountEndPoint = getValueFromProperty("getSingleAccountInfoEndPoint");
		createAccountEndPoint = getValueFromProperty("createNewAccountEndPoint");
		getAllAccountEndPoint = getValueFromProperty("getAllAccountInfoEndPoint");
		headerContentType = getValueFromProperty("contentType");
		createAccountFilePath = "src\\main\\java\\data\\createAccountBody.json";
	}
	

	@Test(priority = 1)
	public void createNewAccount()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.body(new File(createAccountFilePath))
			.log().all().
		when()
			.post(createAccountEndPoint).
		then()
			.log().all()
			.extract().response();
		
		int responseCode = response.getStatusCode();
		System.out.println("Response code:" + responseCode);
		Assert.assertEquals(responseCode, 201, "Status codes are NOT matching!");

		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response header content type:" + responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, headerContentType, "Content types are NOT matching!");


		String responseBody = response.getBody().asString(); // Get the body as a string.
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String accountCreatedMessage = jp.getString("message");
		logger.info("*****Actual Account Created:******" + accountCreatedMessage);
		Assert.assertEquals(accountCreatedMessage, "Account created successfully.","Account created message not matched");
		
		}
	
	@Test(priority = 2)
	public void getAllAccounts()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.auth().preemptive().basic("demo1@codefios.com", "abc123")
			.log().all().
		when()
			.get(getAllAccountEndPoint).
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
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("First Account ID:" + firstAccountId);
		logger.info("*****First Account ID:******" + firstAccountId);
	}
	
	@Test(priority = 3)
	public void getSingleAccountInfo()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.queryParam("account_id", firstAccountId)
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
		
		File expectedRequestBody = new File(createAccountFilePath);
		JsonPath jp2 = new JsonPath(expectedRequestBody);
		
		String expectedAccountName = jp.getString("account_name");
		logger.info("*****Expected Account Name:******" + expectedAccountName);
		Assert.assertEquals(actualAccountName, expectedAccountName,"Account names are not matching");
		
		String expectedAccountNumber = jp.getString("account_number");
		logger.info("*****Expected Account Number:******" + expectedAccountNumber);
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber,"Account numbers are not matching");
		
		String expectedDescription = jp.getString("description");
		logger.info("*****Expected Description:******" + expectedDescription);
		Assert.assertEquals(actualDescription, expectedDescription,"Account descriptions are not matching");
		
		String expectedBalance = jp.getString("balance");
		logger.info("*****Expected Balance:******" + expectedBalance);
		Assert.assertEquals(actualBalance, expectedBalance,"Account balances are not matching");
		
		String expectedContactPerson = jp.getString("contact_person");
		logger.info("*****Expected Contact Person:******" + expectedContactPerson);
		Assert.assertEquals(actualContactPerson, expectedContactPerson,"Account contact persons names are not matching");
	}
	
	
}
