    public MathRealMatrix transpose() {
        int m = this.M;
        int n = this.N;
        double[][] mData = this.Data;
        double[][] newData = new double[n][m];
        for (int ni = 0; ni < n; ni++) {
            for (int mi = 0; mi < m; mi++) {
                newData[ni][mi] = mData[mi][ni];
            }
        }
        return new MathRealMatrix(newData);
    }
