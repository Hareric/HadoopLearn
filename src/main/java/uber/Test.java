package uber;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;;


public class Test
{
	public static void main(String[] args)
	{
		
		String specifyDate = "asdfasdfasd";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y");
		LocalDate formatted = LocalDate.parse(specifyDate,formatter); 
		System.out.println(formatted.getDayOfWeek()); 
		//输出
	}
}