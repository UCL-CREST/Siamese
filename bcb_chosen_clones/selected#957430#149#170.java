    public static void writeZipFile(byte[] content, String entry, File file) {
        if (content.length == 0) {
            Exception e = new Exception();
            e.fillInStackTrace();
            logger.log(Level.SEVERE, "Tried to zip an empty file!  Send this output to a dev" + " and describe what you were doing.", e);
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(entry);
            ze.setSize(content.length);
            zos.putNextEntry(ze);
            zos.write(content);
            zos.flush();
            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception thrown in writeZipFile(byte[] content, String entry, File file)", e);
        }
    }
