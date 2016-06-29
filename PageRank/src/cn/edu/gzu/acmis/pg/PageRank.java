/**
 * 
 */
package cn.edu.gzu.acmis.pg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


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
public class PageRank extends Configured implements Tool {

	public static class PRMap extends Mapper<LongWritable, Text, Text, Text> {

		HashMap<String, Double> pr = new HashMap<String, Double>();

		@Override
		public void setup(Context context) {
			Configuration conf = context.getConfiguration();
			String prDir = conf.get("PRDIR");
			try {
				FileReader fr = new FileReader(prDir);
				BufferedReader br = new BufferedReader(fr);
				String prStr = "";
				while ((prStr = br.readLine()) != null) {
					String[] values = prStr.split("\\s+");
					//System.out.println("---: "+prStr);
					if (!pr.containsKey(values[0])) {
						pr.put(values[0], Double.valueOf(values[1]));
					}
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			
			String line = value.toString();
			//System.out.println("22222222222222222" + line);
			String[] words = line.split("\\s+");
			String ll = words[0];
			int num = words.length - 1;
			double newpr = pr.get(ll) / num;
			// context.write(new Text("1"),new Text("1"));
			for (int i = 0; i < num; i++) {
				context.write(new Text(words[i + 1]), new Text(newpr + ""));
			}

		}
	}

	public static class PRReduce extends Reducer<Text, Text, Text, Text> {

		
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			double pr = 0;
			for (Text tep : values) {
				double tmp = Double.parseDouble(tep.toString());
				pr += tmp;
			}
			context.write(key, new Text(pr + ""));

		}
	}

	public static void main(String[] args) throws Exception {
		Date startTime = new Date();
		Configuration conf = new Configuration();
		//Runtime.getRuntime().exec("rm -rf output");
		conf.set("PRDIR", "pr/pr.txt");
		double distance=Double.MAX_VALUE;
		int i=0;
		while(distance>0.00000000001){
			args[1] = "output/" + i;
			ToolRunner.run(conf, new PageRank(), args);
			String lastFilePath=conf.get("PRDIR");
			System.out.println("这是第 "+i+"次迭代 : "+lastFilePath);
			String nextFilePath="output/" + i + "/part-r-00000";
			distance = Convergence.calDistance(lastFilePath, nextFilePath);
			conf.set("PRDIR", nextFilePath);
			i++;
		}
		Date endTime = new Date();
		long time = endTime.getTime() - startTime.getTime();
        long costSeconds = time / 1000 % 60;
        long costMinutes = time / (60 * 1000) % 60;
        long costHours = time / (60 * 60 * 1000) % 24;
        long costDays = time / (24 * 60 * 60 * 1000);
		
		System.out.println("PageRank计算时间总花费：:"+costDays+"天"+costHours+"小时"+costMinutes+"分"+costSeconds+"秒");
	}

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();

		Job job = Job.getInstance(conf);
		job.setJarByClass(PageRank.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(PRMap.class);
		job.setReducerClass(PRReduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

}
