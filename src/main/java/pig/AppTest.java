package pig; 


import org.apache.pig.pigunit.PigTest;

import junit.framework.*;

public class AppTest extends TestCase
{
	public void testStudentsPigScript() throws Exception
	{
		PigTest pigTest = new PigTest("src/main/java/pig/wordcount.pig");
		pigTest.assertOutput("D", new String[] { "(2,No)", "(3,Ha!)", "(1,Yes)", "(1,Open)", "(3,Papa)", "(1,your)",
				"(1,Johny)", "(1,lies?)", "(1,Eating)", "(1,Johny!)", "(1,mouth!)", "(1,sugar?)", "(1,Telling)"});
		
	}
	
//	public static void main(String[] args) throws IOException, ParseException
//	{
//		PigTest pigTest = new PigTest("src/main/java/pig/wordcount.pig");
////		pigTest.assertOutput("D", new String[] { "(2,No)", "(3,Ha!)", "(1,Yes)", "(1,Open)", "(3,Papa)", "(1,your)",
////				"(1,Johny)", "(1,lies?)", "(1,Eating)", "(1,Johny!)", "(1,mouth!)", "(1,sugar?)", "(1,Telling)"});
//		
//		pigTest.runScript();
//	}
}
