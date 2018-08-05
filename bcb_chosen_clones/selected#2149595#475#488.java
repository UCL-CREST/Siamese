    private double[][] computeCrossCorrelationsMatrix() {
        if (number_of_inputs == 0) return null; else {
            double[][] m = new double[number_of_inputs][number_of_inputs];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j <= i; j++) {
                    if (i == j) m[i][j] = 1; else {
                        m[i][j] = getCorrelation(Integer.toString(2 * i), Integer.toString(2 * j));
                        m[j][i] = m[i][j];
                    }
                }
            }
            return m;
        }
    }
