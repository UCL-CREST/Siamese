    private static void zip(File srcDir, File originSrcDir, ZipOutputStream dstStream) throws IOException {
        byte[] buffer = new byte[ZIP_BUFFER_SIZE];
        int bytes = 0;
        String[] dirList = srcDir.list();
        for (int i = 0; i < dirList.length; i++) {
            File file = new File(srcDir, dirList[i]);
            if (file.isDirectory()) {
                zip(file, originSrcDir, dstStream);
                continue;
            }
            FileInputStream fis = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(TFileHandler.removeDirectoryPath(originSrcDir.getAbsolutePath(), file.getAbsolutePath()));
            dstStream.putNextEntry(entry);
            while ((bytes = fis.read(buffer)) != -1) dstStream.write(buffer, 0, bytes);
            fis.close();
        }
    }
