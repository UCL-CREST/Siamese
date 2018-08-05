    public static void zip(File[] sourceFiles, File destFile, boolean deleteOriginalFiles, boolean includeFullPathName) throws IOException {
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destFile));
        for (File sourceFile : sourceFiles) {
            FileInputStream in = new FileInputStream(sourceFile);
            if (includeFullPathName) {
                out.putNextEntry(new ZipEntry(sourceFile.toString()));
            } else {
                out.putNextEntry(new ZipEntry(sourceFile.getName()));
            }
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
        if (deleteOriginalFiles) {
            for (File sourceFile : sourceFiles) {
                sourceFile.delete();
            }
        }
    }
