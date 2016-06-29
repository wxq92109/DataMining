package cn.edu.gzu.acmis;

import java.util.ArrayList;

public class Bucket {
	
	public static ArrayList<String> getBuckets(){
		ArrayList<String> buckets = new ArrayList<String>();
		int num = 1;
		for(;num <= 100; num++ )
		{
			System.out.print("第"+num+"桶的数据项为{");
			String data = "";
			for(int i  = 1;i <= num; i++){
				if(num % i == 0)
				{
					data += i+":";
					System.out.print(i+":");
				}
			}
			buckets.add(data);
			System.out.println("}");
		}
		return buckets;
	}
	
	public static void main(String[] args) {
		getBuckets();
	}
}
