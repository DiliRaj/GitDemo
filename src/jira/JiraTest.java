package jira;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import groovy.util.logging.Log;

public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		//Login to get session id
		SessionFilter cookie = new SessionFilter();
		given().log().all().header("Content-Type","application/json")
		.body("{\r\n" + 
				"    \"username\": \"diliprajendran\",\r\n" + 
				"    \"password\": \"tamilnadu\"\r\n" + 
				"}")
		.filter(cookie).when().post("rest/auth/1/session").then().log().all();
		
		//adding comment to issue
		String expectedMessage = "I am not adding same comments";
		String addComment = given().pathParam("issueId", "10100").log().all().header("Content-Type","application/json")
		.body("{\r\n" + 
				"    \"body\": \""+expectedMessage+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
		.filter(cookie).when().post("rest/api/2/issue/{issueId}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js = new JsonPath(addComment);
		String commentId = js.get("id");
		
		//Add attachment
		
		  given().header("X-Atlassian-Token","no-check").pathParam("issueId", "10100").
		  header("Content-Type","multipart/form-data")
		  .multiPart("file", new File("C:\\Users\\DilipRajendran\\Downloads\\invoice-PRAAN2646.pdf"))
		  .filter(cookie) .when().post("rest/api/2/issue/{issueId}/attachments")
		  .then().log().all().assertThat().statusCode(200);
		 
		
		//get issue details
		String issueDetails = given().pathParam("issueId", "10100").queryParam("fields", "comment").filter(cookie).log().all()
		.when().get("rest/api/2/issue/{issueId}")
		.then().log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) {
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id");
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String actualMessage = js1.get("fields.comment.comments["+i+"].body");
				System.out.println(actualMessage);
				Assert.assertEquals(actualMessage, expectedMessage);
			}
		}

	}

}
