    public static void mattran_f77(double a[][], double at[][], int n, int p) {
        int i, j;
        for (i = 1; i <= n; i++) {
            for (j = 1; j <= p; j++) {
                at[j][i] = a[i][j];
            }
        }
    }
