    private float[][] Transpose(float[][] a) {
        if (INFO) {
            System.out.println("Performing Transpose...");
        }
        float m[][] = new float[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) for (int j = 0; j < a[i].length; j++) m[j][i] = a[i][j];
        return m;
    }
