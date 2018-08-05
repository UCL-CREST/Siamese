    public static void zipFile(File inputFile, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buf = new byte[1024];
        zipOutputStream.putNextEntry(new ZipEntry(inputFile.getName()));
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        int length;
        while ((length = fileInputStream.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, length);
        }
        fileInputStream.close();
        zipOutputStream.closeEntry();
    }
