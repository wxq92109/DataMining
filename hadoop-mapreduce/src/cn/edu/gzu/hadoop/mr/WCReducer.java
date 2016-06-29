package cn.edu.gzu.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	@Override
	protected void reduce(Text key, Iterable<LongWritable> v2s,Context context)
			throws IOException, InterruptedException {
		//接收数据
		//定义一个计数器
		long counter = 0;
		//循环v2s
		for(LongWritable l : v2s){
			counter += l.get();
		}
		//输出
		context.write(key, new LongWritable(counter));
		
	}
	
}
