    public static final File zipCompress(File file) throws IOException {
        File zipFile = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            String name = file.getName() + ".zip";
            zipFile = TempFileUtil.createTempFileWithName(name);
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    ZipEntry ze = new ZipEntry(subFile.getName());
                    zos.putNextEntry(ze);
                    zos.write(IOUtil.getBytes(subFile));
                    zos.flush();
                }
            } else {
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                zos.write(IOUtil.getBytes(file));
                zos.flush();
            }
        } finally {
            IOUtil.safeClose(zos);
            IOUtil.safeClose(fos);
        }
        return zipFile;
    }
