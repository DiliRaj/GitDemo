package jira;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class OAuthTest {

	public static void main(String[] args) {
		//due to google update selenium automation wont work on gmail. Launch below url on browser manually to get auth code
		//https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=ineedcode
		String url = "https://rahulshettyacademy.com/getCourse.php?state=ineedcode&code=4%2F0AX4XfWigvbkIDzBKKEwXrsMBCdaD1XYVCVrb0DFgWi21iL-SBujkqQlzOUhbnKR_wVHxVQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println(code);
		
		//accesstoken
		String accessTokenResponse = given().urlEncodingEnabled(false).
				queryParams("code", code).
				queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
				queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W").
				queryParams("grant_type","authorization_code").
				queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.when().post("https://www.googleapis.com/oauth2/v4/token")
		.asString();
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		//getCourse
		String[] expectedCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor","not equal"};
		GetCourse response = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
		System.out.println(response.getLinkedIn());
		System.out.println(response.getInstructor());
		System.out.println(response.getCourses().getApi().get(1).getCourseTitle());
		
		List<Api> apiCourses = response.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> webAutoCourses = response.getCourses().getWebAutomation();
		for(int j=0;j<webAutoCourses.size();j++) {
			a.add(webAutoCourses.get(j).getCourseTitle());
		}
		List<String> b = Arrays.asList((expectedCourseTitles));
		Assert.assertTrue(b.equals(a));

	}

}
