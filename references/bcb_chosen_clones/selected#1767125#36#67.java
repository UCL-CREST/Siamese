    private String clientLogin(AuthInfo authInfo) throws AuthoricationRequiredException {
        logger.fine("clientLogin.");
        try {
            String url = "https://www.google.com/accounts/ClientLogin";
            HttpPost httpPost = new HttpPost(url);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
            params.add(new BasicNameValuePair("Email", authInfo.getEmail()));
            params.add(new BasicNameValuePair("Passwd", new String(authInfo.getPassword())));
            params.add(new BasicNameValuePair("service", "ah"));
            params.add(new BasicNameValuePair("source", "client.kotan-server.appspot.com"));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = clientManager.httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                entity.consumeContent();
                throw new AuthoricationRequiredException(EntityUtils.toString(entity));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.startsWith("Auth=")) {
                    return line.substring("Auth=".length());
                }
            }
            reader.close();
            throw new AuthoricationRequiredException("Login failure.");
        } catch (IOException e) {
            throw new AuthoricationRequiredException(e);
        }
    }
