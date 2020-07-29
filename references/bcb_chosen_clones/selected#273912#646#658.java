    public static void saveToZipFile(File f, ByteBuffer buf) {
        try {
            FileOutputStream fOut = new FileOutputStream(f);
            ZipOutputStream zipOut = new ZipOutputStream(fOut);
            zipOut.putNextEntry(new ZipEntry("contents"));
            zipOut.write(buf.getBytes());
            zipOut.closeEntry();
            zipOut.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
