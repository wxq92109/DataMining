package cn.edu.gzu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PCY {
	
	public static void CaculateSuport(){
		int items[][] ={{1,2,3},{2,3,4},{3,4,5},{4,5,6},
						{1,3,5},{2,4,6},{1,3,4},{2,4,5},
						{3,5,6},{1,2,4},{2,3,5},{3,4,6}};
		HashMap<String, Integer> surports = new HashMap<String, Integer>();
		for(int i = 0;i < 12;i ++){
			for(int j = 0; j < 3; j++){
				if(surports.containsKey(""+items[i][j])){
					int s = surports.get(""+items[i][j])+1;
					surports.remove(""+items[i][j]);
					surports.put(""+items[i][j], s);
				}
				else{
					surports.put(""+items[i][j], 1);
				}
			}
		}
		Iterator iteror = surports.entrySet().iterator();
		int c = 1;
		while(iteror.hasNext()){
			Map.Entry entry = (Entry) iteror.next();
			System.out.println("第"+c+"项支持度为："+entry.getValue());
		}
		
	}

	public static void main(String[] args) {
		CaculateSuport();
	}
	
}
