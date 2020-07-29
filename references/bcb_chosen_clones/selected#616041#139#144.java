    private void nextZipEntry(String url, int level) throws IOException {
        if (!url.startsWith("META-INF/") && !url.startsWith("OEBPS/")) url = "OEBPS/" + url;
        ZipEntry ze = new ZipEntry(url);
        ze.setMethod(ZipEntry.DEFLATED);
        tmpzos.putNextEntry(ze);
    }
