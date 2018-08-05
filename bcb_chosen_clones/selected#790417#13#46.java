    public static void main(String[] args) throws IOException {
        PostParameter a1 = new PostParameter("v", Utils.encode("1.0"));
        PostParameter a2 = new PostParameter("api_key", Utils.encode(RenRenConstant.apiKey));
        PostParameter a3 = new PostParameter("method", Utils.encode("feed.publishTemplatizedAction"));
        PostParameter a4 = new PostParameter("call_id", System.nanoTime());
        PostParameter a5 = new PostParameter("session_key", Utils.encode("5.b2ca405eef80b4da1f68d0df64e471be.86400.1298372400-350727914"));
        PostParameter a8 = new PostParameter("format", Utils.encode("JSON"));
        PostParameter a9 = new PostParameter("template_id", Utils.encode("1"));
        PostParameter a10 = new PostParameter("title_data", Utils.encode("\"conteng\":\"xkt\""));
        PostParameter a11 = new PostParameter("body_data", Utils.encode("\"conteng\":\"xkt\""));
        RenRenPostParameters ps = new RenRenPostParameters(Utils.encode(RenRenConstant.secret));
        ps.addParameter(a1);
        ps.addParameter(a2);
        ps.addParameter(a3);
        ps.addParameter(a4);
        ps.addParameter(a5);
        ps.addParameter(a8);
        ps.addParameter(a9);
        ps.addParameter(a10);
        ps.addParameter(a11);
        System.out.println(RenRenConstant.apiUrl + "?" + ps.generateUrl());
        URL url = new URL(RenRenConstant.apiUrl + "?" + ps.generateUrl());
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("POST");
        System.out.println("Sending request...");
        request.connect();
        System.out.println("Response: " + request.getResponseCode() + " " + request.getResponseMessage());
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String b = null;
        while ((b = reader.readLine()) != null) {
            System.out.println(b);
        }
    }
