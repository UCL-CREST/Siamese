    public static void copyWithClose(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } catch (IOException ioe) {
            try {
                if (os != null) os.close();
            } catch (Exception e) {
            }
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
