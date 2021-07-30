package jira;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import files.Payload;

public class SerializeTest {

	public static void main(String[] args) {
		
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		AddPlace a = new AddPlace();
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		a.setLocation(l);
		a.setAccuracy(50);
		a.setName("Frontline house");
		a.setPhone_number("(+91) 983 893 3937");
		a.setAddress("29, side layout, cohen 09");
		
		List<String> t = new ArrayList<String>();
		t.add("shoe park");
		t.add("shop");
		a.setTypes(t);
		a.setWebsite("http://google.com");
		a.setLanguage("French-IN");
		
		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		
		ResponseSpecification respSpec = new ResponseSpecBuilder().expectStatusCode(200).
				expectContentType(ContentType.JSON).build();
	
		/*
		 * Response res = given().log().all().queryParam("key", "qaclick123").body(a)
		 * .when().post("maps/api/place/add/json")
		 * .then().assertThat().statusCode(200).extract().response();
		 */
		RequestSpecification req = given().log().all().spec(reqSpec).body(a);
		Response res = req.when().post("maps/api/place/add/json")
				   .then().spec(respSpec).extract().response();
		
		String responseString = res.asString();
		System.out.println(responseString);

	}

}
