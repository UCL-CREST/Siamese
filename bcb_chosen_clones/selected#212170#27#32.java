    public void addImage(byte[] image) throws IOException {
        ZipEntry zImage = new ZipEntry("photo.jpg");
        kmz.putNextEntry(zImage);
        kmz.write(image);
        kmz.closeEntry();
    }
