    private void appendTempFile(File temp, OutputStream out) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(temp);
            IOUtils.copy(in, out);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }
