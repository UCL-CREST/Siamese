    private void update() {
        double[][] data = new double[4][points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < 3; j++) {
                data[j][i] = points[i][j];
            }
            data[3][i] = 1;
        }
        this.matrix = new Matrix(data);
        this.dirty = false;
    }
