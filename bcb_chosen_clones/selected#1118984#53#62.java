    public static byte[] readUrl(URL url) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        try {
            IOUtils.copy(is, os);
            return os.toByteArray();
        } finally {
            is.close();
        }
    }
