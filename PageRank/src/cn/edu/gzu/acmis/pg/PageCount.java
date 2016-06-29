package cn.edu.gzu.acmis.pg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * PageRank
 * @author wxq
 * June 27th 2016
 */
public class PageCount extends Configured implements Tool {

	public static String pr_value = "";
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();
			String[] str = line.split("\\s+");
			//System.out.println(line);
			context.write(value, new Text(str[1]));

		}
	}

//	public static class NewPartitioner extends HashPartitioner<Text,Text>{    
//		@Override
//	    public int getPartition(Text key, Text value, int numReduceTasks){   
//			String term = key+"\t"+value; 
//			System.out.println("This is Paritioner "+key+value);
//	        return super.getPartition(new Text(term), value, numReduceTasks); 
//	    }
//	}
	
	public static class NewCombiner extends Reducer<Text,Text,Text,Text>{
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			Iterator<Text> itr=values.iterator();
			String term = key.toString().split("\\s+")[0];
			context.write(new Text(term), itr.next());
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String str = "";

			for (Text tep : values) {
				
				String linked = tep.toString();
				str += linked + " ";
			}
			String newKey=key.toString().split("\\s+")[0];
			pr_value += newKey+" "+"1\n";
			//System.out.println("key:"+newKey);
			context.write(new Text(newKey), new Text(str.trim()));

		}
	}

	public static void main(String[] args) throws Exception {
		//Runtime.getRuntime().exec("rm -rf input");
		Date startTime = new Date();
		ToolRunner.run(new Configuration(), new PageCount(), args);
		Date endTime = new Date();
		long time = endTime.getTime() - startTime.getTime();
        long costSeconds = time / 1000 % 60;
        long costMinutes = time / (60 * 1000) % 60;
        long costHours = time / (60 * 60 * 1000) % 24;
        long costDays = time / (24 * 60 * 60 * 1000);
		
		System.out.println("Combiner计算时间总花费：:"+costDays+"天"+costHours+"小时"+costMinutes+"分"+costSeconds+"秒");
		File writename = new File("pr/pr.txt"); 
		writename.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
        out.write(pr_value); // \r\n即为换行  
        out.flush(); // 把缓存区内容压入文件  
        out.close(); // 最后记得关闭文件  
	}

	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();

		Job job = Job.getInstance(conf);
		job.setJarByClass(PageCount.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setCombinerClass(NewCombiner.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	
}
