    public static int[][] getPixels2D(int[] pixels, int w, int h, int size) {
        int[][] npixels = new int[w][h];
        for (int i = 0; i < npixels.length; i++) {
            for (int j = 0; j < npixels[i].length; j++) {
                npixels[i][j] = pixels[i + j * size];
            }
        }
        int pixel[][] = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixel[i][j] = npixels[j][i];
            }
        }
        return pixel;
    }
