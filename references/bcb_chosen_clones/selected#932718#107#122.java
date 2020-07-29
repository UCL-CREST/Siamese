    public static int[][] loadJustArray(final String fileName) throws IOException {
        List list = loadList(fileName);
        int col = list.size();
        int[][] mapArray = new int[col][];
        for (int i = 0; i < col; i++) {
            mapArray[i] = (int[]) list.get(i);
        }
        int row = (((int[]) mapArray[col > 0 ? col - 1 : 0]).length);
        int[][] result = new int[row][col];
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                result[x][y] = mapArray[y][x];
            }
        }
        return result;
    }
