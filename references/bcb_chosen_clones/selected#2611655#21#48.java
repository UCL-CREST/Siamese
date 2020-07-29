    public String postXmlRequest(String url, String data) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        StringBuffer responseStr = new StringBuffer();
        try {
            System.out.println(data);
            Log4j.logger.info("Request:\n" + data);
            StringEntity reqEntity = new StringEntity(data, "UTF-8");
            reqEntity.setContentType("text/xml");
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            this.setPostSatus(response.getStatusLine().getStatusCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line + "\n");
            }
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(responseStr);
        Log4j.logger.info("Response:\n" + responseStr);
        return responseStr.toString();
    }
