    public static final TreeSet<String> getValues(String baseurl, String rftId, String svcId) {
        TreeSet<String> values = new TreeSet<String>();
        String[] fragments = rftId.split("/");
        String e_repoUri = null;
        String e_svcId = null;
        try {
            e_repoUri = URLEncoder.encode(rftId, "UTF-8");
            e_svcId = URLEncoder.encode(svcId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException resulted attempting to encode " + rftId);
        }
        String openurl = baseurl + "/" + fragments[2] + "/openurl-aDORe7" + "?rft_id=" + e_repoUri + "&svc_id=" + e_svcId + "&url_ver=Z39.88-2004";
        log.info("Obtaining Content Values from: " + openurl);
        try {
            URL url = new URL(openurl);
            long s = System.currentTimeMillis();
            URLConnection conn = url.openConnection();
            int timeoutMs = 1000 * 60 * 30;
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            log.info("Query Time: " + (System.currentTimeMillis() - s) + "ms");
            String line;
            while ((line = in.readLine()) != null) {
                values.add(line);
            }
            in.close();
        } catch (Exception ex) {
            log.error("problem with openurl:" + openurl + ex.getMessage());
            throw new RuntimeException(ex);
        }
        return values;
    }
