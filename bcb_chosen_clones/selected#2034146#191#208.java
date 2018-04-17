    protected File getZipFile() throws IOException {
        File tempFile = File.createTempFile("eb_" + getName(), BackupGroup.COMPRESS_ZIP);
        tempFile.deleteOnExit();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempFile));
        InputStream in = null;
        in = ((BrowsableFile) getItem()).getInputStream();
        String zipEntryName = getName();
        out.putNextEntry(new ZipEntry(zipEntryName));
        int length = 0;
        byte[] buffer = new byte[1024];
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.closeEntry();
        in.close();
        out.close();
        return tempFile;
    }
