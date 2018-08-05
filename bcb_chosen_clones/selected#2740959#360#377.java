    private InputStream open(String url) throws IOException {
        debug(url);
        if (!useCache) {
            return new URL(url).openStream();
        }
        File f = new File(System.getProperty("java.io.tmpdir", "."), Digest.SHA1.encrypt(url) + ".xml");
        debug("Cache : " + f);
        if (f.exists()) {
            return new FileInputStream(f);
        }
        InputStream in = new URL(url).openStream();
        OutputStream out = new FileOutputStream(f);
        IOUtils.copyTo(in, out);
        out.flush();
        out.close();
        in.close();
        return new FileInputStream(f);
    }
