    public static SRPGAnimation makeObject(String fileName, int tileWidth, int tileHeight, Color col) {
        String key = fileName.trim().toLowerCase();
        SRPGAnimation animation = (SRPGAnimation) animations.get(key);
        if (animation == null) {
            Image image = GraphicsUtils.loadNotCacheImage(fileName);
            int c = col.getRGB();
            int wlength = image.getWidth(null) / tileWidth;
            int hlength = image.getHeight(null) / tileHeight;
            Image[][] images = new Image[wlength][hlength];
            for (int y = 0; y < hlength; y++) {
                for (int x = 0; x < wlength; x++) {
                    images[x][y] = GraphicsUtils.createImage(tileWidth, tileHeight, true);
                    Graphics g = images[x][y].getGraphics();
                    g.drawImage(image, 0, 0, tileWidth, tileHeight, (x * tileWidth), (y * tileHeight), tileWidth + (x * tileWidth), tileHeight + (y * tileHeight), null);
                    g.dispose();
                    g = null;
                    PixelGrabber pgr = new PixelGrabber(images[x][y], 0, 0, -1, -1, true);
                    try {
                        pgr.grabPixels();
                    } catch (InterruptedException ex) {
                        ex.getStackTrace();
                    }
                    int pixels[] = (int[]) pgr.getPixels();
                    for (int i = 0; i < pixels.length; i++) {
                        if (pixels[i] == c) {
                            pixels[i] = 0;
                        }
                    }
                    ImageProducer ip = new MemoryImageSource(pgr.getWidth(), pgr.getHeight(), pixels, 0, pgr.getWidth());
                    images[x][y] = GraphicsUtils.toolKit.createImage(ip);
                }
            }
            Image[][] result = new Image[hlength][wlength];
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
