    public static double[][] getCorrMatrix(Gene[] gene) {
        double[][] c = new double[gene.length][gene.length];
        for (int i = 0; i < c.length - 1; i++) {
            for (int j = i + 1; j < c.length; j++) {
                c[i][j] = Pearson.calcCorrelation(gene[i].value, gene[j].value);
                c[j][i] = c[i][j];
            }
        }
        return c;
    }
