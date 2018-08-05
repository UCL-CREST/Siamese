    public static SRPGAnimation makeObject(String fileName, int tileWidth, int tileHeight, LColor col) {
        String key = fileName.trim().toLowerCase();
        SRPGAnimation animation = (SRPGAnimation) animations.get(key);
        if (animation == null) {
            LImage image = GraphicsUtils.loadNotCacheImage(fileName);
            int c = col.getRGB();
            int wlength = image.getWidth() / tileWidth;
            int hlength = image.getHeight() / tileHeight;
            LImage[][] images = new LImage[wlength][hlength];
            Rect srcR = new Rect();
            Rect dstR = new Rect();
            Canvas canvas = GraphicsUtils.canvas;
            for (int y = 0; y < hlength; y++) {
                for (int x = 0; x < wlength; x++) {
                    Bitmap bitmap = Bitmap.createBitmap(tileWidth, tileHeight, Config.ARGB_4444);
                    srcR.set((x * tileWidth), (y * tileHeight), tileWidth + (x * tileWidth), tileHeight + (y * tileHeight));
                    dstR.set(0, 0, tileWidth, tileHeight);
                    canvas.setBitmap(bitmap);
                    canvas.drawBitmap(image.getBitmap(), srcR, dstR, null);
                    images[x][y] = new LImage(bitmap);
                    LImage tmp = images[x][y];
                    int[] pixels = tmp.getPixels();
                    int size = pixels.length;
                    for (int i = 0; i < size; i++) {
                        if (pixels[i] == c) {
                            pixels[i] = 0xffffff;
                        }
                    }
                    tmp.setPixels(pixels, tmp.getWidth(), tmp.getHeight());
                }
            }
            LImage[][] result = new LImage[hlength][wlength];
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
