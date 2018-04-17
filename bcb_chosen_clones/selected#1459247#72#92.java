    public static boolean execute(final File source, final File destination, final boolean deleteSource) throws IOException {
        if (source.exists()) {
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(destination);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry zipEntry = new ZipEntry(source.getName());
            zos.putNextEntry(zipEntry);
            byte[] inbuf = new byte[8102];
            int n;
            while ((n = fis.read(inbuf)) != -1) {
                zos.write(inbuf, 0, n);
            }
            zos.close();
            fis.close();
            if (deleteSource && !source.delete()) {
                LogLog.warn("Unable to delete " + source.toString() + ".");
            }
            return true;
        }
        return false;
    }
