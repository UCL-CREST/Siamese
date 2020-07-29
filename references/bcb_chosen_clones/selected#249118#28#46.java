    public static File doCreateZipFile(File[] files, File zipFile) throws IOException {
        byte[] buf = new byte[1024];
        zipFile.getParentFile().mkdirs();
        FileOutputStream fileOut = new FileOutputStream(zipFile);
        ZipOutputStream out = new ZipOutputStream(fileOut);
        for (int i = 0; i < files.length; i++) {
            FileInputStream in = new FileInputStream(files[i]);
            out.putNextEntry(new ZipEntry(files[i].getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
        fileOut.close();
        return zipFile;
    }
