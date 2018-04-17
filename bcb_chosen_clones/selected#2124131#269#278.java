    private static void addFileToZip(ZipOutputStream zip, String filepath, byte[] buf) throws IOException {
        FileInputStream in = new FileInputStream(filepath);
        zip.putNextEntry(new ZipEntry(filepath));
        int len;
        while ((len = in.read(buf)) > 0) {
            zip.write(buf, 0, len);
        }
        zip.closeEntry();
        in.close();
    }
