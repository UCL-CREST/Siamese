    public static void synchronizeConnectionTable(int[][] contab) {
        int N = contab.length;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                contab[j][i] = contab[i][j];
            }
        }
    }
