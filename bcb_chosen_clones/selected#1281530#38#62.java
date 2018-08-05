    private static void zipFile(String folder, File file, ZipOutputStream zipOutputStream, FileFilter filter) throws IOException {
        if (file.isFile()) {
            if (logger.isDebugEnabled()) {
                logger.debug("zipFile(File, ZipOutputStream, FileFilter) - handel file=" + file);
            }
            FileInputStream in = new FileInputStream(file.getPath());
            zipOutputStream.putNextEntry(new ZipEntry(folder + file.getName()));
            int len;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
            in.close();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("zipFile(File, ZipOutputStream, FileFilter) - handel folder=" + file);
            }
            zipOutputStream.putNextEntry(new ZipEntry(folder + file.getName() + "/"));
            zipOutputStream.closeEntry();
            for (File subFile : file.listFiles(filter)) {
                zipFile(folder + file.getName() + "/", subFile, zipOutputStream, filter);
            }
        }
    }
