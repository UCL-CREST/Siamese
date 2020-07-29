    private void getXMLData() {
        String result = null;
        URL url = null;
        URLConnection conn = null;
        BufferedReader rd = null;
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            url = new URL(this.url);
            conn = url.openConnection();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");
            }
            rd.close();
            result = sb.toString();
        } catch (MalformedURLException e) {
            log.error("URL was malformed: {}", url, e);
        } catch (IOException e) {
            log.error("IOException thrown: {}", url, e);
        }
        this.xmlString = result;
    }
