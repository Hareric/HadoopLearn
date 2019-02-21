package uber;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class BasementDayTripCount
{
	public static class ExtractTripMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private IntWritable tripNum; // trip 值
		String specifyDate = "MM/DD/YYYY";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y");  // date转化格式
		LocalDate date;
		String dayOfWeek;

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			String[] splitArray = value.toString().split(","); // 对字符串进行切分
			specifyDate = splitArray[1];
			// 使用try来处理不和谐的数据
			try{
				date = LocalDate.parse(specifyDate,formatter);
				dayOfWeek = date.getDayOfWeek().toString();
				tripNum =  new IntWritable(new Integer(splitArray[3]));
			}
			catch (DateTimeParseException e){
				e.printStackTrace();
				return;
			}

			context.write(new Text(splitArray[0] + "+" + dayOfWeek), tripNum);
		}
	}
	
	public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable val : values)
			{
				sum += val.get();

			}
			context.write(key, new IntWritable(sum));
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		conf.addResource("classpath:/hadoop/core-site.xml");
		conf.addResource("classpath:/hadoop/hdfs-site.xml");
		conf.addResource("classpath:/hadoop/mapred-site.xml");
		// String[] otherArgs = new GenericOptionsParser(conf,
		// args).getRemainingArgs();
		String[] otherArgs = { "/uber", "/uber_output" };
		if (otherArgs.length != 2)
		{
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "Basement Day Trip Count");
		job.setJarByClass(BasementDayTripCount.class);
		job.setMapperClass(ExtractTripMapper.class);
		job.setCombinerClass(SumReducer.class);
		job.setReducerClass(SumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
