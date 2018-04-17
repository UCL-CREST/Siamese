    protected void addFile(String filePath, InputStream content, ZipOutputStream out) throws CompileException {
        try {
            byte[] buf = new byte[1024];
            out.putNextEntry(new ZipEntry(filePath));
            for (int len = 0; (len = content.read(buf)) > 0; ) {
                out.write(buf, 0, len);
            }
        } catch (Exception ex) {
            String msg = "Unable to add an entry to the ZIP file.";
            throw new CompileException(msg, ex);
        }
    }
