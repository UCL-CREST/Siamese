    private boolean verifyAppId(String appid) {
        try {
            String urlstr = "http://" + appid + ".appspot.com";
            URL url = new URL(urlstr);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buf = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            reader.close();
            return buf.toString().contains("hyk-proxy");
        } catch (Exception e) {
        }
        return false;
    }
