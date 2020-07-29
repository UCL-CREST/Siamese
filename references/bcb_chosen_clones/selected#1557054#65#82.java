    protected void writeAlifeImage(ZipOutputStream output, PropertyFile spriteCfg) throws IOException {
        if (!(alifeImage instanceof AlifeSprite)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        AlifeSprite sprite = (AlifeSprite) alifeImage;
        spriteCfg.setProperty(PROPERTY_SECTION_SPRITE, PROPERTY_AUTO_UPDATE).setValue(sprite.isAutoUpdate());
        spriteCfg.setProperty(PROPERTY_SECTION_SPRITE, PROPERTY_LOOPS).setValue(sprite.isLoops());
        int index = 0;
        for (AlifeAnimatedImage alifeAnimatedImage : sprite.getFrames()) {
            if (alifeAnimatedImage.getName() == null) {
                alifeAnimatedImage.setName(String.format("%03d", index++) + "." + IMAGE_FORMAT);
            }
            String frameName = alifeAnimatedImage.getName();
            output.putNextEntry(new ZipEntry(frameName));
            ImageIO.write((RenderedImage) alifeAnimatedImage.getImage(), IMAGE_FORMAT, output);
            spriteCfg.setProperty(PROPERTY_SECTION_DURATION, frameName).setValue(alifeAnimatedImage.getEndTime());
        }
    }
