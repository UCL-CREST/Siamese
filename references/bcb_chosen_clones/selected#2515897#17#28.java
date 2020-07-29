    public void transpose() {
        double[][] new_data = new double[size_y][size_x];
        for (int i = 0; i < size_x; i++) {
            for (int j = 0; j < size_y; j++) {
                new_data[j][i] = data[i][j];
            }
        }
        int dummy = size_x;
        size_x = size_y;
        size_y = dummy;
        data = new_data;
    }
