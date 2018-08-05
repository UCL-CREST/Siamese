    private HashSet<String> href(String urlstr) throws IOException {
        HashSet<String> hrefs = new HashSet<String>();
        URL url = new URL(urlstr);
        URLConnection con = url.openConnection();
        con.setRequestProperty("Cookie", "_session_id=" + _session_id);
        InputStreamReader r = new InputStreamReader(con.getInputStream());
        StringWriter b = new StringWriter();
        IOUtils.copyTo(r, b);
        r.close();
        try {
            Thread.sleep(WAIT_SECONDS * 1000);
        } catch (Exception err) {
        }
        String tokens[] = b.toString().replace("\n", " ").replaceAll("[\\<\\>]", "\n").split("[\n]");
        for (String s1 : tokens) {
            if (!(s1.startsWith("a") && s1.contains("href"))) continue;
            String tokens2[] = s1.split("[\\\"\\\']");
            for (String s2 : tokens2) {
                if (!(s2.startsWith("mailto:") || s2.matches("/profile/index/[0-9]+"))) continue;
                hrefs.add(s2);
            }
        }
        return hrefs;
    }
