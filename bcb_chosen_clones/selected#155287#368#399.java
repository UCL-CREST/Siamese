    private String save(UploadedFile imageFile) {
        try {
            File saveFld = new File(imageFolder + File.separator + userDisplay.getUser().getUsername());
            if (!saveFld.exists()) {
                if (!saveFld.mkdir()) {
                    logger.info("Unable to create folder: " + saveFld.getAbsolutePath());
                    return null;
                }
            }
            File tmp = File.createTempFile("img", "img");
            IOUtils.copy(imageFile.getInputstream(), new FileOutputStream(tmp));
            File thumbnailImage = new File(saveFld + File.separator + UUID.randomUUID().toString() + ".png");
            File fullResolution = new File(saveFld + File.separator + UUID.randomUUID().toString() + ".png");
            BufferedImage image = ImageIO.read(tmp);
            Image thumbnailIm = image.getScaledInstance(310, 210, Image.SCALE_SMOOTH);
            BufferedImage thumbnailBi = new BufferedImage(thumbnailIm.getWidth(null), thumbnailIm.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics bg = thumbnailBi.getGraphics();
            bg.drawImage(thumbnailIm, 0, 0, null);
            bg.dispose();
            ImageIO.write(thumbnailBi, "png", thumbnailImage);
            ImageIO.write(image, "png", fullResolution);
            if (!tmp.delete()) {
                logger.info("Unable to delete: " + tmp.getAbsolutePath());
            }
            String imageId = UUID.randomUUID().toString();
            imageBean.addImage(imageId, new ImageRecord(imageFile.getFileName(), fullResolution.getAbsolutePath(), thumbnailImage.getAbsolutePath(), userDisplay.getUser().getUsername()));
            return imageId;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Unable to save the image.", t);
            return null;
        }
    }
