    private void addEntry(ZipOutputStream out, String filePath) throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(filePath);
        out.putNextEntry(new ZipEntry(filePath));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
    }
