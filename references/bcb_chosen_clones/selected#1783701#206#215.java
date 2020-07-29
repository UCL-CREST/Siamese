    static double[][] getCorrelationMatrix(Gene[] genes, boolean[] pos) {
        double[][] c = new double[genes.length][genes.length];
        for (int i = 0; i < c.length - 1; i++) {
            for (int j = i + 1; j < c.length; j++) {
                c[i][j] = Pearson.calcCorrelation(genes[i].value, genes[j].value, pos);
                c[j][i] = c[i][j];
            }
        }
        return c;
    }
