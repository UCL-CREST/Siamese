    public static byte[] fetchURLData(String url, String proxyHost, int proxyPort) throws IOException {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            if (url.startsWith("file://")) {
                is = new BufferedInputStream(u.openStream());
            } else {
                Proxy proxy;
                if (proxyHost != null) {
                    proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                } else {
                    proxy = Proxy.NO_PROXY;
                }
                con = (HttpURLConnection) u.openConnection(proxy);
                con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6");
                con.addRequestProperty("Accept-Charset", "UTF-8");
                con.addRequestProperty("Accept-Language", "en-US,en");
                con.addRequestProperty("Accept", "text/html,image/*");
                con.setDoInput(true);
                con.setDoOutput(false);
                con.connect();
                is = new BufferedInputStream(con.getInputStream());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(is, baos);
            return baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(is);
            if (con != null) {
                con.disconnect();
            }
        }
    }
