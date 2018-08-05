    protected void writeGameMapImage(GameMap gameMap) throws IOException {
        if (gameMap == null) {
            throw new IllegalArgumentException("gameMap == null");
        }
        ZipEntry entry = new ZipEntry("gameMaps/" + gameMap.getName() + "/image.png");
        ((ZipOutputStream) out).putNextEntry(entry);
        ImageIO.write(gameMap.getImage(), "png", out);
        ((ZipOutputStream) out).closeEntry();
    }
