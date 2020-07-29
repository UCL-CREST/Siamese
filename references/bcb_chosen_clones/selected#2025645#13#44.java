    public static void main(String[] args) throws IOException {
        PostParameter a1 = new PostParameter("v", Utils.encode("1.0"));
        PostParameter a2 = new PostParameter("api_key", Utils.encode(RenRenConstant.apiKey));
        PostParameter a3 = new PostParameter("method", Utils.encode("notifications.send"));
        PostParameter a4 = new PostParameter("call_id", System.nanoTime());
        PostParameter a5 = new PostParameter("session_key", Utils.encode("5.22af9ee9ad842c7eb52004ece6e96b10.86400.1298646000-350727914"));
        PostParameter a6 = new PostParameter("to_ids", Utils.encode("350727914"));
        PostParameter a7 = new PostParameter("notification", "又到了要睡觉的时间了。");
        PostParameter a8 = new PostParameter("format", Utils.encode("JSON"));
        RenRenPostParameters ps = new RenRenPostParameters(Utils.encode(RenRenConstant.secret));
        ps.addParameter(a1);
        ps.addParameter(a2);
        ps.addParameter(a3);
        ps.addParameter(a4);
        ps.addParameter(a5);
        ps.addParameter(a6);
        ps.addParameter(a7);
        ps.addParameter(a8);
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
