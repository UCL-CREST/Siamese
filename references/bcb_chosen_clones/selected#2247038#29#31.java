    public static void returnURL(URL url, OutputStream out) throws IOException {
        IOUtils.copy(url.openStream(), out);
    }
