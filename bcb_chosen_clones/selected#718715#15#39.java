    public static void main(String[] args) throws Exception {
        System.setProperty("debug", "debug");
        OAuthConsumer consumer = new DefaultOAuthConsumer("YmRF4HDvikvwDxYafsaK", "r)q7L!4X$j$nTS0lXAjC=al9Xf*cLOdyFJsy%2OE");
        consumer.setTokenWithSecret("5722da60fee79ef9efc2d383f871d550", "39142f31ad8a7e6ff7b87f36cc9e8f10");
        URL url = new URL("http://api.t.sohu.com/statuses/update.json");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("POST");
        HttpParameters para = new HttpParameters();
        para.put("status", URLEncoder.encode("中 文", "utf-8").replaceAll("\\+", "%20"));
        consumer.setAdditionalParameters(para);
        consumer.sign(request);
        OutputStream ot = request.getOutputStream();
        ot.write(("status=" + URLEncoder.encode("1中 文", "utf-8")).replaceAll("\\+", "%20").getBytes());
        ot.flush();
        ot.close();
        System.out.println("Sending request...");
        request.connect();
        System.out.println("Response: " + request.getResponseCode() + " " + request.getResponseMessage());
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String b = null;
        while ((b = reader.readLine()) != null) {
            System.out.println(b);
        }
    }
