    private synchronized File zipTempFile(File tempFile) throws BlogunityException {
        try {
            File zippedFile = new File(BlogunityManager.getSystemConfiguration().getTempDir(), tempFile.getName() + ".zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zippedFile));
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            FileInputStream fis = new FileInputStream(tempFile);
            ZipEntry anEntry = new ZipEntry(tempFile.getName());
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
            zos.close();
            return zippedFile;
        } catch (Exception e) {
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FEED_ZIP_FAILED, e));
        }
    }
