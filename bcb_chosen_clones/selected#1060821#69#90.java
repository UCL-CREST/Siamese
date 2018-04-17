    public static String callApi(String api, String paramname, String paramvalue) {
        String loginapp = SSOFilter.getLoginapp();
        String u = SSOUtil.addParameter(loginapp + "/api/" + api, paramname, paramvalue);
        u = SSOUtil.addParameter(u, "servicekey", SSOFilter.getServicekey());
        String response = "error";
        try {
            URL url = new URL(u);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response = line.trim();
            }
            reader.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        if ("error".equals(response)) {
            return "error";
        } else {
            return response;
        }
    }
