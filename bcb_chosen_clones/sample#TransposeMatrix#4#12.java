	public static int[][] transpose(int[][] m) {
		int[][] retval = new int[m[0].length][m.length];
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) {
				retval[j][i] = m[i][j];
			}
		}
		return retval;
	}
