    public static double[][] calcTranspose(double[][] mat) {
        double[][] transpose = new double[mat[0].length][mat.length];
        for (int a = 0; a < transpose.length; a++) {
            for (int b = 0; b < transpose[0].length; b++) {
                transpose[a][b] = mat[b][a];
            }
        }
        return transpose;
    }
