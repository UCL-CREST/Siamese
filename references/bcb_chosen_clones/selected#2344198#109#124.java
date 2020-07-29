    public static SRPGAnimation makeObject(String fileName, int row, int col, int tileWidth, int tileHeight) {
        String key = fileName.trim().toLowerCase();
        SRPGAnimation animation = (SRPGAnimation) animations.get(key);
        if (animation == null) {
            Image[][] images = GraphicsUtils.getSplit2Images(fileName, tileWidth, tileHeight);
            Image[][] result = new Image[row][col];
            for (int y = 0; y < col; y++) {
                for (int x = 0; x < row; x++) {
                    result[x][y] = images[y][x];
                }
            }
            images = null;
            animations.put(key, animation = makeObject(result[0], result[1], result[2], result[3]));
        }
        return animation;
    }
