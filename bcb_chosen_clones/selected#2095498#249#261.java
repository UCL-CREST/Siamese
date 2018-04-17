    public static double[][] Transpose(double[][] a) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("Performing Transpose...");
        }
        int tms = a.length;
        double m[][] = new double[tms][tms];
        for (int i = 0; i < tms; i++) {
            for (int j = 0; j < tms; j++) {
                m[i][j] = a[j][i];
            }
        }
        return m;
    }
