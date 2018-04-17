    public ResultSet transpose() {
        int row = numRows(), col = numCols();
        ResultSet result = new ResultSet(col, row, !useRowFormats);
        for (; row >= 0; row--) for (col = numCols(); col >= 0; col--) result.data[col][row] = data[row][col];
        for (int f = prefix.length; f-- > 0; ) {
            result.prefix[f] = prefix[f];
            result.suffix[f] = suffix[f];
            result.multiplier[f] = multiplier[f];
        }
        return result;
    }
