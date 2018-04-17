    public static void returnURL(URL url, Writer out) throws IOException {
        IOUtils.copy(url.openStream(), out);
    }
