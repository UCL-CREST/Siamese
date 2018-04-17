    List<String> HttpGet(URL url) throws IOException {
        List<String> responseList = new ArrayList<String>();
        Logger.getInstance().logInfo("HTTP GET: " + url, null, null);
        URLConnection con = url.openConnection();
        con.setAllowUserInteraction(false);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) responseList.add(inputLine);
        in.close();
        return responseList;
    }
