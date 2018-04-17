    String[][] transpose(String[][] data) {
        int r = data.length;
        int c = data[0].length;
        String matrix[][] = new String[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                matrix[j][i] = data[i][j];
            }
        }
        return matrix;
    }
