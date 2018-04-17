    public java.io.File gzip(java.io.File file) throws Exception {
        java.io.File tmp = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            tmp = java.io.File.createTempFile(file.getName(), ".gz");
            tmp.deleteOnExit();
            is = new BufferedInputStream(new FileInputStream(file));
            os = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)));
            byte[] buf = new byte[4096];
            int nread = -1;
            while ((nread = is.read(buf)) != -1) {
                os.write(buf, 0, nread);
            }
            os.flush();
        } finally {
            os.close();
            is.close();
        }
        return tmp;
    }
