    public static int[][] reversalXandY(final int[][] array) {
        int col = array[0].length;
        int row = array.length;
        int[][] result = new int[col][row];
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                result[x][y] = array[y][x];
            }
        }
        return result;
    }
