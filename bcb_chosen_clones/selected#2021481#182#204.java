    public static boolean writeZipFile(byte[] content, String entry, File file) {
        if (content == null || content.length == 0) {
            Exception e = new Exception();
            e.fillInStackTrace();
            logger.log(Level.SEVERE, "Tried to zip an empty file!  Send this output to a dev" + " and describe what you were doing.", e);
            return false;
        }
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
            zos.setLevel(9);
            ZipEntry ze = new ZipEntry(entry);
            ze.setSize(content.length);
            zos.putNextEntry(ze);
            zos.write(content);
            zos.flush();
            zos.closeEntry();
            zos.close();
            return true;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Exception thrown in writeZipFile(byte[] content, String entry, File file)", e);
            return false;
        }
    }
