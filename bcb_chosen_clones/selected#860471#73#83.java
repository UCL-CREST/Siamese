    public static LImage[][] reversalXandY(final LImage[][] array) {
        int col = array[0].length;
        int row = array.length;
        LImage[][] result = new LImage[col][row];
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                result[x][y] = array[y][x];
            }
        }
        return result;
    }
