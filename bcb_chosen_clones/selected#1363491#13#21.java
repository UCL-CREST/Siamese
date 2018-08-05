    public Object send(URL url, Object params) throws Exception {
        params = processRequest(params);
        String response = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        response += in.readLine();
        while (response != null) response += in.readLine();
        in.close();
        return processResponse(response);
    }
