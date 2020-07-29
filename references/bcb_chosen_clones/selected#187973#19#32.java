    public static void notify(String msg) throws Exception {
        String url = "http://api.clickatell.com/http/sendmsg?";
        url = add(url, "user", user);
        url = add(url, "password", password);
        url = add(url, "api_id", apiId);
        url = add(url, "to", to);
        url = add(url, "text", msg);
        URL u = new URL(url);
        URLConnection c = u.openConnection();
        InputStream is = c.getInputStream();
        IOUtils.copy(is, System.out);
        IOUtils.closeQuietly(is);
        System.out.println();
    }
