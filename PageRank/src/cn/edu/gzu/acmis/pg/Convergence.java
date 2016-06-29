
package cn.edu.gzu.acmis.pg;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/**
 * PageRank
 * @author wxq
 * June 27th 2016
 */
public class Convergence{
	
	public static double calDistance(String filePath1, String filePath2){
		HashMap<String, Double> map1=Convergence.readFile(filePath1);
		HashMap<String, Double> map2=Convergence.readFile(filePath2);
		double distance=0;
		Set<String> keySet=map1.keySet();
		Iterator<String> itr=keySet.iterator();
		while(itr.hasNext()){
			String key=itr.next();
			System.out.println("key--------------:"+key);
			if(map2.containsKey(key))
			{
				distance+=Math.pow(map1.get(key)-map2.get(key),2);
				System.out.println(map1.get(key)+"------------------"+map2.get(key));
			}
		}
		
		return distance;
		
	}
	
	public static HashMap<String, Double> readFile(String filePath){
		HashMap<String, Double> map = new HashMap<String, Double>();
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String prStr = "";
			
			while ((prStr = br.readLine()) != null) {
				String[] values = prStr.split("\\s+");
				if (!map.containsKey(values[0])) {
					map.put(values[0], Double.valueOf(values[1]));
				}
			}
			
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
