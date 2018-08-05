    public static float[][] transpose(float[][] f) {
        int m = f.length;
        int n = f[1].length;
        float[][] ft = new float[n][m];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
            if (f[j].length > i) ft[i][j] = f[j][i];
            System.out.println(i + ":" + j + " " + ft[i][j]);
        }
        return ft;
    }
