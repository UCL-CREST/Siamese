    public double[][] getTransposta(double[][] matriz) {
        double[][] nova = new double[matriz.length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                nova[i][j] = matriz[j][i];
            }
        }
        return nova;
    }
