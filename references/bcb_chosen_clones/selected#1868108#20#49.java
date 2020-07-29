    public String selectFROM() throws Exception {
        BufferedReader in = null;
        String data = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            URI uri = new URI("http://**.**.**.**/OctopusManager/index2.php");
            HttpGet request = new HttpGet();
            request.setURI(uri);
            HttpResponse httpresponse = httpclient.execute(request);
            HttpEntity httpentity = httpresponse.getEntity();
            in = new BufferedReader(new InputStreamReader(httpentity.getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            data = sb.toString();
            return data;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
