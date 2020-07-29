    public ReqJsonContent(String useragent, String urlstr, String domain, String pathinfo, String alarmMessage) throws IOException {
        URL url = new URL(urlstr);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("user-agent", useragent);
        conn.setRequestProperty("pathinfo", pathinfo);
        conn.setRequestProperty("domain", domain);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
            response = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            jsonContectResult = response.toString();
        } catch (SocketTimeoutException e) {
            log.severe(alarmMessage + "-> " + e.getMessage());
            jsonContectResult = null;
        } catch (Exception e) {
            log.severe(alarmMessage + "-> " + e.getMessage());
            jsonContectResult = null;
        }
    }
