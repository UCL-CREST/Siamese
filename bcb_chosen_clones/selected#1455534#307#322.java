    protected void copy(URL url, File file) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = url.openStream();
            out = new FileOutputStream(file);
            IOUtils.copy(in, out);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
