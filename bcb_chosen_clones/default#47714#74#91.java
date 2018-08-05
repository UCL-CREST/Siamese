    private String getFanFouMessage(int pageNo) throws IOException {
        URL url = new URL("http://api.fanfou.com/statuses/user_timeline.json?page=" + pageNo);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("GET");
        String basicAuth = Base64.encode((getUsername() + ":" + getPassword()).getBytes());
        request.addRequestProperty("Authorization", "Basic " + basicAuth);
        System.out.println("Sending request...");
        request.connect();
        System.out.println("Response: " + request.getResponseCode() + " " + request.getResponseMessage());
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String b = null;
        StringBuffer sb = new StringBuffer();
        while ((b = reader.readLine()) != null) {
            sb.append(b);
        }
        return sb.toString();
    }
