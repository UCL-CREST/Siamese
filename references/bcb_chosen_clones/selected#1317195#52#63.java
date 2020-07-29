    public static double[][] transpose(double[][] in) {
        assert in != null;
        int h = in.length;
        int w = in[0].length;
        double[][] out = new double[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                out[x][y] = in[y][x];
            }
        }
        return out;
    }
