    public static boolean processUrl(String urlPath, UrlLineHandler handler) {
        boolean ret = true;
        URL url;
        InputStream in = null;
        BufferedReader bin = null;
        try {
            url = new URL(urlPath);
            in = url.openStream();
            bin = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bin.readLine()) != null) {
                if (!handler.process(line)) break;
            }
        } catch (IOException e) {
            ret = false;
        } finally {
            safelyClose(bin, in);
        }
        return ret;
    }
