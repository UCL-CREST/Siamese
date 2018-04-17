    public static AnimationHelper makeObject(String fileName, int row, int col, int tileWidth, int tileHeight) {
        String key = fileName.trim().toLowerCase();
        AnimationHelper animation = (AnimationHelper) animations.get(key);
        if (animation == null) {
            LTexture[][] images = TextureUtils.getSplit2Textures(fileName, tileWidth, tileHeight);
            LTexture[][] result = new LTexture[row][col];
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
