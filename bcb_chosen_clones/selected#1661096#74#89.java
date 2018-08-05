    private void copyPhoto(final IPhoto photo, final Map.Entry<String, Integer> size) {
        final File fileIn = new File(storageService.getPhotoPath(photo, storageService.getOriginalDir()));
        final File fileOut = new File(storageService.getPhotoPath(photo, size.getKey()));
        InputStream fileInputStream;
        OutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(fileIn);
            fileOutputStream = new FileOutputStream(fileOut);
            IOUtils.copy(fileInputStream, fileOutputStream);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (final IOException e) {
            log.error("file io exception", e);
            return;
        }
    }
