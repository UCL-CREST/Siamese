    public static int[][] transpose(int[][] array) {
        int[][] result = new int[array[0].length][array.length];
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                result[i][j] = array[j][i];
            }
        }
        return result;
    }
