    public static double[][] transpose(double[][] object) {
        double[][] transpose = new double[object[0].length][object.length];
        for (int i = 0; i < object.length; i++) {
            for (int x = 0; x < transpose.length; x++) {
                transpose[x][i] = object[i][x];
            }
        }
        return transpose;
    }
