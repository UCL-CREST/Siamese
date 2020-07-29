    public static int[][] mergeColumns(int[]... x) {
        int[][] array = new int[x[0].length][x.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = x[j][i];
            }
        }
        return array;
    }
