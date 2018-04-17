    public static AnimationHelper makeObject(String fileName, int tileWidth, int tileHeight, GLColor col) {
        String key = fileName.trim().toLowerCase();
        AnimationHelper animation = (AnimationHelper) animations.get(key);
        if (animation == null) {
            LTexture texture = TextureUtils.filterColor(fileName, col);
            int wlength = texture.getWidth() / tileWidth;
            int hlength = texture.getHeight() / tileHeight;
            LTexture[][] images = TextureUtils.getSplit2Textures(texture, tileWidth, tileHeight);
            LTexture[][] result = new LTexture[hlength][wlength];
            for (int y = 0; y < wlength; y++) {
                for (int x = 0; x < hlength; x++) {
                    result[x][y] = images[y][x];
                }
            }
            images = null;
            animations.put(key, animation = makeObject(result[0], result[1], result[3], result[2]));
        }
        return animation;
    }
