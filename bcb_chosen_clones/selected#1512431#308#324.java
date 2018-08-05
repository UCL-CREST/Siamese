    private void addFileToZip(ZipOutputStream out, String fn, String content) {
        if (fn != null) {
            byte[] buf = new byte[1024];
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
                out.putNextEntry(new ZipEntry(fn));
                int len;
                while ((len = bais.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
