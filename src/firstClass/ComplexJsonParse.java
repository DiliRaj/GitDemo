package firstClass;


import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		JsonPath js = new JsonPath(Payload.coursePrice());
		
		//Print no. of courses
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//Print purchase amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);
		
		//Print title of 1st course
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println(titleFirstCourse);
		
		//Print all course titles and price
		for(int i=0;i<count;i++) {
			String courseTitles = js.get("courses["+i+"].title");
			System.out.println(js.getInt("courses["+i+"].price"));
			System.out.println(courseTitles);
		}
		
		//Print no. of copies sold by RPA
		for(int i=0;i<count;i++) {
			String courseTitles = js.get("courses["+i+"].title");
			if(courseTitles.equalsIgnoreCase("RPA")) {
				int copies = (js.getInt("courses["+i+"].copies"));
				System.out.println(copies);
				break;
			}

	}

}
	
}
