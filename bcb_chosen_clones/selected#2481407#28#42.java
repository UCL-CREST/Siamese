    protected void writeFile(String fileName, OutputStream outputStream) throws IOException {
        File f = new File(getLogRootPath(), fileName);
        FileInputStream is = new FileInputStream(f);
        java.util.zip.ZipOutputStream zipOutputStream = new java.util.zip.ZipOutputStream(outputStream);
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(f.getName());
        zipOutputStream.putNextEntry(entry);
        byte buf[] = new byte[10000];
        int len;
        while ((len = is.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, len);
        }
        is.close();
        zipOutputStream.flush();
        zipOutputStream.close();
    }
