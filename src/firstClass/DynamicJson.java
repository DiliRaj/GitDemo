package firstClass;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethods;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	public void addBookJson(String isbn, String aisle) throws IOException {
		
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type","application/json")
		//.body(Payload.addBook(isbn,aisle))
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\DilipRajendran\\Desktop\\addbook.json"))))		
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	@DataProvider(name="BooksData")
	public Object[][] dataset() {
		
		return new Object[][] {{"def","2279"},{"efg","2280"},{"fgh","2281"}};
	}

}
