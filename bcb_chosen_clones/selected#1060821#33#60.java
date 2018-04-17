    public static String validateSession(String sessionid, String servicekey, HttpServletRequest request) {
        if (sessionid == null) {
            return "error";
        }
        String loginapp = SSOFilter.getLoginapp();
        String u = SSOUtil.addParameter(loginapp + "/api/validatesessionid", "sessionid", sessionid);
        u = SSOUtil.addParameter(u, "servicekey", servicekey);
        u = SSOUtil.addParameter(u, "ip", request.getRemoteHost());
        u = SSOUtil.addParameter(u, "url", encodeUrl(request.getRequestURI()));
        u = SSOUtil.addParameter(u, "useragent", request.getHeader("User-Agent"));
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
