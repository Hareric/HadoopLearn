package word;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount
{

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	{

		/**
		 * LongWritable, IntWritable, Text 均是 Hadoop 中实现的用于封装 Java
		 * 数据类型的类，这些类实现了WritableComparable接口，
		 * 都能够被串行化从而便于在分布式环境中进行数据交换，你可以将它们分别视为long,int,String 的替代品。
		 */
		private final static IntWritable one = new IntWritable(1); // 值为1
		private Text word = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			StringTokenizer itr = new StringTokenizer(value.toString()); // 对字符串进行切分
			while (itr.hasMoreTokens())
			{
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable val : values)
			{
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
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
		String[] otherArgs = { "/input", "/output" };
		if (otherArgs.length != 2)
		{
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "word count");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
