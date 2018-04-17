    @Override
    protected void createTiles() throws InterruptedException, MapCreationException {
        atlasProgress.initMapCreation((xMax - xMin + 1) * (yMax - yMin + 1));
        ImageIO.setUseCache(false);
        int mapWidth = (xMax - xMin + 1) * tileSize;
        int mapHeight = (yMax - yMin + 1) * tileSize;
        int imageWidth = Math.min(1024, mapWidth);
        int imageHeight = Math.min(1024, mapHeight);
        int len = Math.max(mapWidth, mapHeight);
        double scaleFactor = 1.0;
        if (len > 1024) {
            scaleFactor = 1024d / len;
            if (imageWidth != imageHeight) {
                if (imageWidth > imageHeight) imageHeight = (int) (scaleFactor * mapHeight); else imageWidth = (int) (scaleFactor * mapWidth);
            }
        }
        BufferedImage tileImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = tileImage.createGraphics();
        try {
            if (len > 1024) graphics.setTransform(AffineTransform.getScaleInstance(scaleFactor, scaleFactor));
            int lineY = 0;
            for (int y = yMin; y <= yMax; y++) {
                int lineX = 0;
                for (int x = xMin; x <= xMax; x++) {
                    checkUserAbort();
                    atlasProgress.incMapCreationProgress();
                    try {
                        byte[] sourceTileData = mapDlTileProvider.getTileData(x, y);
                        if (sourceTileData != null) {
                            BufferedImage tile = ImageIO.read(new ByteArrayInputStream(sourceTileData));
                            graphics.drawImage(tile, lineX, lineY, Color.WHITE, null);
                        }
                    } catch (IOException e) {
                        log.error("", e);
                    }
                    lineX += tileSize;
                }
                lineY += tileSize;
            }
        } finally {
            graphics.dispose();
        }
        try {
            TileImageJpegDataWriter writer = new TileImageJpegDataWriter(1.0);
            writer.initialize();
            ArrayOutputStream buf = new ArrayOutputStream(MAX_FILE_SIZE);
            byte[] data = null;
            for (int c = 99; c > 50; c -= 5) {
                buf.reset();
                try {
                    writer.processImage(tileImage, buf);
                    data = buf.toByteArray();
                    break;
                } catch (IOException e) {
                    log.trace("Image size too large, increasing compression to 0." + c);
                }
                writer.setJpegCompressionLevel(c / 100f);
            }
            if (data == null) throw new MapCreationException("Unable to create an image with less than 3 MB!");
            kmzOutputStream.putNextEntry(new ZipEntry(imageFileName));
            kmzOutputStream.write(data);
            kmzOutputStream.closeEntry();
        } catch (IOException e) {
            throw new MapCreationException(e);
        }
    }
