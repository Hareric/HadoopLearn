package youtube;

import java.io.IOException;
import java.util.Arrays;
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

public class FindMaxCategory
{

	public static class CategoryMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1); // 值为1
		private Text category = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			String[] attributeArray = value.toString().split("\t");// 对字符串进行切分
			if (attributeArray.length > 5)  // 忽略属性值少于5的错误数据  
			{
				category.set(attributeArray[3]);
				context.write(category, one);
			}
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
	public static class TwoTuple implements Comparable<TwoTuple>
	{
		public  String first;
		public  int second;

		public TwoTuple(String a, int b)
		{
			first = a;
			second = b;
		}

		public String toString()
		{
			return "(" + first + ", " + second + ")";
		}

		@Override
		public int compareTo(TwoTuple tt)
		{
			return second - tt.second;

		}

	}

	public static class TopNReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		
		int len;
		TwoTuple[] top;

		@Override
		protected void setup(Context context) throws IOException, InterruptedException
		{
			len =context.getConfiguration().getInt("N", 10);  // 从配置中获取top N的N值，若无则默认为10
			top = new TwoTuple[len + 1];
			for (int i=0; i<=len; i++)
			{
				top[i] = new TwoTuple("null", 0);
			}
			
		}
		
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException
		{
			for (int i = len; i > 0; i--)
			{
				context.write(new Text(top[i].first), new IntWritable(top[i].second));
			}
		}

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException
		{
			int sum = 0;
			for (IntWritable val : values)
			{
				sum += val.get();

			}
			add(key.toString(), sum);
		}

		private void add(String key, int val)
		{
			top[0].first = key;
			top[0].second = val;  // 替换掉最小值
			Arrays.sort(top); // 排序，从小到大顺序
		}


	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		conf.addResource("classpath:/hadoop/core-site.xml");
		conf.addResource("classpath:/hadoop/hdfs-site.xml");
		conf.addResource("classpath:/hadoop/mapred-site.xml");
		conf.setInt("N", 5);
		// String[] otherArgs = new GenericOptionsParser(conf,
		// args).getRemainingArgs();
		String[] otherArgs = { "/youtube", "/youtube_category_Top5" };
		if (otherArgs.length != 2)
		{
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "youtube");
		job.setJarByClass(FindMaxCategory.class);
		job.setMapperClass(CategoryMapper.class);
		job.setCombinerClass(SumReducer.class);
//		job.setReducerClass(SumReducer.class);  // 统计每个类别的总量
		job.setReducerClass(TopNReducer.class);  // 统计TopN的类别的总量
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.setInputDirRecursive(job, true);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
