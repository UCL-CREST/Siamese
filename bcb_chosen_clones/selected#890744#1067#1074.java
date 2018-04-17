    public static void mattran_j(double a[][], double at[][], int n, int p) {
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < p; j++) {
                at[j][i] = a[i][j];
            }
        }
    }
