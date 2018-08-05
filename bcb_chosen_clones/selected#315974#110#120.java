    private void writeFileToZip(ZipOutputStream out, String sourceFilename, String vPath) throws IOException {
        FileInputStream in = new FileInputStream(sourceFilename);
        out.putNextEntry(new ZipEntry(vPath));
        int len;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
    }
