

import java.util.ArrayList;
import java.util.List;

public class GetScore implements Base {
	
	public static double [][]getScore(int[][] user_movie_base,double[][] combineMatrix ){
	
	double[][] matrix = new double[PREFROWCOUNT][PREFROWCOUNT];
	// 进行评分预测
	List<Integer> neighborSerial = new ArrayList<Integer>();
	for (int i = 0; i < KNEIGHBOUR; i++) {
		neighborSerial.clear();
		double max = 0;
		int j = 0;
		int itemSerial = 0;
		int itemId = 0;
		for (; j < PREFROWCOUNT; j++) {
			if (user_movie_base[i][j] == 0) {
				double similaritySum = 0;
				double sum = 0;
				double score = 0;
				// 该方法有三个参数，score表示某一用户对所有项目的评分；i表示某个项目的序号combineMatrix表示项目间的相似性
				neighborSerial = new FindKNeighbors().findKNeighbors(user_movie_base[i], j, combineMatrix);
				for (int m = 0; m < neighborSerial.size(); m++) {
					sum += combineMatrix[j][neighborSerial.get(m)]* user_movie_base[i][neighborSerial.get(m)];
					similaritySum += combineMatrix[j][neighborSerial.get(m)];
				}
				if (similaritySum == 0)
					score = 0;
				else
					score = sum / similaritySum;
				itemId = j;
				matrix[i][itemId] = score;
			}
			
		}
		

	}
	return matrix;
	}
}
