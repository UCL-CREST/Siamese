    public static float[][] Transpose(float[][] a) {
        int righe = a.length;
        int colonne = a[0].length;
        float m[][] = new float[colonne][righe];
        for (int i = 0; i < colonne; i++) for (int j = 0; j < righe; j++) {
            m[i][j] = a[j][i];
        }
        return m;
    }
