    public static Rectangle2D[][] transpose(Rectangle2D[][] a) {
        int m = a.length;
        int n = a[0].length;
        Rectangle2D[][] t = new Rectangle2D[n][m];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) t[j][i] = a[i][j];
        return t;
    }
