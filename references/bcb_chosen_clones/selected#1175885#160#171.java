    public static double[][] transpose(double[][] buf, double[][] A, int heightA, int widthA) {
        buf = create(buf, widthA, heightA, false);
        if (buf == A) {
            double t;
            for (int i = 0; i < widthA; i++) for (int j = i + 1; j < heightA; j++) {
                t = A[i][j];
                A[i][j] = A[j][i];
                A[j][i] = t;
            }
        } else for (int i = 0; i < widthA; i++) for (int j = 0; j < heightA; j++) buf[i][j] = A[j][i];
        return buf;
    }
