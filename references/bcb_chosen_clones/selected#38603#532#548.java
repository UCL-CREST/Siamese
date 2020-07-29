    private static void addZipEntry(ZipOutputStream zipOut, File zipIn, File root) throws IOException {
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(zipIn));
        byte buffer[] = new byte[1024];
        int length;
        String zipEntryName = replaceSeparator(zipIn.getPath().substring(root.getPath().length() + 1));
        zipOut.putNextEntry(new ZipEntry(zipEntryName));
        try {
            length = inStream.read(buffer);
            while (length != -1) {
                zipOut.write(buffer, 0, length);
                length = inStream.read(buffer);
            }
        } finally {
            zipOut.closeEntry();
            inStream.close();
        }
    }
