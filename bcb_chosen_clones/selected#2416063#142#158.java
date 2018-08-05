    private synchronized File gzipTempFile(File tempFile) throws BlogunityException {
        try {
            File gzippedFile = new File(BlogunityManager.getSystemConfiguration().getTempDir(), tempFile.getName() + ".gz");
            GZIPOutputStream zos = new GZIPOutputStream(new FileOutputStream(gzippedFile));
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            FileInputStream fis = new FileInputStream(tempFile);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
            zos.close();
            return gzippedFile;
        } catch (Exception e) {
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FEED_GZIP_FAILED, e));
        }
    }
