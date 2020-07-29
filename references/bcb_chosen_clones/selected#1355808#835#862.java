    private static File getZipFile() throws Exception {
        File[] zipFiles = new File[2];
        File zipFile;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            for (int i = 0; i < zipFiles.length; i++) {
                zipFiles[i] = TempFileUtil.createTemporaryFile(".txt");
                Utils.makeTempFile(Utils.makeRandomData((int) (100 * Math.random())), zipFiles[i]);
            }
            zipFile = TempFileUtil.createTempFileWithName("zipTest.zip");
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (File file : zipFiles) {
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                zos.write(IOUtil.getBytes(file));
                zos.flush();
            }
        } finally {
            IOUtil.safeClose(zos);
            IOUtil.safeClose(fos);
            for (File tmp : zipFiles) {
                IOUtil.safeDelete(tmp);
            }
        }
        return zipFile;
    }
