    protected String issueCommandToServer(String command, ChangeCapsule changeCapsule) throws IOException {
        URLConnection urlConn = serverURL.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
        String content = ApplyChangesServlet.PARAMETER_COMMAND + "=" + command;
        content += "&" + ApplyChangesServlet.PARAMETER_CAPSULE + "=" + URLEncoder.encode(changeCapsule.toJSON(), "UTF-8");
        wr.write(content);
        wr.flush();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        StringBuffer response = new StringBuffer();
        String str;
        while (null != ((str = input.readLine()))) {
            response.append(str);
        }
        wr.close();
        input.close();
        return response.toString();
    }
