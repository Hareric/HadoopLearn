package avroCh12;

import java.io.*;

public class generateCode 
{
	public static void useLinuxCommond(){  
        try {
        	String avroToolPath = "./src/main/avro/avro-tools-1.8.2.jar";
        	String avscPath = "./src/main/resources/avro/User.avsc";
        	String outputPath = "./src/main/resources/avro/";
        	String scheme2code = "java -jar " + avroToolPath + 
        			" compile schema "+avscPath+" " + outputPath;
        	System.out.println(scheme2code);
            Process p = Runtime.getRuntime().exec(scheme2code);  //调用Linux的相关命令  
              
            InputStreamReader ir = new InputStreamReader(p.getInputStream());    
            LineNumberReader input = new LineNumberReader (ir);      //创建IO管道，准备输出命令执行后的显示内容  
              
             String line;    
             while ((line = input.readLine ()) != null){     //按行打印输出内容  
              System.out.println(line);    
            }    
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	
	public static void main(String[] args){
		useLinuxCommond();
	}
}
