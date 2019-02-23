package pig;

import java.io.IOException;
import java.util.Properties;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.backend.executionengine.ExecException;

public class PigRun
{

	public static void runLocal(String path) throws IOException
	{
		// local 模式
		PigServer pigServer1 = new PigServer(ExecType.LOCAL);
		pigServer1.registerScript(path);
	}
	
	public static void runMR(String path) throws IOException
	{
		// MapReduce 模式
				Properties props = new Properties();
				props.setProperty("fs.default.name", "hdfs://localhost:9000");
				// props.setProperty("mapred.job.tracker", "http://localhost:50070");
				PigServer pigServer = new PigServer(ExecType.MAPREDUCE, props);
				pigServer.registerScript(path);
	}

	public static void main(String[] args) throws Exception
	{
		String path = "src/main/java/pig/grammar.pig";
		runLocal(path);
		

	}
}
