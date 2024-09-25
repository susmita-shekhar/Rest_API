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

public class GetSingleAccountInfo extends GenerateTokenBearer {
	String baseUri;
	String getSingleAccountEndPoint;
	String headerContentType;
	public static Logger logger = LogManager.getLogger(GetSingleAccountInfo.class);
	

	
	public GetSingleAccountInfo()
	{
		baseUri = getValueFromProperty("baseURL");
		getSingleAccountEndPoint = getValueFromProperty("getSingleAccountInfoEndPoint");
		headerContentType = getValueFromProperty("contentType");
	}
	

	@Test
	public void getSingleAccountInfo()
	{
		Response response = 
		given()
			.baseUri(baseUri)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateTokenBearer())
			.queryParam("account_id", "911")
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
		Assert.assertEquals(actualAccountName, "MD Techfios account 111","Account names are not matching");
		
		String actualAccountNumber = jp.getString("account_number");
		logger.info("*****Actual Account Number:******" + actualAccountNumber,"Account numbers are not matching");
		Assert.assertEquals(actualAccountNumber, "123456789");
		
		String actualDescription = jp.getString("description");
		logger.info("*****Actual Description:******" + actualDescription);
		Assert.assertEquals(actualDescription, "Test description 1","Account descriptions are not matching");
		
		String actualBalance = jp.getString("balance");
		logger.info("*****Actual Balance:******" + actualBalance);
		Assert.assertEquals(actualBalance, "100.22","Account balances are not matching");
		
		String actualContactPerson = jp.getString("contact_person");
		logger.info("*****Actual Contact Person:******" + actualContactPerson);
		Assert.assertEquals(actualContactPerson, "MD Islam","Account contact persons names are not matching");
	}
	
	
}
