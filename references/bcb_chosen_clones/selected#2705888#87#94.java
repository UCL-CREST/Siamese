    public void localTranspose() {
        double[][] data = new double[cols()][rows()];
        for (int i = 0; i < rows(); i++) for (int j = 0; j < cols(); j++) data[j][i] = mData[i][j];
        int t = rows();
        numRows = cols();
        numCols = t;
        mData = data;
    }
