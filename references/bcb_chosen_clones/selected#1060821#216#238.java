    public static String getRolesString(HttpServletRequest hrequest, HttpServletResponse hresponse, String username, String servicekey) {
        String registerapp = SSOFilter.getRegisterapp();
        String u = SSOUtil.addParameter(registerapp + "/api/getroles", "username", username);
        u = SSOUtil.addParameter(u, "servicekey", servicekey);
        String roles = "";
        try {
            URL url = new URL(u);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                roles = line.trim();
            }
            reader.close();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        if ("error".equals(roles)) {
            return "";
        }
        return roles.trim();
    }
