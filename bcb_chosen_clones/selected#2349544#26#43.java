    protected static StringBuffer doRESTOp(String urlString) throws Exception {
        StringBuffer result = new StringBuffer();
        String restUrl = urlString;
        int p = restUrl.indexOf("://");
        if (p < 0) restUrl = System.getProperty("fedoragsearch.protocol") + "://" + System.getProperty("fedoragsearch.hostport") + "/" + System.getProperty("fedoragsearch.path") + restUrl;
        URL url = null;
        url = new URL(restUrl);
        URLConnection conn = null;
        conn = url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode((System.getProperty("fedoragsearch.fgsUserName") + ":" + System.getProperty("fedoragsearch.fgsPassword")).getBytes()));
        conn.connect();
        content = null;
        content = conn.getContent();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) content));
        while ((line = br.readLine()) != null) result.append(line);
        return result;
    }
