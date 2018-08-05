    public static final double Kernel_Intervalar(double[] dVx, double[] dVy, double[] discre, double paraSimil) {
        double lambda = paraSimil;
        int rangos = discre.length - 1;
        double[][] dist = new double[rangos][rangos];
        for (int i = 0; i < rangos; i++) for (int j = i; j < rangos; j++) if (i == j) dist[i][j] = 0; else {
            dist[i][j] = Math.pow((discre[j] + discre[j + 1]) / 2 - (discre[i] + discre[i + 1]) / 2, 2) + Math.pow((discre[j + 1] - discre[j]) / 2 - (discre[i + 1] - discre[i]) / 2, 2);
            dist[j][i] = dist[i][j];
        }
        double coste = 0.0;
        int sizex = dVx.length;
        for (int i = 0; i < sizex; i++) coste += Math.pow(lambda, dist[(int) dVx[i]][(int) dVy[i]]);
        return coste;
    }
