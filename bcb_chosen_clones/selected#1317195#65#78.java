    public static void inPlaceTranspose(double[][] in) {
        assert in != null;
        int h = in.length;
        int w = in[0].length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (x > y) {
                    double f = in[y][x];
                    in[y][x] = in[x][y];
                    in[x][y] = f;
                }
            }
        }
    }
