    public static MathComplexMatrix Transpone(MathComplexMatrix matrix) {
        int m = matrix.getM();
        int n = matrix.getN();
        Complex[][] mData = matrix.getAllItems();
        Complex[][] newData = new Complex[n][m];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                newData[ni][mi] = mData[mi][ni];
            }
        }
        return new MathComplexMatrix(newData);
    }
