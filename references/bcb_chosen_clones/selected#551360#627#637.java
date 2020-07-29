    public static LImage[][] getFlipHorizintalImage2D(LImage[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        LImage pixel[][] = new LImage[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixel[i][j] = pixels[j][i];
            }
        }
        return pixel;
    }
