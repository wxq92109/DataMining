

public class ProduceSimilarityMatrix implements Base{
		public double[][] produceSimilarityMatrix(int[][] preference) {
		double[][] similarityMatrix = new double[PREFROWCOUNT][PREFROWCOUNT];
		for (int i = 0; i < PREFROWCOUNT; i++) {
			for (int j = 0; j < PREFROWCOUNT; j++) {
				if (i == j) {
					similarityMatrix[i][j] = 1;
				}
				else {
					similarityMatrix[i][j] = 
							new ComputeSimilarity().computeSimilarity(preference[i], preference[j]);
				}			
			}
		}
		return similarityMatrix;
	}

}
