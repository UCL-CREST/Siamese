    private double[][] transpose(double[][] in) {
        double[][] out = new double[in[0].length][in.length];
        for (int r = 0; r < in.length; r++) {
            for (int c = 0; c < in[0].length; c++) {
                out[c][r] = in[r][c];
            }
        }
        return out;
    }
