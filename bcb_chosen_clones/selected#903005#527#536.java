    protected void writeImage(ImageDrawComponent image, ZipOutputStream out) {
        try {
            ZipEntry imageEntry = new ZipEntry(IMAGES_DIR + ManifestWriter.getUniqueImageName(image));
            out.putNextEntry(imageEntry);
            out.write(image.getOriginalImageData());
            out.closeEntry();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
