    public static String validateAuthTicketAndGetSessionId(ServletRequest request, String servicekey) {
        String loginapp = SSOFilter.getLoginapp();
        String authticket = request.getParameter("authticket");
        String u = SSOUtil.addParameter(loginapp + "/api/validateauthticket", "authticket", authticket);
        u = SSOUtil.addParameter(u, "servicekey", servicekey);
        String sessionid = null;
        try {
            URL url = new URL(u);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sessionid = line.trim();
            }
            reader.close();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        if ("error".equals(sessionid)) {
            return null;
        }
        return sessionid;
    }
